package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.ProdComm;
import com.bjpowernode.service.ProdCommService;
import com.bjpowernode.mapper.ProdCommMapper;
import com.bjpowernode.service.ProdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【prod_comm(商品评论)】的数据库操作Service实现
 * @createDate 2024-03-17 18:51:41
 */
@Service
@RequiredArgsConstructor
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm>
        implements ProdCommService {
    private final ProdService prodService;

    @Override
    public Page<ProdComm> page(Page<ProdComm> page) {
        Page<ProdComm> prodCommPage = super.page(page);
        List<ProdComm> prodComms = prodCommPage.getRecords();
        prodComms.forEach(prodComm ->
                prodComm.setProdName(
                        prodService.getById(prodComm.getProdId()).getProdName()
                ));
        return prodCommPage;
    }

    @Override
    public Page<ProdComm> prodCommPageByProd(Long prodId, int current, int size, int evaluate) {
        LambdaQueryWrapper<ProdComm> lqw = new LambdaQueryWrapper<ProdComm>()
                .eq(ProdComm::getProdId, prodId)
                .eq(ProdComm::getStatus, 1);
        switch (evaluate) {
            //全部评论
            case 0:
                break;
            //有图评论
            case 3:
                lqw.exists("select 1 from prod_comm where pics != null");
                break;
            //好评、中评、差评
            default:
                lqw.eq(ProdComm::getEvaluate, evaluate);
                break;
        }
        return super.page(new Page<ProdComm>(current, size), lqw);
    }
}




