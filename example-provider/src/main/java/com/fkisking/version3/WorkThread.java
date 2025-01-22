package com.fkisking.version3;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class WorkThread  implements Runnable{

    private Map<String,Object> severProvider;
    private Socket socket;

    public WorkThread(Map<String, Object> severProvider, Socket socket) {
        this.severProvider = severProvider;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 读取客户端传过来的request
            rpcRequest request = (rpcRequest) ois.readObject();
            // 反射调用对应方法
            Object o = severProvider.get(request.getInterfaceName());
            Method method = o.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(o, request.getParams());
            // 封装，写入response对象
            rpcResponse success = rpcResponse.success(invoke);
            oos.writeObject(success);
            System.out.println("服务端对象"+success);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
