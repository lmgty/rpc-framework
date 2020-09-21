package com.yufa.xz.ly.provider;

import com.yufa.xz.ly.entity.RpcServiceProperties;
import com.yufa.xz.ly.enumeration.RpcErrorMessage;
import com.yufa.xz.ly.exeception.RpcException;
import com.yufa.xz.ly.registery.ServiceRegistry;
import com.yufa.xz.ly.registery.zk.ZkServiceRegistry;
import com.yufa.xz.ly.remoting.transport.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        this.serviceMap = new ConcurrentHashMap<>();
        this.registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = new ZkServiceRegistry();
    }

    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.getServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, service);
        log.info("Add service: {} and interfaces:{}", rpcServiceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.getServiceName();
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object service) {
        this.publishService(service,
                RpcServiceProperties.builder().group("").version("").build());
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Class<?> serviceInterface = service.getClass().getInterfaces()[0];
            String serviceName = serviceInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            this.addService(service, serviceInterface, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(), new InetSocketAddress(host, NettyServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
