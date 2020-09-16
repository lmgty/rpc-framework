package com.yufa.xz.ly.serialize;

/**
 * 序列化接口，所有序列化类都要实现这个接口
 * @author LiuYe
 * @data 2020/9/16
 */
public interface Serializer {

    /**
     * 序列化
     * @param object 需要被序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类
     * @param <T> 目标类的类型。
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
