package com.yufa.xz.ly.remoting.transport.netty.client;

import com.yufa.xz.ly.remoting.dto.RpcRequest;
import com.yufa.xz.ly.remoting.dto.RpcResponse;
import com.yufa.xz.ly.remoting.transport.netty.codec.kryo.NettyKryoDecoder;
import com.yufa.xz.ly.remoting.transport.netty.codec.kryo.NettyKryoEncoder;
import com.yufa.xz.ly.serialize.kryo.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class NettyClient {
    private final KryoSerializer kryoSerializer = new KryoSerializer();
    private final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;

    public NettyClient() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 连接成功后返回 channel
     *
     * @param inetSocketAddress 服务端地址
     * @return channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("The Client has connected [{}] successful!", inetSocketAddress.toString());
                    //  告诉completableFuture任务已经完成，需要返回的结果
                    completableFuture.complete(future.channel());
                } else {
                    throw new IllegalStateException();
                }
            }
        });
        // 获取任务结果，如果没有完成会一直阻塞等待
        return completableFuture.get();
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
