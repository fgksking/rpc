package com.fkisking.rpc.register;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegister {

    private static final Map<String,Class<?>> localService = new ConcurrentHashMap<>();

    public static void load(String interfaceName,Class<?> tclass){
        System.out.println("LocalRegister 正在加载服务");
        localService.put(interfaceName,tclass);
    }

    public static Class<?> read(String interfaceName){
        Class<?> aClass = localService.get(interfaceName);
        if(aClass ==null){
            throw new RuntimeException("没有此对应的实现类");
        }
        return aClass;
    }

    public static void remove(String serviceName) {
        localService.remove(serviceName);
    }



}
