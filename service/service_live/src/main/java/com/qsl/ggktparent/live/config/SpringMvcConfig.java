package com.qsl.ggktparent.live.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 注意：使用拦截器必须把spirng和SpringMvc配置分开。
@Configuration // 配置类
@ComponentScan("com.qsl.ggktparent.utils") // 默认bean  注意：此处主要是使用工具类的SpringMVC拦截器
public class SpringMvcConfig {


}
