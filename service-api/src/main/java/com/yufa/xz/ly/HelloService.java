package com.yufa.xz.ly;

/**
 * @author LiuYe
 * @data 2020/9/17
 */
public interface HelloService {

    /**
     * 服务端实际调用的方法
     * @param hello hello.message
     * @return hello.description
     */
    String hello(Hello hello);
}
