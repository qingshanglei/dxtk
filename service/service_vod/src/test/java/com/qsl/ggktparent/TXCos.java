package com.qsl.ggktparent;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;


/**
 * 腾讯对象存储：COS
 */
public class TXCos {

    public static void main(String[] args) {
        // ==================初始化客户端=======
        // 1 初始化用户身份信息（secretId, secretKey）。  注意：SecretId和SecretKey都可使用子账号秘钥，遵循授权最小权限,降低使用风险。
//        String secretId = System.getenv("AKIDmRvEMPJD0d4HH3cvEJF0aw73jJl26ua3");// 注意：不能使用这个否则报 Access key cannot be null错
        String secretId = "AKIDmRvEMPJD0d4HH3cvEJF0aw73jJl26ua3";
        String secretKey = "H8nFWZYQp23rRogT5qTrztbBbv67wZ2g";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
// 2 设置 bucket 的地域,
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法
        Region region = new Region("ap-guangzhou");  // 地域
        ClientConfig clientConfig = new ClientConfig(region);
        // 从 5.6.54 版本开始，默认使用 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            // ==================上传文件头=======
            // 注意：此方法适用于20M以下图片类小文件上传，最大支持上传不超过5GB文件。5GB以上的文件必须使用分块上传或高级 API 接口上传。
            // 指定要上传的文件
            File localFile = new File("G:\\1111111.png");
            // 指定文件将要存放的存储桶  "
            String bucketName = "ggkt-1316853721";
// 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
            String key = "/2023/5/7/华为ICP.png";  // 修改名称并上传到指定的目录
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println(JSON.toJSONString(putObjectResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
