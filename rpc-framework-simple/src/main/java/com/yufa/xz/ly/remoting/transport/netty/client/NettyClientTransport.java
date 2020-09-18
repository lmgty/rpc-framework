package com.yufa.xz.ly.remoting.transport.netty.client;

import com.yufa.xz.ly.factory.SingletonFactory;
import com.yufa.xz.ly.remoting.dto.RpcRequest;
import com.yufa.xz.ly.remoting.transport.ClientTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {
    private ChannelProvider channelProvider;

    public NettyClientTransport() {
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9998);
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()){
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()){
                    log.info("client send message: [{}]", rpcRequest);
                } else {
                    future.channel().close();
                }
            });
        }


        return null;
    }
}
