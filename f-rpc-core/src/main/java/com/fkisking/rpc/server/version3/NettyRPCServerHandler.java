package com.fkisking.rpc.server.version3;

import cn.hutool.core.bean.BeanUtil;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import com.fkisking.rpc.protocol.ProtocolConstant;
import com.fkisking.rpc.server.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<MessageProtocol<rpcRequest>> {
    private ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol<rpcRequest> rpcMessage) throws Exception {
        rpcResponse response = getResponse(rpcMessage.getBody());
        response.setObjectType(response.getObject().getClass());
        System.out.println("server得到的response是 "+response);
        MessageProtocol<rpcResponse> r = new MessageProtocol<>();
        MessageProtocol.Header h = new MessageProtocol.Header();
        BeanUtil.copyProperties(rpcMessage.getHeader(),h);
        h.setType(ProtocolConstant.PROTOCOL_TYPE_RESPONSE);
        r.setHeader(h);
        r.setBody(response);
        ctx.writeAndFlush(r);
        ctx.close();
    }

    rpcResponse getResponse(rpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        System.out.println("serviceName "+interfaceName);
        if(service instanceof Object[]){
            System.out.println("service类型是Object数组");
            Object[] services = (Object[])service;
            service = services[0];
        }
        Method method =null;
        try {
            method = service.getClass().getMethod(request.getMethodName(),request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            System.out.println("服务器执行方法  "+invoke);
            return rpcResponse.success(invoke);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
