package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Transcity;
import com.bjpowernode.domain.TranscityFree;
import com.bjpowernode.domain.TransfeeFree;
import com.bjpowernode.service.AreaService;
import com.bjpowernode.service.TranscityFreeService;
import com.bjpowernode.service.TransfeeFreeService;
import com.bjpowernode.mapper.TransfeeFreeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【transfee_free】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
@RequiredArgsConstructor
public class TransfeeFreeServiceImpl extends ServiceImpl<TransfeeFreeMapper, TransfeeFree>
    implements TransfeeFreeService{
    private final TranscityFreeService transcityFreeService;
    private final AreaService areaService;
    @Override
    public boolean saveBatch(Collection<TransfeeFree> transfeeFrees) {
        ArrayList<TranscityFree> transcityFrees = new ArrayList<>();
        if (super.saveBatch(transfeeFrees)) {
            for (TransfeeFree transfeeFree : transfeeFrees) {
                transcityFrees.addAll(
                        transfeeFree.getFreeCityList().stream().map(
                                area -> {
                                    return TranscityFree.builder()
                                            .transfeeFreeId(transfeeFree.getTransfeeFreeId())
                                            .freeCityId(area.getAreaId())
                                            .build();
                                }
                        ).collect(Collectors.toList())
                );
            }
            return transcityFreeService.saveBatch(transcityFrees);
        }
        return false ;
    }

    @Override
    public List<TransfeeFree> list(Wrapper<TransfeeFree> queryWrapper) {
        List<TransfeeFree> transfeeFreeList = super.list(queryWrapper);
        for (TransfeeFree transfeeFree : transfeeFreeList) {
            List<Long> cityIdList = transcityFreeService.list(
                    new LambdaQueryWrapper<TranscityFree>()
                            .eq(TranscityFree::getTransfeeFreeId, transfeeFree.getTransfeeFreeId())
            ).stream().map(TranscityFree::getFreeCityId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(cityIdList)) {
                transfeeFree.setFreeCityList(
                        areaService.listByIds(cityIdList));
            }
        }
        return transfeeFreeList;
    }

    @Override
    public boolean remove(Wrapper<TransfeeFree> queryWrapper) {
        List<Long> idList = this.list(queryWrapper).stream().map(TransfeeFree::getTransfeeFreeId).collect(Collectors.toList());
        transcityFreeService.remove(
                new LambdaQueryWrapper<TranscityFree>()
                        .in(TranscityFree::getTransfeeFreeId, idList)
        );
        return super.remove(queryWrapper);
    }
}




