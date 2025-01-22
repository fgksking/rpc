package com.fkisking.version2;

import com.fkisking.Blog;
import com.fkisking.User;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.proxy.ClientProxy;
import com.fkisking.sevice.BlogService;
import com.fkisking.sevice.UserService;

import java.lang.reflect.Proxy;

public class consumerExample {

    public static void main(String[] args) {
        ClientProxy proxy = new ClientProxy("127.0.0.1",8899);
        UserService userService = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserService.class}, proxy);
        User user = userService.getUser("zhan");
        System.out.println("客户端得到的user"+ user);
        BlogService blogService = (BlogService) Proxy.newProxyInstance(BlogService.class.getClassLoader()
        ,new Class[]{BlogService.class},proxy);
        Blog blog = blogService.getById(2);
        System.out.println("客户端得到的"+blog);
    }
}
