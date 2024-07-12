package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Area;
import com.bjpowernode.domain.Transcity;
import com.bjpowernode.domain.Transfee;
import com.bjpowernode.service.AreaService;
import com.bjpowernode.service.TranscityService;
import com.bjpowernode.service.TransfeeService;
import com.bjpowernode.mapper.TransfeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【transfee】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
@RequiredArgsConstructor
public class TransfeeServiceImpl extends ServiceImpl<TransfeeMapper, Transfee>
    implements TransfeeService{
    private final TranscityService transcityService;
    private final AreaService areaService;
    @Override
    public boolean saveBatch(Collection<Transfee> transfees) {
        List<Transcity> transcities = new ArrayList<>();
        if (super.saveBatch(transfees)) {
            ArrayList<Transfee> transfeeList = new ArrayList<>(transfees);
            for (Transfee transfee : transfeeList) {
                transcities.addAll(transfee.getCityList().stream().map(
                        area -> {
                            return Transcity.builder()
                                    .transfeeId(transfee.getTransfeeId())
                                    .cityId(area.getAreaId())
                                    .build();
                        }
                ).collect(Collectors.toList()));
            }
            return transcityService.saveBatch(transcities);
        }
        return false;
    }

    @Override
    public List<Transfee> list(Wrapper<Transfee> queryWrapper) {
        List<Transfee> transfeeList = super.list(queryWrapper);
        for (Transfee transfee : transfeeList) {
            List<Long> cityIdList = transcityService.list(
                    new LambdaQueryWrapper<Transcity>()
                            .eq(Transcity::getTransfeeId, transfee.getTransfeeId())
            ).stream().map(Transcity::getCityId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(cityIdList)) {
                transfee.setCityList(
                        areaService.listByIds(cityIdList)
                );
            }
        }
        return transfeeList;
    }

    @Override
    public boolean remove(Wrapper<Transfee> queryWrapper) {
        List<Long> idList= this.list(queryWrapper).stream().map(Transfee::getTransfeeId).collect(Collectors.toList());
        transcityService.remove(
                new LambdaQueryWrapper<Transcity>()
                        .in(Transcity::getTransfeeId, idList)
        );
        return super.remove(queryWrapper);
    }
}




