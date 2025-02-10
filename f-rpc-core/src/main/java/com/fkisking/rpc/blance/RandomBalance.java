package com.fkisking.rpc.blance;

import com.fkisking.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;

public class RandomBalance implements LoadBalance{
    @Override
    public ServiceMetaInfo getByRandom(List<ServiceMetaInfo> serviceMetaInfoList) {
        Random random = new Random();
        return serviceMetaInfoList.get(random.nextInt(serviceMetaInfoList.size()));
    }
}
