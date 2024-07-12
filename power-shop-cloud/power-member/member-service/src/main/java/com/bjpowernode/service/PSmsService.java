package com.bjpowernode.service;

import com.bjpowernode.domain.PhoneAndCode;

import java.util.concurrent.ExecutionException;

public interface PSmsService {
    public boolean savePhoneNum(PhoneAndCode phoneAndCode);

    public boolean sendSms(PhoneAndCode phoneAndCode) throws ExecutionException, InterruptedException;
}
