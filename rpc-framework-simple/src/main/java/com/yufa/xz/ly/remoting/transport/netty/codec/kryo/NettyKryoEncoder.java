package com.yufa.xz.ly.remoting.transport.netty.codec.kryo;

import com.yufa.xz.ly.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private final Class<?> genericClass;


    /**
     * 将对象转换成字节码然后写入到 ByteBuf 对象中
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) {
        if (genericClass.isInstance(o)) {
            byte[] body = serializer.serialize(o);
            int length = body.length;
            // 先写长度，后写内容
            byteBuf.writeInt(length);
            byteBuf.writeBytes(body);
        }
    }
}
