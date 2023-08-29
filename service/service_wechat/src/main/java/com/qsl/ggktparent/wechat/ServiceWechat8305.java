package com.qsl.ggktparent.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 //开启Swagger
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.qsl.ggktparent.client.vod", "com.qsl.ggktparent.client.order"})
// 开启服务调用   basePackages:扫描到指定包
@MapperScan("com.qsl.ggktparent.wechat.mapper") //@MapperScan注解替代@Mapper注解
public class ServiceWechat8305 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceWechat8305.class, args);
    }
}
