package com.qsl.ggktparent.live.config;

import com.qsl.ggktparent.live.mtcloud.MTCloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 读取欢拓云直播配置openID，openToken
 */
@Component // 默认bean
public class MTCloudConfig {

    /**
     * 合作方ID： 合作方在欢拓平台的唯一ID
     */
    @Value("${mtcloud.openId}")
    public String openID;

    /**
     * 合作方秘钥： 合作方ID对应的参数加密秘钥
     */
    @Value("${mtcloud.openToken}")
    public String openToken;

    @Bean
    public MTCloud mtCloudClient() {
        return new MTCloud(openID, openToken);
    }
}
