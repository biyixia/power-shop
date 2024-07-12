package com.bjpowernode.service.impl;

import com.bjpowernode.domain.PhoneAndCode;
import com.bjpowernode.domain.User;
import com.bjpowernode.service.PSmsService;
import com.bjpowernode.service.UserService;
import com.bjpowernode.utils.SmsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PSmsServiceImpl implements PSmsService {
    private final SmsUtils smsUtils;
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;

    @Override
    public boolean sendSms(PhoneAndCode phoneAndCode) throws ExecutionException, InterruptedException {
        int codeInt = new Random().nextInt(9000) + 1000;
        String code = Integer.toString(codeInt);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("phoneNumbers:" + phoneAndCode.getPhonenum()+":", code, 30, TimeUnit.MINUTES);
        return smsUtils.sendSms(phoneAndCode.getPhonenum(), code);
    }

    @Override
    public boolean savePhoneNum(PhoneAndCode phoneAndCode) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String indexCode = operations.get("phoneNumbers:" + phoneAndCode.getPhonenum()+":");
        if (phoneAndCode.getCode().equals(indexCode)) {
            User user = userService.getById(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setUserMobile(phoneAndCode.getPhonenum());
            return userService.updateById(user);
        }
        return false;
    }
}
