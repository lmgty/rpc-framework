package com.yufa.xz.ly.remoting.transport.netty.client;

import com.yufa.xz.ly.factory.SingletonFactory;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> channelMap;
    private final NettyClient nettyClient;

    public ChannelProvider() {
        this.channelMap = new ConcurrentHashMap<>();
        this.nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    public Channel get(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()){
                return channel;
            }else {
                channelMap.remove(key);
            }
        }
        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size : [{}]", channelMap.size());
    }
}
