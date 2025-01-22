package com.fkisking.rpc.proxy;

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

        return null;
    }

    public <T> T getProxyClass(Class<T> c){
        return (T)Proxy.newProxyInstance(c.getClassLoader(),new Class[]{c.getClass()},this);
    }
}
