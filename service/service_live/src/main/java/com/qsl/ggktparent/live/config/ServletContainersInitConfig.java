package com.qsl.ggktparent.live.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * springMVC拦截器
 */
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {


    // spring配置
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    // springMVC配置
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    // 拦截路径配置
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
