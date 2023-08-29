package com.qsl.ggktparent.vod.util;

import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

public class getVideoPlaySignature {


    public static String getVideoPlaySignature(String fileId, String secretId, String secretKey) {
        // 构造签名参数
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
        String signValidDuration = "3600";  // 签名有效期默认为一个小时
        String signature = null;

        try {
            // 计算签名参数
            TreeMap<String, Object> parameters = new TreeMap<>();
            parameters.put("currentTimeStamp", currentTime);
            parameters.put("signValidDuration", signValidDuration);
//            parameters.put("vodSubAppId", "your_vod_sub_app_id"); // 如果有子应用需要填上对应的子应用 ID
            parameters.put("fileId", fileId);
            String plainText = buildPlainText(parameters);
            signature = DigestUtils.sha1Hex(plainText + secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回计算得到的签名字符串
        return signature;
    }

    // 根据参数构建用于计算签名的明文字符串
    public static String buildPlainText(TreeMap<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();

        for (String key : parameters.keySet()) {
            Object value = parameters.get(key);
            sb.append(key).append("=").append(value.toString()).append("&");
        }

        return sb.substring(0, sb.length() - 1);
    }
}
