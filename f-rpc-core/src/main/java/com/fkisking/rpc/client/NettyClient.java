package com.fkisking.rpc.client;

import cn.hutool.core.collection.CollUtil;
import com.fkisking.rpc.RpcApplication;
import com.fkisking.rpc.blance.BalanceFactory;
import com.fkisking.rpc.blance.LoadBalance;
import com.fkisking.rpc.blance.RandomBalance;
import com.fkisking.rpc.config.RegistryConfig;
import com.fkisking.rpc.config.RpcConfig;
import com.fkisking.rpc.constant.rpcConstants;
import com.fkisking.rpc.model.ServiceMetaInfo;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import com.fkisking.rpc.protocol.ProtocolConstant;
import com.fkisking.rpc.register.Register;
import com.fkisking.rpc.register.RegistryFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor
public class NettyClient implements RPCClient{
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private static final CompletableFuture<rpcResponse> responseFuture = new CompletableFuture<>();

    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer(responseFuture));
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回， 而不是想要的相应的response
     */
    @Override
    public rpcResponse sendRequest(rpcRequest request) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registry = rpcConfig.getRegistry();
        Register instance = RegistryFactory.getInstance(registry.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(request.getInterfaceName());
        serviceMetaInfo.setServiceVersion(rpcConstants.RPCConfig_VERSION);
        System.out.println("发现的serviceKey ="+ serviceMetaInfo.getServiceKey());
        List<ServiceMetaInfo> serviceMetaInfos = instance.serviceDiscover(serviceMetaInfo.getServiceKey());
        if(CollUtil.isEmpty(serviceMetaInfos)){
            throw new RuntimeException("没有此服务");
        }
        System.out.println("所有服务实例 ="+serviceMetaInfos);
        //负载均衡
        LoadBalance loadBalance = BalanceFactory.getBalance(rpcConfig.getLoadBalancer());
        ServiceMetaInfo random = loadBalance.getByRandom(serviceMetaInfos);
        String host = random.getServiceHost().toString();
        int port = random.getServicePort();
        //容错
      //  CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        //重试
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
            //AttributeKey<rpcResponse> key = AttributeKey.valueOf("RPCResponse");
            //rpcResponse response = channel.attr(key).get();
            rpcResponse response = null;
            try {
                System.out.println("等待响应");
                response = responseFuture.get(5000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
               throw new RuntimeException("获取响应超时",e);
            }
            System.out.println("NettyClient回调的数据");
            System.out.println(response);

            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
