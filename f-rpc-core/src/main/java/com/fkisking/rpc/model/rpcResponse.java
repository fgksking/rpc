package com.fkisking.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class rpcResponse implements Serializable {
    private int code;
    private String message;
    private Object object;
    private Class<?> objectType;


    public static rpcResponse success(Object object){
        return rpcResponse.builder().code(200).object(object).build();
    }

}
