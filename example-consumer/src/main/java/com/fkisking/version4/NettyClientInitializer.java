package com.fkisking.version4;


import com.fkisking.rpc.protocol.MyDecode;
import com.fkisking.rpc.protocol.MyEncode;
import com.fkisking.rpc.serlizera.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyClientInitializer extends ChannelInitializer<io.netty.channel.socket.SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new MyEncode<>(new JsonSerializer()));
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyClientHandler());
    }
}
