package com.fkisking.version3;

import com.fkisking.BlogServiceImpl;
import com.fkisking.UserServiceProxy;

import java.util.HashMap;
import java.util.Map;

public class providerExample {

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("com.fkisking.sevice.UserService",new UserServiceProxy());
        map.put("com.fkisking.sevice.BlogService",new BlogServiceImpl());
        Server server = new SimpleSever(map);
        server.start(8899);
    }
}
