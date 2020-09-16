package com.yufa.xz.ly.remoting.dto;

import com.yufa.xz.ly.enumeration.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    private Integer code;
    private String message;
    private T data;

    private static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data){
            response.setData(data);
        }
        return response;
    }

    private static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
         RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }


}
