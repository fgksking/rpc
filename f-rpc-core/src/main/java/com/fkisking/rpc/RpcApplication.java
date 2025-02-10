package com.fkisking.rpc;

import com.fkisking.rpc.config.RpcConfig;
import com.fkisking.rpc.constant.rpcConstants;
import com.fkisking.rpc.register.Register;
import com.fkisking.rpc.register.RegistryFactory;
import com.fkisking.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig loadConfig){
        log.info("rpcApplication init");
        //全局配置已经初始化完成了
        //初始化里面的注册中心配置
        String registry = loadConfig.getRegistry().getRegistry();
        Register instance = RegistryFactory.getInstance(registry);
        rpcConfig = loadConfig;
        instance.init(loadConfig.getRegistry());
        log.info("registry init, config = {}", loadConfig.getRegistry());
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(instance::destroy));
    }

    public static void init(){
        RpcConfig loadConfig;
        loadConfig = ConfigUtils.loadConfig(RpcConfig.class, rpcConstants.ConfigPrefix);
        init(loadConfig);
    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig ==null){
            synchronized (RpcApplication.class){
                if(rpcConfig ==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
