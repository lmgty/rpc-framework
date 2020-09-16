package com.yufa.xz.ly.remoting.transport.netty.codec.kryo;

import com.yufa.xz.ly.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
@AllArgsConstructor
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final Serializer serializer;
    private final Class<?> genericClass;

    /**
     * encoder 时存储的长度数据类型 int 的大小为4
     */
    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)  {
        if (in.readableBytes() >= BODY_LENGTH) {
            in.markReaderIndex();

            // 读取消息的长度
            int dataLength = in.readInt();
            // 不合理的情况
            if (dataLength < 0 || in.readableBytes() < 0) {
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            // 可读字节数小于消息的长度
            if (in.readableBytes() < dataLength) {
                in.resetReaderIndex();
                return;
            }

            byte[] body = new byte[dataLength];
            in.readBytes(body);

            Object object = serializer.deserialize(body, genericClass);
            out.add(object);
            log.info("successful decode ByteBuf to Object");
        }
    }
}
