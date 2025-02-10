package com.fkisking.rpc.blance;

import com.fkisking.rpc.model.ServiceMetaInfo;

import java.util.List;

public interface LoadBalance {

    ServiceMetaInfo getByRandom(List<ServiceMetaInfo> serviceMetaInfoList);
}
