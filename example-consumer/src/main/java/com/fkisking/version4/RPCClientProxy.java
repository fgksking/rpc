package com.fkisking.version4;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    private RPCClient rpcClient;

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

    public <T> T getProxyClass(Class<T> c){
        return (T) Proxy.newProxyInstance(c.getClassLoader(),new Class[]{c},this);
    }
}
