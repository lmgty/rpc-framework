package com.yufa.xz.ly.registery;

import java.net.InetSocketAddress;

/**
 * @author LiuYe
 * @data 2020/9/21
 */
public interface ServiceDiscovery {

    /**
     * lookup service by rpcServiceName
     * @param rpcServiceName service name
     * @return service address
     */
    InetSocketAddress lookUpService(String rpcServiceName);
}
