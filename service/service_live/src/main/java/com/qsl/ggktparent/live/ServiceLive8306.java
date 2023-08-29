package com.qsl.ggktparent.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 // 开启Swagger2
@SpringBootApplication
@EnableDiscoveryClient // 服务注册
@EnableFeignClients(basePackages = "com.qsl.ggktparent") // 服务调用
@MapperScan("com.qsl.ggktparent.live.mapper")
//@EnableWebMvc // SpringMVC
public class ServiceLive8306 {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLive8306.class, args);
    }
}
