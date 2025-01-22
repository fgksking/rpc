package com.fkisking.version3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleSever implements Server{

    private Map<String,Object> severProvider ;
    private final ThreadPoolExecutor threadPool;
    @Override
    public void start(int port) {
        //
        try {
            ServerSocket SeverSocket = new ServerSocket(8899);
            while (true) {
                Socket socket = SeverSocket.accept();
                //传入socket,以及severProvider,创建任务，丢给线程池去调用
                threadPool.execute(new WorkThread(severProvider,socket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SimpleSever(Map<String, Object> severProvider) {
        this.severProvider = severProvider;
        threadPool = new ThreadPoolExecutor(10,
                15, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void stop() {

    }
}
