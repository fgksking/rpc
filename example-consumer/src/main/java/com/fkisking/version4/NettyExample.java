package com.fkisking.version4;

import com.fkisking.User;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.sevice.UserService;

public class NettyExample {
    public static void main(String[] args) {
        RPCClient rpcClient = new NettyClient("127.0.0.1",8899);
        RPCClientProxy clientProxy = new RPCClientProxy(rpcClient);
        UserService userService = clientProxy.getProxyClass(UserService.class);
        User user = userService.getUser("zhan");
        System.out.println("user  "+user);

    }
}
