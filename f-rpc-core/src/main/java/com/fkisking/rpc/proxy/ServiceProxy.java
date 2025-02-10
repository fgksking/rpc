package com.fkisking.rpc.proxy;

import com.fkisking.rpc.RpcApplication;
import com.fkisking.rpc.client.NettyClient;
import com.fkisking.rpc.client.RPCClient;
import com.fkisking.rpc.config.RpcConfig;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {

    private RPCClient rpcClient;

    public ServiceProxy(){
        //拿到
        rpcClient = new NettyClient();
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method);
        rpcRequest r = rpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args)
                .paramsTypes(method.getParameterTypes()).build();
        System.out.println("invoke的request"+r);
        rpcResponse client = rpcClient.sendRequest(r);
        return client.getObject();
    }

}
