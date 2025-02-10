package com.fksiking.rpc.balance;

import com.fkisking.rpc.blance.BalanceFactory;
import com.fkisking.rpc.blance.LoadBalance;
import com.fkisking.rpc.constant.LoadBalancerKeys;
import com.fkisking.rpc.model.ServiceMetaInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RandomTest {
    @Test
    public void test1(){
        LoadBalance loadBalance = BalanceFactory.getBalance(LoadBalancerKeys.RANDOM);
        List<ServiceMetaInfo> list = new ArrayList<>();
        ServiceMetaInfo s1 =new ServiceMetaInfo();
        s1.setServiceName("zhan");
        ServiceMetaInfo s2 = new ServiceMetaInfo();
        s2.setServiceName("wp");
        list.add(s1);
        list.add(s2);
        ServiceMetaInfo random = loadBalance.getByRandom(list);
        System.out.println(random);
    }
}
