package com.fkisking.version3;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;

public interface RpcClient {

    rpcResponse sendRequest(rpcRequest r);
}
