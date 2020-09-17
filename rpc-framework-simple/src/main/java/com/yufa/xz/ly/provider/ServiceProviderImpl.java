package com.yufa.xz.ly.provider;

import com.yufa.xz.ly.entity.RpcServiceProperties;
import com.yufa.xz.ly.enumeration.RpcErrorMessage;
import com.yufa.xz.ly.exeception.RpcException;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;

    public ServiceProviderImpl() {
        this.serviceMap = new ConcurrentHashMap<>();
        this.registeredService = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.getServiceName();
        if (registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName,service);
        log.info("Add service: {} and interfaces:{}",rpcServiceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.getServiceName();
        Object service = serviceMap.get(rpcServiceName);
        if (null == service){
            throw  new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
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
        Class<?> serviceInterface = service.getClass().getInterfaces()[0];
        this.addService(service,serviceInterface,rpcServiceProperties);
    }
}
