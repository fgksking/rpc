package com.fkisking.rpc.blance;

import com.fkisking.rpc.constant.LoadBalancerKeys;

public class BalanceFactory {

    private static LoadBalance loadBalancer;
    public static LoadBalance getBalance(String balancer){
        if(loadBalancer !=null){
            return loadBalancer;
        }
        if (LoadBalancerKeys.RANDOM.equals(balancer)){
            synchronized (BalanceFactory.class) {
                if(loadBalancer ==null) {
                    loadBalancer = new RandomBalance();
                }
            }
        }
        return loadBalancer;
    }
}
