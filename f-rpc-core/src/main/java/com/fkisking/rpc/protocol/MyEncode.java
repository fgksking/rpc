package com.fkisking.rpc.protocol;

import com.fkisking.rpc.serlizera.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyEncode<T> extends MessageToByteEncoder<MessageProtocol<T>> {
    private Serializer serializer;
    //序列化的过程
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol<T> messageProtocol, ByteBuf byteBuf) throws Exception {
        MessageProtocol.Header header = messageProtocol.getHeader();
        System.out.println("正在进行编码encode");
        //写入魔数
        byteBuf.writeByte(header.getMagic());
        //写入版本号
        byteBuf.writeByte(header.getVersion());
        //写入序列化方式
        byteBuf.writeByte(header.getSerializer());
        //写入状态
        byteBuf.writeByte(header.getStatus());
        //写入类型
        byteBuf.writeByte(header.getType());
        //写入请求ID
        byteBuf.writeLong(header.getRequestId());
        //写入消息体长度
        //写入消息内容Body
        byte[] serialize = serializer.serialize(messageProtocol.getBody());
        byteBuf.writeInt(serialize.length);
        byteBuf.writeBytes(serialize);
    }
}
