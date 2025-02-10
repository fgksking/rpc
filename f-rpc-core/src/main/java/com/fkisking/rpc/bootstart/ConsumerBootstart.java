package com.fkisking.rpc.bootstart;

import com.fkisking.rpc.RpcApplication;

public class ConsumerBootstart {

    private ConsumerBootstart(){

    }

    public static void init(){
        RpcApplication.init();
        System.out.println("消费端启动了");
    }
}
