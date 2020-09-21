package com.yufa.xz.ly.registery;

import java.net.InetSocketAddress;

/**
 *
 * @author LiuYe
 * @data 2020/9/21
 */
public interface ServiceRegistry {
    /**
     * register service
     * @param serviceName service name
     * @param inetSocketAddress service address
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
