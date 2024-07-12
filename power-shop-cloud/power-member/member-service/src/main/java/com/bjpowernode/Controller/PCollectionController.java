package com.bjpowernode.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.UserCollection;
import com.bjpowernode.service.UserCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/p/collection")
@RequiredArgsConstructor
public class PCollectionController {
    private final UserCollectionService userCollectionService;

    @GetMapping("prods")
    public ResponseEntity<Page<Prod>> getProds(int current, int size) {
        return ResponseEntity.ok(
                userCollectionService.getProds(new Page<Prod>(current, size))
        );
    }

    @GetMapping("isCollection")
    public ResponseEntity<Boolean> isCollection(@RequestParam("prodId") Long prodId) {
        return ResponseEntity.ok(
                userCollectionService.count(
                        new LambdaQueryWrapper<UserCollection>()
                                .eq(UserCollection::getProdId, prodId)
                                .eq(UserCollection::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
                ) > 0
        );
    }

    @PostMapping("addOrCancel")
    public ResponseEntity<Boolean> addOrCancel(@RequestBody Long prodId) {
        return ResponseEntity.ok(
                userCollectionService.addOrCancel(prodId)
        );

    }

    @GetMapping("count")
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(
                userCollectionService.count(
                        new LambdaQueryWrapper<UserCollection>()
                                .eq(UserCollection::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
                )
        );
    }

}
