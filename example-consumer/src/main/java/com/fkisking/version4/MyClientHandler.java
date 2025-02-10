package com.fkisking.version4;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;
@AllArgsConstructor
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol<rpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<rpcResponse> rPMsg) throws Exception {
        AttributeKey<rpcResponse> key = AttributeKey.valueOf("RPCResponse");
        channelHandlerContext.channel().attr(key).set(rPMsg.getBody());
        System.out.println("channelRead0"+rPMsg.getBody());
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
