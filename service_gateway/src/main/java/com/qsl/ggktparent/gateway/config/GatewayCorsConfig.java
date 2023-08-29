package com.qsl.ggktparent.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 网关解决跨域问题:
 * 覆盖@CrossOrigin：请求支持跨域注解；
 * 注意：网关跨域方案不能与@CrossOrigin注解一起使用，否则跨域请求会失败。
 */
@Configuration // 配置类
public class GatewayCorsConfig {

    // 网关处理跨域问题
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*"); //
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*"); // 所有请求头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
