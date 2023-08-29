package com.qsl.ggktparent.activity.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *   mp分页插件
 */
@Configuration // 配置类
@MapperScan("com.qsl.ggktparent.activity.mapper") //  @MapperScan注解替代@Mapper或@Resource注解
public class mpConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
