package com.yufa.xz.ly.remoting.dto;

import com.yufa.xz.ly.entity.RpcServiceProperties;
import com.yufa.xz.ly.enumeration.RpcMessageType;
import lombok.*;

import java.io.Serializable;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private RpcMessageType rpcMessageType;
    private String version;
    private String group;

    public RpcServiceProperties toRpcProperties(){
        return RpcServiceProperties.builder()
                .serviceName(this.getInterfaceName())
                .version(this.getVersion())
                .build();
    }
}
