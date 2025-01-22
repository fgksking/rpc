package com.fkisking;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class consumerExample {
    public static void main(String[] args) throws IOException {
        //调用User接口，获取User代理类，

        String host = "127:0:0:1";




    }

    public static  rpcResponse IOClient(String host,int port,rpcRequest r){
        rpcResponse rpcResponse = null;
        try {
            Socket socket = new Socket(host,port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(r);
            oos.flush();
            rpcResponse = (rpcResponse) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  rpcResponse;
    }

}