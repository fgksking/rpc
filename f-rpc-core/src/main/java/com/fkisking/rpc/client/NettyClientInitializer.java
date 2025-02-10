package com.fkisking.rpc.client;


import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MyDecode;
import com.fkisking.rpc.protocol.MyEncode;
import com.fkisking.rpc.serlizera.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private CompletableFuture<rpcResponse> responseFuture;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new MyEncode<>(new JsonSerializer()));
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyClientHandler(responseFuture));
    }
}
