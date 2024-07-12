package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.ScheduleJob;
import com.bjpowernode.service.ScheduleJobService;
import com.bjpowernode.mapper.ScheduleJobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【schedule_job(定时任务)】的数据库操作Service实现
* @createDate 2024-03-14 07:24:21
*/
@Service
@RequiredArgsConstructor
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob>
    implements ScheduleJobService{
    private final ScheduleJobMapper scheduleJobMapper;
    @Override
    public boolean save(ScheduleJob scheduleJob) {
        scheduleJob.setStatus(0);
        scheduleJob.setCreateTime(new DateTime());
        return scheduleJobMapper.insert(scheduleJob) > 0;
    }

    @Override
    public boolean pause(List<Long> scheduleJobIds) {
        return this.updateBatchById(
                scheduleJobIds.stream().map(
                        scheduleJobId -> {
                            return scheduleJobMapper.selectById(scheduleJobId).setStatus(1);
                        }
                ).collect(Collectors.toList())
        );
    }

    @Override
    public boolean resume(List<Long> scheduleJobIds) {
        return this.updateBatchById(
                scheduleJobIds.stream().map(
                        scheduleJobId -> {
                            return scheduleJobMapper.selectById(scheduleJobId).setStatus(0);
                        }
                ).collect(Collectors.toList())
        );
    }

    @Override
    public boolean run(List<Long> scheduleJobIds) {
        return false;
    }
}




