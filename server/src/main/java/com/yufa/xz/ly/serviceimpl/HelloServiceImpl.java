package com.yufa.xz.ly.serviceimpl;

import com.yufa.xz.ly.Hello;
import com.yufa.xz.ly.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author LiuYe
 * @data 2020/9/17
 */
@Slf4j
@Component
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("HelloServiceImpl被创建");
    }
    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl 收到: {}", hello.getMessage());
        String result = "Hello description is" + hello.getDescription();
        log.info("HelloServiceImpl 返回: {}",result);
        return result;
    }
}
