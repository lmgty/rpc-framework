package com.yufa.xz.ly.exeception;

import com.yufa.xz.ly.enumeration.RpcErrorMessage;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorMessage rpcErrorMessage, String detail) {
        super(rpcErrorMessage.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessage rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }
}
