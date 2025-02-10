package com.fkisking.version4;

import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import com.fkisking.rpc.protocol.ProtocolConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class NettyClient implements RPCClient{

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回， 而不是想要的相应的response
     */
    @Override
    public rpcResponse sendRequest(rpcRequest request) {
        try {
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setBody(request);
            MessageProtocol.Header header = new MessageProtocol.Header();
            header.setRequestId(1L);
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer(ProtocolConstant.PROTOCOL_JSON);
            header.setType(ProtocolConstant.PROTOCOL_TYPE_REQUEST);
            header.setStatus(ProtocolConstant.PROTOCOL_STATUS_);
            messageProtocol.setHeader(header);
            // 发送数据
            System.out.println("sendRequest 发送数据");
            channel.writeAndFlush(messageProtocol);
            // 等待响应
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            // 等待响应
            AttributeKey<rpcResponse> key = AttributeKey.valueOf("RPCResponse");
            rpcResponse response = channel.attr(key).get();
            System.out.println("NettyClient回调的数据");
            System.out.println(response);

            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
