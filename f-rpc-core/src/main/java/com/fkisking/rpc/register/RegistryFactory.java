package com.fkisking.rpc.register;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取注册中心
 */
@Slf4j
public class RegistryFactory {
    //缓存注册中心，可注册多个中心
    private static List<Register> register = new ArrayList<>();

    /**
     * 创建新注册中心
     * @param registerName
     * @return
     */
    public static Register getInstance(String registerName){
        if(! register.isEmpty()){
            return register.get(0);
        }
        if(RegistryKeys.ETCD.equals(registerName)){
            return new EtcdRegister();
        } else if (RegistryKeys.ZK.equals(registerName)) {
            ZkRegister zkRegister = new ZkRegister();
            addRegister(zkRegister);
            return zkRegister;
        }
        log.error("注册中心初始化错误");
        throw new RuntimeException("注册中心初始化错误");
    }
    public static void addRegister(Register register1){
        register.add(register1);
    }
    /**
     * 获取注册中心
     * @return
     */
    public static Register getRegister(){
        return register.get(0);
    }

}
