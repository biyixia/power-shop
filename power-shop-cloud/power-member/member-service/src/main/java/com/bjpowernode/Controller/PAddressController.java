package com.bjpowernode.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.domain.UserAddr;
import com.bjpowernode.service.UserAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/p/address")
@RequiredArgsConstructor
public class PAddressController {
    private final UserAddrService userAddrService;

    @GetMapping("list")
    public ResponseEntity<List<UserAddr>> list() {
        return ResponseEntity.ok(
                userAddrService.list(
                        new LambdaQueryWrapper<UserAddr>()
                                .eq(UserAddr::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
                )
        );
    }

    @PostMapping("addAddr")
    public ResponseEntity<Boolean> addAddr(@RequestBody UserAddr userAddr) {
        return ResponseEntity.ok(
                userAddrService.save(userAddr)
        );
    }

    @PutMapping("defaultAddr/{addrId}")
    public ResponseEntity<Boolean> setDefaultAddr(@PathVariable String addrId) {
        return ResponseEntity.ok(
                userAddrService.setDefaultAddr(addrId)

        );
    }

    @GetMapping("addrInfo/{addrId}")
    public ResponseEntity<UserAddr> addrInfo(@PathVariable String addrId) {
        return ResponseEntity.ok(
                userAddrService.getById(addrId)
        );
    }

    @PutMapping("updateAddr")
    public ResponseEntity<Boolean> updateAddr(@RequestBody UserAddr userAddr) {
        return ResponseEntity.ok(
                userAddrService.updateById(userAddr)
        );
    }

    @DeleteMapping("deleteAddr/{addrId}")
    public ResponseEntity<Boolean> deleteAddr(@PathVariable String addrId) {
        return ResponseEntity.ok(
                userAddrService.removeById(addrId)
        );
    }

    //-------------------------------远程调用----------------------------------------------------
    @GetMapping("getDefaultAddr")
    public UserAddr getDefaultAddr(String userId) {
        return userAddrService.getOne(
                new LambdaQueryWrapper<UserAddr>()
                        .eq(UserAddr::getUserId, userId)
                        .eq(UserAddr::getCommonAddr, 1)
        );
    }
}
