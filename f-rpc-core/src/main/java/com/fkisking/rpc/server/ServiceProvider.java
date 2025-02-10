package com.fkisking.rpc.server;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private Map<String, Object> serviceProvider;

    public ServiceProvider() {
        this.serviceProvider = new HashMap<>();
    }
    public void setServerProvider(Object server){
        String interfaceName = server.getClass().getName();
        //一个实现类可能会实现多个接口
        Class<?>[] interfaces = server.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            serviceProvider.put(anInterface.getName(),server);
        }
    }
    public Object getService(String serverName){
        return serviceProvider.get(serverName);
    }
}
