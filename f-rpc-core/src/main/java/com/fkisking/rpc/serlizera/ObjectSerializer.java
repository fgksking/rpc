package com.fkisking.rpc.serlizera;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer implements Serializer{
    // java 对象 -> 字节数组
    @Override
    public byte[] serialize(Object o) {
        byte[] bytes =null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] byteArray = new byte[0];
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            byteArray = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArray;
    }

    @Override
    public Object deserialize(byte[] data, int type) {
        Object o =null;
        ByteInputStream bis = new ByteInputStream();
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            o = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    @Override
    public int getSerialize() {
        return 0;
    }
}
