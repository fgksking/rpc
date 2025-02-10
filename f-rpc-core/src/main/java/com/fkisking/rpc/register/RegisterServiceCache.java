package com.fkisking.rpc.register;

import com.fkisking.rpc.model.ServiceMetaInfo;

import java.util.List;

public class RegisterServiceCache {

    private List<ServiceMetaInfo> serviceMetaInfos;

    public List<ServiceMetaInfo> readCache(){
        return this.serviceMetaInfos;
    }

    public void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceMetaInfos = newServiceCache;
    }

    public void clearAll(){
        this.serviceMetaInfos = null;
    }


}
