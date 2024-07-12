package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.anno.Log;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("info")
    public ResponseEntity<SysUser> getCurrentUser() {
        return ResponseEntity.ok(
                sysUserService.getById(SecurityContextHolder.getContext().getAuthentication().getName())
        );
    }

    @GetMapping("info/{userId}")
    public ResponseEntity<SysUser> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(
                sysUserService.getById(Long.valueOf(userId))
        );
    }

    @Log(operation = "分页查询管理员列表")
    @GetMapping("page")
    public ResponseEntity<Page<SysUser>> page(@RequestParam int current,
                                              @RequestParam int size) {
        return ResponseEntity.ok(
                sysUserService.page(new Page<SysUser>(current, size))
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addSysUser(@RequestBody SysUser sysUser) {
        return ResponseEntity.ok(
                sysUserService.save(sysUser)
        );
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Boolean> delSysUser(@PathVariable String userId) {
        return ResponseEntity.ok(
                sysUserService.removeById(userId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> edit(@RequestBody SysUser sysUser){
        return ResponseEntity.ok(
                sysUserService.updateById(sysUser)
        );
    }

    //---------------------------------------远程调用----------------------------------------------------
    @GetMapping("getById")
    public SysUser getById(@RequestParam("userId") Long userId) {
        return sysUserService.getById(userId);
    }
}
