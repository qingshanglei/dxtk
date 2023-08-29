package com.qsl.ggktparent.vod.util;


import java.util.HashMap;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.qsl.ggktparent.vod.config.TXCosConfig;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

/**
 * 腾讯云-VOD—视频上传2_服务端上传（申请上传签名）
 * // TODO 跟官网的好像对不上，官网的播放签名是Token，这个不是
 */
@Setter // 生成secretId等setter
@Component
public class Signature {
    private String secretId;
    private String secretKey;
    private long currentTime;
    private int random;
    private int signValidDuration;
    private static final String HMAC_ALGORITHM = "HmacSHA1"; //签名算法
    private static final String CONTENT_CHARSET = "UTF-8"; //UTF-8

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    // 获取签名-上传视频签名
    public String getUploadSignature() throws Exception {
        String strSign = "";
        String contextStr = "";

        // 生成原始参数字符串
        long endTime = (currentTime + signValidDuration);
        contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
        contextStr += "&currentTimeStamp=" + currentTime;
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + random;
        contextStr += "&procedure=LongVideoPreset";//设置转码任务流

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);

            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = base64Encode(sigBuf);
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            throw e;
        }
        return strSign;
    }

    private String base64Encode(byte[] buffer) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(buffer);
    }



    // 视频播放签名（psign）
    public String getPsign(String  fileId) {
        String AudioVideoType = "Original";   //   RawAdaptive
        Integer RaWAdaptiveDefinition = 10;
        Integer ImageSpriteDefinition = 10;
        Long CurrentTime = System.currentTimeMillis() / 1000; //时间戳，播放器签名创建时间
        Long PsignExpire = System.currentTimeMillis() / 1000; //时间戳，播放器签名过期时间
        String UrlTimeExpire = ""; // 防盗链过期时间
        String PlayKey = TXCosConfig.PlayKey; // 播放秘钥
        HashMap<String, Object> urlAccessInfo = new HashMap<>();
        urlAccessInfo.put("t", UrlTimeExpire); // 防盗链
        HashMap<String, Object> contentInfo = new HashMap<>();
        contentInfo.put("audioVideoType", AudioVideoType);
//        contentInfo.put("raWAdaptiveDefinition", RaWAdaptiveDefinition); //
//        contentInfo.put("imageSpriteDefinition", ImageSpriteDefinition); //

        Algorithm algorithm = Algorithm.HMAC256(PlayKey);
        String token = JWT.create().withClaim("appId", 1316853721)  // 必须为整型
                .withClaim("fileId", fileId)
                .withClaim("contentInfo", contentInfo)
                .withClaim("currentTimeStamp", CurrentTime)
                .sign(algorithm);
        return token;
    }

}

