package com.qsl.ggktparent.vod.service.impl;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.vod.config.TXCosConfig;
import com.qsl.ggktparent.vod.service.FileService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;

/**
 * TODO 腾讯对象存储:COS,VOD
 */
@Service
public class FileServiceImpl implements FileService {
    //======================================COS

    // COS-文件上传
    @Override
    public String upload(MultipartFile file) {
        // ==================初始化客户端=======
        // 1 初始化用户身份信息（secretId, secretKey）。  注意：SecretId和SecretKey都可使用子账号秘钥，遵循授权最小权限,降低使用风险。
        String secretId = TXCosConfig.ACCESS_KEY_ID;
        String secretKey = TXCosConfig.ACCESS_KEY_SECRET;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
// 2 设置 bucket 的地域,    注意： clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法
        Region region = new Region(TXCosConfig.END_POINT);  // 地域
        System.out.println(region);

        ClientConfig clientConfig = new ClientConfig(region);
        // 从 5.6.54 版本开始，默认使用 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);


        // 存储桶
        String bucketName = TXCosConfig.BUCKET_NAME;
        // 对象键(Key)是对象在存储桶中的唯一标识。   解决文件名称不重复：获取UUId并去除”-“，获取文件名称
        String key = UUID.randomUUID().toString().replaceAll("-", "")
                + file.getOriginalFilename(); // 文件名称
        // 根据日期实现文件分组
        String dateTime = new DateTime().toString("yyyy/MM/dd");
        key = dateTime + "/" + key;

        try {
            // 指定要上传的文件
            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();

            // 存储桶,文件名称，输入流，
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
            putObjectRequest.setStorageClass(StorageClass.Standard_IA);
            // 上传
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            System.out.println(JSON.toJSONString(putObjectResult));
            //  拼接返回路径：  https://ggkt-atguigu-1310644373.cos.ap-beijing.myqcloud.com/01.jpg
            String url = "https://" + bucketName + ".cos." + region.getRegionName() + ".myqcloud.com/" + key;
            System.out.println("@@@" + url);// https://ggkt-1316853721.cos.ap-guangzhou.myqcloud.com/2023/05/07/87bdec253093451bb8b53605a86787d03.jpg
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //======================================VOD

    // VOD-服务端-视频上传
    @Override
    public String addVod(InputStream inputStream, String originalFilename) {
        try {
            VodUploadClient client = new VodUploadClient(TXCosConfig.ACCESS_KEY_ID, TXCosConfig.ACCESS_KEY_SECRET); // secretId,secretKey
            VodUploadRequest request = new VodUploadRequest();
            request.setMediaFilePath("D:\\HYLiteResources\\video\\鬼刀.mp4");// 视频路径
//            request.setProcedure("LongVideoPreset"); // 任务流
            request.setConcurrentUploadNumber(5); // 分片上传
            VodUploadResponse response = client.upload(TXCosConfig.END_POINT, request); //地域
            //返回文件id保存到业务表，用于控制视频播放
            String fileId = response.getFileId();

            return fileId;
        } catch (Exception e) {
            // 业务方进行异常处理
            throw new BusinessException(20001, "视频上传失败");
        }
    }

    // VOD-删除视频
    @Override
    public void removeVideo(String FileId) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            Credential cred = new Credential(TXCosConfig.ACCESS_KEY_ID, TXCosConfig.ACCESS_KEY_SECRET);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com"); // 设置API服务的请求域名，调用腾讯云视频点播(VOD)的API接口。
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, TXCosConfig.END_POINT,clientProfile); //地域
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(FileId);

            // 返回的resp是一个DescribeMediaInfosResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            System.out.println(DescribeMediaInfosResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            throw  new BusinessException(20001,"删除视频失败");
        }
    }


}
