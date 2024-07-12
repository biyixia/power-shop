package com.bjpowernode.controller;

import com.bjpowernode.domain.AuthAndMenuVo;
import com.bjpowernode.domain.SysMenu;
import com.bjpowernode.exception.AppException;
import com.bjpowernode.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 查询权限列表和菜单列表数据
     */
    @GetMapping("nav")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public ResponseEntity<AuthAndMenuVo> nav() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        List<SysMenu> menus = sysMenuService.selectMenuList(authentication.getName());
        List<SysMenu> menuList = menus.stream().filter(sysMenu -> 0 == sysMenu.getType()).collect(Collectors.toList());
        menuList.forEach(sysMenu -> sysMenu.setList(
                menus.stream().filter(menu -> menu.getParentId() == sysMenu.getMenuId()).collect(Collectors.toList())
        ));
        return ResponseEntity.ok(
                AuthAndMenuVo.builder()
                        .authorities(authorities)
                        .menuList(menuList)
                        .build()
        );
    }

    @GetMapping("table")
    public ResponseEntity<List<SysMenu>> table(){
        return ResponseEntity.ok(
                sysMenuService.list()
        );
    }

    @GetMapping("list")
    public ResponseEntity<List<SysMenu>> list(){
        return ResponseEntity.ok(
                sysMenuService.list()
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addSysMenu(@RequestBody SysMenu sysMenu) {
        return ResponseEntity.ok(
                sysMenuService.save(sysMenu)
        );
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Boolean> delSysMenu(@PathVariable Long menuId) throws AppException {
        return ResponseEntity.ok(
                sysMenuService.removeById(menuId)
        );
    }
}
