package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.SysRole;
import com.bjpowernode.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 分页查询系统角色列表
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<Page<SysRole>> page(@RequestParam int current,
                                              @RequestParam int size) {
        return ResponseEntity.ok(
                sysRoleService.page(new Page<SysRole>(current, size))
        );
    }

    @GetMapping("list")
    public ResponseEntity<List<SysRole>> list() {
        return ResponseEntity.ok(
                sysRoleService.list()
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addRole(@RequestBody SysRole sysRole) {
        sysRole.setCreateUserId(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        sysRole.setCreateTime(new Date());
        return ResponseEntity.ok(
                sysRoleService.save(sysRole)
        );
    }

    @GetMapping("info/{roleId}")
    public ResponseEntity<SysRole> info(@PathVariable Long roleId) {
        return ResponseEntity.ok(
                sysRoleService.getById(roleId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editRole(@RequestBody SysRole sysRole) {
        return ResponseEntity.ok(
                sysRoleService.updateById(sysRole)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> batchDelete(@RequestBody List<Long> roleIds) {
        return ResponseEntity.ok(
                sysRoleService.removeByRoleIds(roleIds)
        );
    }
}
