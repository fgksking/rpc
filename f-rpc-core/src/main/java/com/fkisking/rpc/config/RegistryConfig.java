package com.fkisking.rpc.config;

import com.fkisking.rpc.register.RegistryKeys;
import lombok.Data;

/**
 * 服务service模块
 */
@Data
public class RegistryConfig {
    //多注册中心的id关联
    private Integer id;
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ZK;

    /**
     * 注册中心地址
     */
    private String address = "192.168.220.129:2181";

    private String RegisterHost ="192.168.220.129";

    private int port = 2181;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
