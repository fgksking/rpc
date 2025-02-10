package com.fkisking.rpc.serlizera;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fkisking.rpc.model.rpcRequest;
import com.fkisking.rpc.model.rpcResponse;

import java.util.LinkedHashMap;

/**
 * 由于json是把对象转为字符串传输，当反序列化时失去了字节转对象的信息
 * 所以反序列化时需要 告知对象类的信息
 */
public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object o) {
        byte[] jsonBytes = JSONObject.toJSONBytes(o);
        return jsonBytes;
    }

    @Override
    public Object deserialize(byte[] data, int type) {
        Object obj = null;
        switch (type){
            case 0:
                rpcRequest request = JSON.parseObject(data,rpcRequest.class);
                //把里面的 params读出
                System.out.println(request);
                Object[] objects =new Object[request.getParams().length];
                for (int i = 0; i <objects.length ; i++) {
                    Class<?> paramType = request.getParamsTypes()[i];
                    if (!paramType.isAssignableFrom(request.getParams()[i].getClass())){
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i],request.getParamsTypes()[i]);
                    }else{
                        objects[i] = request.getParams()[i];
                    }
                }
                obj = request;
                break;
            case 1:
                rpcResponse response =JSON.parseObject(data,rpcResponse.class);
                Class<?> dataType = response.getObjectType();
                if(! dataType.isAssignableFrom(response.getObject().getClass())){
                    response.setObject(JSONObject.toJavaObject((JSONObject) response.getObject(),dataType));
                }
                obj = response;
                break;
            default:
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getSerialize() {
        return 1;
    }
}
