package com.yufa.xz.ly;

import com.yufa.xz.ly.entity.RpcServiceProperties;
import com.yufa.xz.ly.proxy.RpcClientProxy;
import com.yufa.xz.ly.remoting.transport.ClientTransport;
import com.yufa.xz.ly.remoting.transport.netty.client.NettyClientTransport;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        ClientTransport rpcClient = new NettyClientTransport();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                .group("test2").version("version2").build();

        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceProperties);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String result = helloService.hello(new Hello("111", "222"));
        System.out.println(result);

        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            result = helloService.hello(new Hello("111", " i = " + i));
            System.out.println(result);
        }
    }
}
