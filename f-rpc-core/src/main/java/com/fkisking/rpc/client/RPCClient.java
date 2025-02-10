package com.fkisking.rpc.client;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;

public interface RPCClient {
    rpcResponse sendRequest(rpcRequest request);


}
