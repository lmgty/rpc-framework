package com.yufa.xz.ly.remoting.handler;

import com.yufa.xz.ly.factory.SingletonFactory;
import com.yufa.xz.ly.provider.ServiceProvider;
import com.yufa.xz.ly.provider.ServiceProviderImpl;
import com.yufa.xz.ly.remoting.dto.RpcRequest;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest rpcRequest){
         Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
         return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        return null;
    }
}
