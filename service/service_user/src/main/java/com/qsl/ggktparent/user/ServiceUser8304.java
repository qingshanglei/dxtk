package com.qsl.ggktparent.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册中心
@MapperScan("com.qsl.ggktparent.user.mapper") // @MapperScan注解替代@Mapper
public class ServiceUser8304 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUser8304.class, args);
    }
}
