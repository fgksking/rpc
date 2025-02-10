package com.fkisking.rpc.protocol;

import com.fkisking.rpc.serlizera.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MyDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessageProtocol.Header header = new MessageProtocol.Header();
        byte magic = byteBuf.readByte();
        if (magic != ProtocolConstant.PROTOCOL_MAGIC){
            throw new Exception("Invalid magic!" + magic);
        }
        System.out.println("正在进行decode");
        header.setMagic(magic);
        header.setVersion(byteBuf.readByte());
        header.setSerializer(byteBuf.readByte());
        header.setStatus(byteBuf.readByte());
        header.setType(byteBuf.readByte());
        header.setRequestId(byteBuf.readLong());
        int dataLength = byteBuf.readInt();
        header.setBodyLength(dataLength);
        if(byteBuf.readableBytes()<dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bodyBytes = new byte[dataLength];
        byteBuf.readBytes(bodyBytes);
        byte type = header.getType();
        MessageType MsgType = MessageType.getEnumByKey(type);
        if(MsgType==null){
            System.out.println("消息的Type为空");
            return;
        }
        Serializer serializer = Serializer.getSerializerByCode(header.getSerializer());
        if(serializer==null){
            throw new RuntimeException("序列化器不存在");
        }
        //TODO 这里传入Body,并未给Handler传入完整的消息协议实体
        Object deserialize = serializer.deserialize(bodyBytes, header.getType());
        MessageProtocol messageProtocol = new MessageProtocol<>();
        messageProtocol.setHeader(header);
        messageProtocol.setBody(deserialize);
        list.add(messageProtocol);

    }
}
