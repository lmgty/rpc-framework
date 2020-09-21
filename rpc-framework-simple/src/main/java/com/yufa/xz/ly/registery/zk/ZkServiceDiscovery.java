package com.yufa.xz.ly.registery.zk;

import com.yufa.xz.ly.enumeration.RpcErrorMessage;
import com.yufa.xz.ly.exeception.RpcException;
import com.yufa.xz.ly.registery.ServiceDiscovery;
import com.yufa.xz.ly.registery.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author LiuYe
 * @data 2020/9/21
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {
    public ZkServiceDiscovery() {
    }

    @Override
    public InetSocketAddress lookUpService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList.size() == 0) {
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // 注册的服务URL都一样。。
        // 计划未来实现调用远程服务的时候进行负载均衡
        String targetServiceUrl = serviceUrlList.get(0);
        log.info("Successfully found the service address : [{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
