package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.consts.QueueConstant;
import com.bjpowernode.domain.*;
import com.bjpowernode.mapper.ProdCommMapper;
import com.bjpowernode.mapper.ProdMapper;
import com.bjpowernode.mapper.ProdTagReferenceMapper;
import com.bjpowernode.pool.ProductThreadPool;
import com.bjpowernode.repository.ProdRepository;
import com.bjpowernode.service.ImportService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService, CommandLineRunner {

    private final ProdRepository prodRepository;
    private final ProdMapper prodMapper;
    private final ProdTagReferenceMapper prodTagReferenceMapper;
    private final ProdCommMapper prodCommMapper;

    @Value("${es.import.size}")
    private Integer size;

    private DateTime t1;
    private DateTime t2;

    @Override
    public void importAll() {
        List<ProdEs> prodEsList = new ArrayList<>();
        Integer totalCount = prodMapper.selectCount(null);
        int totalPages = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        CountDownLatch countDownLatch = new CountDownLatch(totalPages);
        for (int i = 0; i < totalPages; i++) {
            List<Prod> records = prodMapper.selectPage(new Page<>((long) i + 1, size), null).getRecords();
            //使用线程池来异步执行耗时操作
            ProductThreadPool.threadPoolExecutor.execute(() -> {
                prodRepository.saveAll(
                        translateIntoProdEs(records)
                );
            });
            countDownLatch.countDown();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        t1 = new DateTime();
    }

    private List<ProdEs> translateIntoProdEs(List<Prod> prodList) {
        return prodList.stream().map(
                record -> {
                    Integer praiseNumber = prodCommMapper.selectCount(
                            new LambdaQueryWrapper<ProdComm>()
                                    .eq(ProdComm::getStatus, 1)
                                    .eq(ProdComm::getProdId, record.getProdId())
                                    .eq(ProdComm::getEvaluate, 0)
                    );
                    Integer count = prodCommMapper.selectCount(
                            new LambdaQueryWrapper<ProdComm>()
                                    .eq(ProdComm::getStatus, 1)
                                    .eq(ProdComm::getProdId, record.getProdId())
                    );
                    List<Long> tagList = prodTagReferenceMapper.selectList(
                            new LambdaQueryWrapper<ProdTagReference>()
                                    .eq(ProdTagReference::getProdId, record.getProdId())
                    ).stream().map(ProdTagReference::getTagId).collect(Collectors.toList());
                    return ProdEs.builder()
                            .prodId(record.getProdId())
                            .prodName(record.getProdName())
                            .price(record.getPrice())
                            .soldNum(Long.valueOf(record.getSoldNum()))
                            .brief(record.getBrief())
                            .pic(record.getPic())
                            .status(record.getStatus())
                            .totalStocks(Long.valueOf(record.getTotalStocks()))
                            .categoryId(record.getCategoryId())
                            .tagList(
                                    CollectionUtils.isEmpty(tagList) ? new ArrayList<>() : tagList
                            )
                            .praiseNumber(ObjectUtils.isEmpty(praiseNumber) ? 0 : praiseNumber.longValue())
                            .positiveRating(
                                    count.equals(0) ? BigDecimal.ZERO :
                                            new BigDecimal(praiseNumber).divide(new BigDecimal(count), 2, RoundingMode.HALF_UP)
                                                    .multiply(new BigDecimal(100))
                            )
                            .build();
                }
        ).collect(Collectors.toList());
    }

    @Override
    @Scheduled(initialDelay = 300 * 1000, fixedDelay = 300 * 1000)
    public void updateImport() {
        t2 = new DateTime();
        System.out.println("执行定时任务操作中：t1 = " + t1 + ",t2 = " + t2);
        List<Prod> prodList = prodMapper.selectList(
                new LambdaQueryWrapper<Prod>()
                        .between(Prod::getUpdateTime, t1, t2)
        );
        prodRepository.saveAll(
                translateIntoProdEs(prodList)
        );
        t1 = t2;
    }

    @Override
    @RabbitListener(queues = {QueueConstant.ES_CHANGE_QUEUE})
    public void quickImport(Message message, Channel channel) {
        String json = new String(message.getBody());
        if (StringUtils.isEmpty(json)) {
            try {
                channel.basicAck(
                        message.getMessageProperties().getDeliveryTag(),
                        false
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<ProdSkuCount> prodSkuCountList = JSON.parseArray(json, ProdSkuCount.class);
        List<Prod> prodList = prodMapper.selectList(
                new LambdaQueryWrapper<Prod>()
                        .in(Prod::getProdId,
                                prodSkuCountList.stream().map(ProdSkuCount::getProdId)
                                        .collect(Collectors.toList())
                        )
        );
        List<ProdEs> prodEsList = translateIntoProdEs(prodList);
        for (ProdEs prodEs : prodEsList) {
            int count = prodSkuCountList.stream().filter(
                            prodSkuCount -> prodSkuCount.getProdId().equals(prodEs.getProdId())
                    ).collect(Collectors.toList())
                    .stream().map(ProdSkuCount::getCount).collect(Collectors.toList())
                    .stream().reduce(Integer::sum).get();
            prodEs.setTotalStocks(
                    new BigDecimal(prodEs.getTotalStocks()).subtract(
                            new BigDecimal(count)
                    ).longValue()
            );
            //prodEs.setSoldNum(
            //        new BigDecimal(prodEs.getSoldNum()).subtract(
            //                new BigDecimal(count)
            //        ).longValue()
            //);
        }
        prodRepository.saveAll(prodEsList);
        try {
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(),
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(java.lang.String... args) throws Exception {
        //在服务器启动时将数据加载到es中
        importAll();
    }
}
