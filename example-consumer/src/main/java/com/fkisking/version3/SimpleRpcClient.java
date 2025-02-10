package com.fkisking.version3;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@AllArgsConstructor
public class SimpleRpcClient implements RpcClient{
    private String host;
    private int port;

    @Override
    public rpcResponse sendRequest(rpcRequest request) {
        try {
            // 发起一次Socket连接请求
            Socket socket = new Socket(host, port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            rpcResponse response = (rpcResponse) objectInputStream.readObject();

            //System.out.println(response.getData());
            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }
}
