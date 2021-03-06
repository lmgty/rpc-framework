package com.yufa.xz.ly.remoting.transport.netty.client;

import com.yufa.xz.ly.factory.SingletonFactory;
import com.yufa.xz.ly.registery.ServiceDiscovery;
import com.yufa.xz.ly.registery.zk.ZkServiceDiscovery;
import com.yufa.xz.ly.remoting.dto.RpcRequest;
import com.yufa.xz.ly.remoting.dto.RpcResponse;
import com.yufa.xz.ly.remoting.transport.ClientTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {
    private final ServiceDiscovery serviceDiscovery;
    private ChannelProvider channelProvider;
    private UnprocessedRequests unprocessedRequests;

    public NettyClientTransport() {
        this.serviceDiscovery = new ZkServiceDiscovery();
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest) {
        // 表示未来结果的 "占位符" completableFuture
        CompletableFuture<RpcResponse<Object>> completableFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        // get the URL by rpcServiceName
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookUpService(rpcServiceName);
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()){
            // put unprocessed request
            unprocessedRequests.put(rpcRequest.getRequestId(), completableFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()){
                    log.info("client send message: [{}]", rpcRequest);
                    unprocessedRequests.put(rpcRequest.getRequestId(), completableFuture);
                } else {
                    future.channel().close();
                }
            });
        }
        return completableFuture;
    }
}
