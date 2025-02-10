package com.fkisking.version5;

import com.fkisking.UserServiceProxy;
import com.fkisking.rpc.bootstart.ProviderBootstart;
import com.fkisking.rpc.model.ServiceConfig;
import com.fkisking.sevice.UserService;

import java.util.ArrayList;
import java.util.List;

public class serverExample {

    public static void main(String[] args) {
        List<ServiceConfig<?>> list= new ArrayList<>();
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setServiceName(UserService.class.getName());
        serviceConfig.setImplClass(UserServiceProxy.class);
        list.add(serviceConfig);
        ProviderBootstart.init(list);
    }
}
