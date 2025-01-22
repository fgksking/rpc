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
                    // 读取客户端传过来的request
                    rpcRequest request = (rpcRequest) ois.readObject();
                    // 反射调用对应方法
                    Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
                    Object invoke = method.invoke(userService, request.getParams());
                    // 封装，写入response对象
                    rpcResponse success = rpcResponse.success(invoke);
                    oos.writeObject(success);
                    System.out.println("服务端对象"+success);
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