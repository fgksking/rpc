package com.fkisking.rpc.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    //存放 接口名 -> key、实现类
    private Map<String,Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";


    /**
     * 对象实例缓存 避免重放new 类路径=> 对象实例 ，单例模式
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();
}
