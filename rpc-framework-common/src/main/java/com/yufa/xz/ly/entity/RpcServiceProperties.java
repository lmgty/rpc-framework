package com.yufa.xz.ly.entity;

import lombok.*;

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
public class RpcServiceProperties {
    private String version;
    /**
     * 当接口具有多个实现类时，按组区分
     */
    private String group;
    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
