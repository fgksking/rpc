package com.fkisking.rpc.server;

import cn.hutool.core.bean.BeanUtil;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;
import com.fkisking.rpc.protocol.MessageProtocol;
import com.fkisking.rpc.protocol.ProtocolConstant;
import com.fkisking.rpc.register.LocalRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
@Slf4j
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<MessageProtocol<rpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol<rpcRequest> rpcMessage) throws Exception {
        //这里开一个线程池去处理这些任务
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
        log.info("调用的接口名"+interfaceName);
        System.out.println(request);
        // 注册缓存中获取对应的实现类
        Class<?> service = LocalRegister.read(interfaceName);
        System.out.println("serviceName "+interfaceName);
        System.out.println("service =" + service);
       /* if(service instanceof Object[]){
            System.out.println("service类型是Object数组");
            Object[] services = (Object[])service;
            service = services[0];
        }*/
        Object instance = null;
        try {
            Constructor<?> constructor = service.getDeclaredConstructor();
            instance = constructor.newInstance();
            System.out.println(instance.getClass().getName());
        } catch (Exception e) {
          throw new RuntimeException("创建代理类失败",e);
        }
        Method method =null;
        try {

            method = instance.getClass().getMethod(request.getMethodName(),request.getParamsTypes());
            Object invoke = method.invoke(instance, request.getParams());
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
