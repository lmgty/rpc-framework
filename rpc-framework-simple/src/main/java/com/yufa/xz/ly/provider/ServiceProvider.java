package com.yufa.xz.ly.provider;

import com.yufa.xz.ly.entity.RpcServiceProperties;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
public interface ServiceProvider {
    /**
     * addService to map
     * @param service              service object
     * @param serviceClass         the interface class implemented by the service instance object
     * @param rpcServiceProperties service related attributes
     */
    void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * getService from map
     * @param rpcServiceProperties service related attributes
     * @return service object
     */
    Object getService(RpcServiceProperties rpcServiceProperties);

    /**
     * publishService to registry
     * @param service              service object
     * @param rpcServiceProperties service related attributes
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * publishService to registry
     * @param service service object
     */
    void publishService(Object service);

}
