package com.qsl.ggktparent.user.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信公众号
 */
@Component // 默认bean
public class WeChatConfig {

    @Autowired
    private WXPublicAccount wxPublicAccount;

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    private WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage(); // // weixin-java-mp的2.7版本
//        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl(); // 必须weixin-java-mp的4.1版本,
        wxMpConfigStorage.setAppId(WXPublicAccount.ACCESS_KEY_ID);
        wxMpConfigStorage.setSecret(WXPublicAccount.ACCESS_KEY_SECRET);
        return wxMpConfigStorage;
    }
}
