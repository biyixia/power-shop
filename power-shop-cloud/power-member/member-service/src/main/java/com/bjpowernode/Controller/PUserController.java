package com.bjpowernode.Controller;

import com.bjpowernode.domain.User;
import com.bjpowernode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/p/user")
@RequiredArgsConstructor
public class PUserController {
    private final UserService userService;

    @PutMapping("setUserInfo")
    public ResponseEntity<Boolean> setUserInfo(@RequestBody User user) {
        return ResponseEntity.ok(
                userService.setUserInfo(user)
        );
    }

}
