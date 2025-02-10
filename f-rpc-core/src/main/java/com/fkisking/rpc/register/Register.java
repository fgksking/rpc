package com.fkisking.rpc.register;

import com.fkisking.rpc.config.RegistryConfig;
import com.fkisking.rpc.model.ServiceConfig;
import com.fkisking.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口
 */
public interface Register {
    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);


    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    void unregister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> serviceDiscover(String serviceKey);

    void watch(String serviceNodeKey);

    void destroy();

}
