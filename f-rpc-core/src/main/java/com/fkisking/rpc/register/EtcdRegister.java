package com.fkisking.rpc.register;

import cn.hutool.json.JSONUtil;
import com.fkisking.rpc.config.RegistryConfig;
import com.fkisking.rpc.model.ServiceConfig;
import com.fkisking.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class EtcdRegister implements Register{
    private Client client;
    private KV kvClient;
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscover(String serviceKey) {
          return null;
    }

    @Override
    public void watch(String serviceNodeKey) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception{
        Lease leaseClient = client.getLeaseClient();
        //创建租约
        long leaseId = 0;

        leaseId = leaseClient.grant(30).get().getID();

        String registerKey = serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {

    }
}
