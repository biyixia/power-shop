package com.bjpowernode.service;

import com.bjpowernode.domain.ScheduleJob;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【schedule_job(定时任务)】的数据库操作Service
* @createDate 2024-03-14 07:24:21
*/
public interface ScheduleJobService extends IService<ScheduleJob> {
    public boolean save(ScheduleJob scheduleJob);

    public boolean pause(List<Long> scheduleJobIds);

    public boolean resume(List<Long> scheduleJobIds);

    public boolean run(List<Long> scheduleJobIds);

}
