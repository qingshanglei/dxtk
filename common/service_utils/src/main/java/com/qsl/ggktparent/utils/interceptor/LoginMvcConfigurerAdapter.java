package com.qsl.ggktparent.utils.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * SpringMVC拦截器-登录拦截器   WebMvcConfigurer WebMvcConfigurationSupport
 */
@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    // 配置拦截器-请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器，设置路径
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/api/**");
//        registry.addInterceptor(new UserLoginInterceptor(redisTemplate)).addPathPatterns("/api/**");
//        registry.addInterceptor(new AdminLoginInterceptor(redisTemplate)).addPathPatterns("/admin/**");
//        super.addInterceptors(registry);
        System.out.println("2354235");
    }
}
