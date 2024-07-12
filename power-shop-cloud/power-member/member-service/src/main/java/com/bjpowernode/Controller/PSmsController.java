package com.bjpowernode.Controller;

import com.bjpowernode.domain.PhoneAndCode;
import com.bjpowernode.service.PSmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/p/sms")
@RequiredArgsConstructor
public class PSmsController {
    private final PSmsService pSmsService;

    @PostMapping("send")
    public ResponseEntity<Boolean> send(@RequestBody PhoneAndCode phoneAndCode) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                pSmsService.sendSms(phoneAndCode)
        );
    }

    @PostMapping("savePhone")
    public ResponseEntity<Boolean> savePhone(@RequestBody PhoneAndCode phoneAndCode){
        return ResponseEntity.ok(
                pSmsService.savePhoneNum(phoneAndCode)
        );
    }

}
