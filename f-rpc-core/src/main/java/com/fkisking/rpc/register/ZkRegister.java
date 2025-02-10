package com.fkisking.rpc.register;

import com.fkisking.rpc.config.RegistryConfig;
import com.fkisking.rpc.model.ServiceConfig;
import com.fkisking.rpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Slf4j
public class ZkRegister implements Register{

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;

    private AtomicInteger serviceInstanceId = new AtomicInteger(0);

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();
    /**
     * 服务缓存
     */
    private final RegisterServiceCache serviceCache = new RegisterServiceCache();

    private final String ZK_ROOT_PATH = "/rpc/zk";
    @Override
    public void init(RegistryConfig registryConfig) {
        // 构建 client 实例
        client = CuratorFrameworkFactory
                .builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()), 3))
                .build();

        // 构建 serviceDiscovery 实例
        System.out.println("注册中心地址："+registryConfig.getAddress());
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();
        try {
            System.out.println("注册中心初始化");
            // 启动 client 和 serviceDiscovery
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 注册到 zk 里
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));
        // 添加节点信息到本地缓存
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 从本地缓存移除
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.remove(registerKey);
    }
    //TODO 这里service的ID要唯一，看是否能是实现
    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();
        try {
            serviceInstanceId.getAndIncrement();
            return ServiceInstance
                    .<ServiceMetaInfo>builder()
                    .id(serviceAddress + serviceInstanceId)
                    .name(serviceMetaInfo.getServiceKey())
                    .address(serviceAddress)
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  服务发现
     * @param serviceKey   服务的Name -> serviceName
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceKey) {
        //先从缓存中查询
        List<ServiceMetaInfo> serviceMetaInfos = serviceCache.readCache();
        if(serviceMetaInfos != null){
            return serviceMetaInfos;
        }
        try {
            if(serviceDiscovery ==null){
                throw new RuntimeException("服务发现者为空");
            }
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceKey);
            List<ServiceMetaInfo> serviceMetaInfoList = serviceInstances.stream().map((instance) -> instance.getPayload())
                    .collect(Collectors.toList());
            serviceCache.writeCache(serviceMetaInfoList);
            return  serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void watch(String serviceNodeKey) {
        String watchKey = ZK_ROOT_PATH + "/" + serviceNodeKey;
        /*boolean newWatch = watchingKeySet.add(watchKey);
        if (newWatch) {
            CuratorCache curatorCache = CuratorCache.build(client, watchKey);
            curatorCache.start();
            curatorCache.listenable().addListener(
                    CuratorCacheListener
                            .builder()
                            .forDeletes(childData -> registryServiceCache.clearCache())
                            .forChanges(((oldNode, node) -> registryServiceCache.clearCache()))
                            .build()
            );
        }*/
    }

    @Override
    public void destroy() {
        log.info("当前节点下线");
        // 下线节点（这一步可以不做，因为都是临时节点，服务下线，自然就被删掉了）
        for (String key : localRegisterNodeKeySet) {
            try {
                log.info("删除节点 = " +key);
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }

        // 释放资源
        if (client != null) {
            client.close();
        }
    }
}
