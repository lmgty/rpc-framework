package com.yufa.xz.ly.remoting.transport;

import com.yufa.xz.ly.remoting.dto.RpcRequest;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
public interface ClientTransport {

    /**
     * 发送请求消息
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

}
