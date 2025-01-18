package com.fkisking.rpc.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class rpcResponse implements Serializable {
    private int code;
    private String message;
    private Object object;
}
