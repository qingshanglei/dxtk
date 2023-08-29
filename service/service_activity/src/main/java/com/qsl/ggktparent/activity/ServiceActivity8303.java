package com.qsl.ggktparent.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients(basePackages = "com.qsl.ggktparent")//开启服务调用
@EnableDiscoveryClient //开启服务注册中心，，
@SpringBootApplication
public class ServiceActivity8303 {

    public static void main(String[] args) {
        SpringApplication.run(ServiceActivity8303.class, args);
    }
}
