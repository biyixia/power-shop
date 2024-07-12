package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Transfee;
import com.bjpowernode.domain.TransfeeFree;
import com.bjpowernode.domain.Transport;
import com.bjpowernode.service.TransfeeFreeService;
import com.bjpowernode.service.TransfeeService;
import com.bjpowernode.service.TransportService;
import com.bjpowernode.mapper.TransportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【transport】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
@RequiredArgsConstructor
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport>
    implements TransportService{
    private final TransfeeFreeService transfeeFreeService;
    private final TransfeeService transfeeService;
    @Override
    public boolean save(Transport transport) {
        transport.setShopId(1L);
        transport.setCreateTime(new DateTime());
        super.save(transport);

        if (transport.getIsFreeFee() == 0) {
            List<Transfee> transfees = transport.getTransfees();
            for (Transfee transfee : transfees) {
                transfee.setTransportId(transport.getTransportId());
            }
            transfeeService.saveBatch(transfees);
        }
        if (transport.getHasFreeCondition() == 1) {
            List<TransfeeFree> transfeeFrees = transport.getTransfeeFrees();
            for (TransfeeFree transfeeFree : transfeeFrees) {
                transfeeFree.setTransportId(transport.getTransportId());
            }
            transfeeFreeService.saveBatch(transfeeFrees);
        }
        return true;
    }


    @Override
    public Transport getById(Long transportId) {
        Transport transport = super.getById(transportId);
        if (transport.getIsFreeFee() == 0) {
            transport.setTransfees(
                    transfeeService.list(
                            new LambdaQueryWrapper<Transfee>()
                                    .eq(Transfee::getTransportId, transportId)
                    )
            );
        }
        if (transport.getIsFreeFee() == 1) {
            ArrayList<Transfee> transfeeArrayList = new ArrayList<>();
            transfeeArrayList.add(Transfee.builder()
                    .firstFee(BigDecimal.valueOf(0))
                    .firstPiece(BigDecimal.valueOf(1))
                    .continuousFee(BigDecimal.valueOf(0))
                    .continuousPiece(BigDecimal.valueOf(1))
                    .build());
            transport.setTransfees(transfeeArrayList);
        }
        if (transport.getHasFreeCondition() == 1) {
            transport.setTransfeeFrees(
                    transfeeFreeService.list(
                            new LambdaQueryWrapper<TransfeeFree>()
                                    .eq(TransfeeFree::getTransportId, transportId)
                    )
            );
        }
        return transport;
    }

    @Override
    public boolean updateById(Transport transport) {
        if (super.updateById(transport)) {
            if (transport.getIsFreeFee() == 0) {
                transfeeService.remove(
                        new LambdaQueryWrapper<Transfee>()
                                .eq(Transfee::getTransportId, transport.getTransportId())
                );
                transfeeService.saveBatch(transport.getTransfees());
            }
            if (transport.getHasFreeCondition() == 1) {
                transfeeFreeService.remove(
                        new LambdaQueryWrapper<TransfeeFree>()
                                .eq(TransfeeFree::getTransportId, transport.getTransportId())
                );
                transfeeFreeService.saveBatch(transport.getTransfeeFrees());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByIds(List<Long> transportIds) {
        for (Long transportId : transportIds) {
            Transport transport = this.getById(transportId);
            if (transport.getIsFreeFee() == 0) {
                transfeeService.remove(
                        new LambdaQueryWrapper<Transfee>()
                                .eq(Transfee::getTransportId, transport.getTransportId())
                );
            }
            if (transport.getHasFreeCondition() == 1) {
                transfeeFreeService.remove(
                        new LambdaQueryWrapper<TransfeeFree>()
                                .eq(TransfeeFree::getTransportId, transport.getTransportId())
                );
            }
        }
        return super.removeByIds(transportIds);
    }
}




