package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.ScheduleJob;
import com.bjpowernode.service.ScheduleJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/schedule")
@RequiredArgsConstructor
public class SysScheduleController {
    private final ScheduleJobService scheduleJobService;

    @GetMapping("page")
    public ResponseEntity<Page<ScheduleJob>> page(@RequestParam int page,
                                                  @RequestParam int limit,
                                                  @RequestParam String beanName) {
        return ResponseEntity.ok(
                scheduleJobService.page(new Page<ScheduleJob>())
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addSchedule(@RequestBody ScheduleJob scheduleJob) {
        return ResponseEntity.ok(
                scheduleJobService.save(scheduleJob)
        );
    }

    @PostMapping("pause")
    public ResponseEntity<Boolean> pause(@RequestBody List<Long> scheduleJobIds) {
        return ResponseEntity.ok(
                scheduleJobService.pause(scheduleJobIds)
        );
    }

    @PostMapping("resume")
    public ResponseEntity<Boolean> resume(@RequestBody List<Long> scheduleJobIds) {
        return ResponseEntity.ok(
                scheduleJobService.resume(scheduleJobIds)
        );
    }

    @GetMapping("info/{scheduleJobId}")
    public ResponseEntity<ScheduleJob> info(@PathVariable Long scheduleJobId) {
        return ResponseEntity.ok(
                scheduleJobService.getById(scheduleJobId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> updateSchedule(@RequestBody ScheduleJob scheduleJob) {
        return ResponseEntity.ok(
                scheduleJobService.updateById(scheduleJob)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteScheduleJob(@RequestBody List<Long> scheduleJobIds) {
        return ResponseEntity.ok(
                scheduleJobService.removeByIds(scheduleJobIds)
        );
    }

    // TODO: 2024/3/17
    @PostMapping("run")
    public ResponseEntity<Boolean> run(@RequestBody List<Long> scheduleJobIds) {
        return ResponseEntity.ok(
                scheduleJobService.run(scheduleJobIds)
        );
    }
}
