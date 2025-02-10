package com.fkisking.rpc.protocol;

import cn.hutool.http.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageProtocol<T> {
    private Header header;
    private T body;
    @Data
    public static class Header{
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 协议版本
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 消息类型 - 请求/响应
         */
        private byte type;


        /**
         * 状态
         */
        private byte status;

        /**
         * 请求 ID
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}
