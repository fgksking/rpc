package com.fkisking;

import com.fkisking.sevice.UserService;

public class UserServiceProxy implements UserService {


    @Override
    public User getUser(String name) {
        // 通过 构造所约定的请求结构，序列化后，发送http请求，等待响应
        return null;
    }
}
