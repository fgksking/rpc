package com.fkisking.rpc.proxy;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.proxy.version1.IOClient;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//proxy代理的核心类
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private  String host;
    private int port;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method);
        rpcRequest  r = rpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args)
                .paramsTypes(method.getParameterTypes()).build();
        rpcResponse client = IOClient.client(host, port, r);
        return client.getObject();
    }
    public <T> T getProxyClass(Class<T> c){
        return (T)Proxy.newProxyInstance(c.getClassLoader(),new Class[]{c.getClass()},this);
    }
}
