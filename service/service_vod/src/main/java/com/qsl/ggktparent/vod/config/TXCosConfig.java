package com.qsl.ggktparent.vod.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 腾讯对象存储-读取properties文件的地域；秘钥：secretId，secretKey ；存储桶
 */
@Component //配置类
public class TXCosConfig implements InitializingBean {

    // 读取properties文件的地域；秘钥：secretId，secretKey ；存储桶
    @Value("${tencent.cos.file.region}")
    private String region;
    @Value("${tencent.cos.file.secretid}")
    private String secretId;
    @Value("${tencent.cos.file.secretkey}")
    private String secretKey;
    @Value("${tencent.cos.file.bucketname}")
    private String bucketName;
    @Value("${tencent.vod.PlayKey}")
    private String playKey;

    // 设置地域；秘钥：secretId，secretKey ；存储桶的权限为public
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    public static String PlayKey;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = region;   // 地域
        ACCESS_KEY_ID = secretId; // secretId
        ACCESS_KEY_SECRET = secretKey; //secretKey
        BUCKET_NAME = bucketName; //存储桶
        PlayKey = playKey; //播放秘钥
    }
}
