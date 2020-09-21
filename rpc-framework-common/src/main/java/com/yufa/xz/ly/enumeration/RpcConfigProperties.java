package com.yufa.xz.ly.enumeration;

/**
 * @author LiuYe
 * @data 2020/9/21
 */
public enum RpcConfigProperties {
    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;

    RpcConfigProperties(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue(){
        return propertyValue;
    }
}
