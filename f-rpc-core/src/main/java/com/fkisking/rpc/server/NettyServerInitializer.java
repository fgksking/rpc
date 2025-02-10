package com.fkisking.rpc.server;

import com.fkisking.rpc.protocol.MyDecode;
import com.fkisking.rpc.protocol.MyEncode;
import com.fkisking.rpc.serlizera.JsonSerializer;
import com.fkisking.rpc.server.NettyRPCServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

/**
 * 初始化，负责序列化的编码解码，需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode<>(new JsonSerializer()));
        pipeline.addLast(new NettyRPCServerHandler());

    }

}
