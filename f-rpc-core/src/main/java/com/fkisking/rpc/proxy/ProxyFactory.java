package com.fkisking.rpc.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T getProxyClass(Class<T> c){
        return (T) Proxy.newProxyInstance(c.getClassLoader(),new Class[]{c},new ServiceProxy());
    }
}
