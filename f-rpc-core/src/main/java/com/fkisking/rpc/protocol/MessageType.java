package com.fkisking.rpc.protocol;

public enum MessageType {
    REQUEST(0),
    RESPONSE(1);

    private final int key;

    MessageType(int key) {
        this.key = key;
    }
    public static MessageType getEnumByKey(int key){
        for (MessageType value : MessageType.values()) {
            if(value.key==key){
                return value;
            }
        }
        return null;
    }
}
