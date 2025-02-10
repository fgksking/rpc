package com.fkisking.version5;

import com.fkisking.User;
import com.fkisking.rpc.bootstart.ConsumerBootstart;
import com.fkisking.rpc.proxy.ProxyFactory;
import com.fkisking.sevice.UserService;

public class consumer {
    public static void main(String[] args) {
        ConsumerBootstart.init();
        UserService userService = ProxyFactory.getProxyClass(UserService.class);
        User zhan = userService.getUser("zhan");
        System.out.println(zhan);
    }
}
