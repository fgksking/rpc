package com.fkisking.rpc.bootstart;

import com.fkisking.rpc.RpcApplication;
import com.fkisking.rpc.config.RegistryConfig;
import com.fkisking.rpc.config.RpcConfig;
import com.fkisking.rpc.model.ServiceConfig;
import com.fkisking.rpc.model.ServiceMetaInfo;
import com.fkisking.rpc.register.LocalRegister;
import com.fkisking.rpc.register.Register;
import com.fkisking.rpc.register.RegistryFactory;
import com.fkisking.rpc.server.NettyRpcServer;

import java.util.List;

public class ProviderBootstart {
    private static volatile ProviderBootstart instance;

    public ProviderBootstart getInstance(){
        if(instance ==null){
            synchronized (ProviderBootstart.class){
                instance = new ProviderBootstart();
            }
        }
        return instance;
    }

    public static void init(List<ServiceConfig<?>> serviceConfigList){
        // 初始化rpc配置文件
        RpcApplication.init();
        // 运行时配置文件不可变（发生更改）
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        for (ServiceConfig<?> serviceConfig : serviceConfigList) {
            RegistryConfig registry = rpcConfig.getRegistry();
            System.out.println("register ="+registry.getRegistry());
            String registerType = registry.getRegistry();
            Register register = RegistryFactory.getInstance(registerType);
            register.init(registry);
            //缓存加载 serviceName 和 实现类
            LocalRegister.load(serviceConfig.getServiceName(),serviceConfig.getImplClass());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceConfig.getServiceName());
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            System.out.println(" serviceMetaInfo =" +serviceConfig);
            try {
                register.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("服务注册失败",e);
            }
        }

        //初始化 Netty服务端
        System.out.println("初始化服务");
        NettyRpcServer server = new NettyRpcServer();
        server.start(rpcConfig.getServerPort());
    }

}
