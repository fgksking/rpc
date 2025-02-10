package com.fkisking.rpc.server.version3;

import com.fkisking.rpc.server.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleSever implements Server{

    private ServiceProvider serviceProvider;
    private final ThreadPoolExecutor threadPool;
    @Override
    public void start(int port) {
        //
        try {
            ServerSocket SeverSocket = new ServerSocket(8899);
            while (true) {
                Socket socket = SeverSocket.accept();
                //传入socket,以及severProvider,创建任务，丢给线程池去调用
                threadPool.execute(new WorkThread(serviceProvider,socket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SimpleSever(ServiceProvider severProvider) {
        this.serviceProvider = severProvider;
        threadPool = new ThreadPoolExecutor(10,
                15, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void stop() {

    }
}
