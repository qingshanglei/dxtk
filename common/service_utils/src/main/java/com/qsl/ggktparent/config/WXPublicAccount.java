package com.qsl.ggktparent.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取微信公众号-AppId,appSecret
 */
@Component // 配置类
public class WXPublicAccount implements InitializingBean {

    @Value("${wechat.mpAppId}")
    private String appid;
    @Value("${wechat.mpAppSecret}")
    private String appSecret;

   public static String ACCESS_KEY_ID;
   public static String  ACCESS_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
          ACCESS_KEY_ID=appid;
          ACCESS_KEY_SECRET=appSecret;
    }
}
