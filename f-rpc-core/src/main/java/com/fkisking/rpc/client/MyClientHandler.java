package com.fkisking.rpc.client;

import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@NoArgsConstructor
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol<rpcResponse>> {

    private  CompletableFuture<rpcResponse> responseFuture;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<rpcResponse> rPMsg) throws Exception {
       /* AttributeKey<rpcResponse> key = AttributeKey.valueOf("RPCResponse");
        channelHandlerContext.channel().attr(key).set(rPMsg.getBody());*/
        System.out.println("channelRead0"+rPMsg.getBody());
        responseFuture.complete(rPMsg.getBody());
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
