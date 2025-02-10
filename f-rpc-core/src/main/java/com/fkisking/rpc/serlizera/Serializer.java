package com.fkisking.rpc.serlizera;

public interface Serializer {
    byte[] serialize(Object o);

    /**
     *
     * @param data
     * @param type  0 是 rpcRequest  1是 rpcResponse
     * @return
     */
    Object deserialize(byte[] data,int type);

    int getSerialize();

    /**
     *
     * @param code 0是 jdk序列化 1是fastJson
     * @return
     */
    static Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
