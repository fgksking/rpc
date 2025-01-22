package com.fkisking.rpc.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class rpcResponse implements Serializable {
    private int code;
    private String message;
    private Object object;


    public static rpcResponse success(Object object){
        return rpcResponse.builder().code(200).object(object).build();
    }

}
