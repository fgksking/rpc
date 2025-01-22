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

public class providerExample {
    public static void main(String[] args) throws IOException {
        UserServiceProxy userService = new UserServiceProxy();
        ServerSocket serverSocket = new ServerSocket(8899);
        System.out.println("服务启动了");
        while (true){

            Socket socket = serverSocket.accept();
            new Thread(()->{
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    rpcRequest rpcRequest = (rpcRequest) ois.readObject();
                    Method method = userService.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamsTypes());
                    Object invoke = method.invoke(userService, rpcRequest.getParams());
                    oos.writeObject(rpcResponse.success(invoke));
                    oos.flush();
                } catch (IOException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }

    }
}