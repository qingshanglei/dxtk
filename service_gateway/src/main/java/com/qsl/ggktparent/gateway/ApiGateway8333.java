package com.qsl.ggktparent.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // 开启服务注册中心，除了Eureq的
@SpringBootApplication
public class ApiGateway8333 {
    public static void main(String[] args) {
        SpringApplication.run(ApiGateway8333.class, args);
    }
}
