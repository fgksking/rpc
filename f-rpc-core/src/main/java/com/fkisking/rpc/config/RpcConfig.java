package com.fkisking.rpc.config;

import com.fkisking.rpc.constant.LoadBalancerKeys;
import com.fkisking.rpc.constant.rpcConstants;
import lombok.Data;

/**
 * rpc 全局配置
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "f-rpc";

    /**
     * 版本号
     */
    private String version = rpcConstants.RPCConfig_VERSION;

    /**
     * 服务器主机
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口
     */
    private int serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private int serializer = 1;

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.RANDOM;

/*    *//**
     * 重试策略
     *//*
    private String retryStrategy = RetryStrategyKeys.NO;

    *//**
     * 容错策略
     *//*
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;*/


    /**
     *    注册中心配置
     */
    private RegistryConfig registry = new RegistryConfig();

}
