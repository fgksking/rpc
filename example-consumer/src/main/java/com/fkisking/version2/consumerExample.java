package com.fkisking.version2;

import com.fkisking.rpc.proxy.ClientProxy;
import com.fkisking.sevice.UserService;

import java.lang.reflect.Proxy;

public class consumerExample {

    public static void main(String[] args) {
        ClientProxy proxy = new ClientProxy("127:0:0:1",8877);
        UserService userService = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserService.class}, proxy);
        userService.getUser("zhan");
    }
}
