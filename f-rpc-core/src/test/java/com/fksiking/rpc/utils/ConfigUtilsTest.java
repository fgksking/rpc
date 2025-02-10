package com.fksiking.rpc.utils;

import com.fkisking.rpc.config.RpcConfig;
import com.fkisking.rpc.utils.ConfigUtils;
import org.junit.Test;

public class ConfigUtilsTest {
    @Test
    public void testLoad(){
        RpcConfig loadConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc.");
        System.out.println(loadConfig);
    }

}
