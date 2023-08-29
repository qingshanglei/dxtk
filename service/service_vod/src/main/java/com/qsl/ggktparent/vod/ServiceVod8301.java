package com.qsl.ggktparent.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2 //开启Swagger2
@EnableDiscoveryClient // 开启服务注册中心，除了Eureq的
@SpringBootApplication
public class ServiceVod8301 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVod8301.class,args);
    }
}
