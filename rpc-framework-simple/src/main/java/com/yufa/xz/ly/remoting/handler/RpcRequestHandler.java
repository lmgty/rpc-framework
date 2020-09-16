package com.yufa.xz.ly.remoting.handler;

import com.yufa.xz.ly.exeception.RpcException;
import com.yufa.xz.ly.factory.SingletonFactory;
import com.yufa.xz.ly.provider.ServiceProvider;
import com.yufa.xz.ly.provider.ServiceProviderImpl;
import com.yufa.xz.ly.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service: [{}] successful invoke method: [{}]", rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
