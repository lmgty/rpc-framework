package com.yufa.xz.ly.registery.zk.util;

import com.yufa.xz.ly.enumeration.RpcConfigProperties;
import com.yufa.xz.ly.exeception.RpcException;
import com.yufa.xz.ly.file.PropertiesFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Curator(zookeeper client) utils
 *
 * @author LiuYe
 * @data 2020/9/21
 */
@Slf4j
public class CuratorUtils {

    public static final String ZK_REGISTER_ROOT_PATH = "/ly-rpc";
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static String defaultZookeeperAddress = "192.168.137.149:2181";
    private static CuratorFramework zkClient;

    public CuratorUtils() {
    }

    /**
     * Create persistent nodes.
     *
     * @param zkClient zkClient
     * @param path     node path
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node is already exists. The node is : [{}]", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is : [{}]", path);
                REGISTERED_PATH_SET.add(path);
            }
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Get the children under a node.
     *
     * @param zkClient       zkClient
     * @param rpcServiceName rpc service name eg:com.yufa.xz.ly.HelloServicetest2version2
     * @return
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            // 下一级节点名字的List
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }
        return result;
    }

    /**
     * Empty the registry of data
     *
     * @param zkClient zkClient
     */
    public static void clearRegistry(CuratorFramework zkClient) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                zkClient.delete().forPath(p);
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e.getCause());
            }
        });
        log.info("All registered services on the server are cleared : [{}]", REGISTERED_PATH_SET.toString());
    }

    public static CuratorFramework getZkClient() {
        // check if user had set zk properties
        Properties properties = PropertiesFileUtils.readPropertiesFile(RpcConfigProperties.RPC_CONFIG_PATH.getPropertyValue());
        if (properties != null) {
            defaultZookeeperAddress = properties.getProperty(RpcConfigProperties.ZK_ADDRESS.getPropertyValue());
        }

        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(defaultZookeeperAddress)
                .build();
        zkClient.start();
        return zkClient;
    }

    /**
     * Registers to listen for changes to the specified node
     * @param rpcServiceName rpc service name eg:com.yufa.xz.ly.HelloServicetest2version2
     * @param zkClient zkClient
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent)->{
            List<String> serviceAddress = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddress);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RpcException(e.getMessage(), e.getCause());
        }

    }
}













