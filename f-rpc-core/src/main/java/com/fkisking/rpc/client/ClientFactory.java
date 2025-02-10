package com.fkisking.rpc.client;

public class ClientFactory {

    private static RPCClient rpcClient;

    public static RPCClient getProxyClient(){
        return rpcClient;
    }
}
