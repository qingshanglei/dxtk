package com.qsl.ggktparent.vod.controller;

import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.config.TXCosConfig;
import com.qsl.ggktparent.vod.service.FileService;
import com.qsl.ggktparent.vod.util.Signature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * 腾讯对象存储-上传文件
 */

@Api(tags = "腾讯云-上传文件、视频")
@RestController // bean
@RequestMapping("/admin/vod/file")
@CrossOrigin // 支持浏览器跨域 （注意：上传图片必须使用这个，网关 和前端使用代理服务器没用。）
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @ApiOperation("COS-文件(图片)上传")
    @PostMapping("upload")   // 注意：图片：修改的话只是修改数据库的路径，腾讯Cos图片没删除。
    public Result upload(@ApiParam(name = "file", value = "文件") // 参数名称，参数说明
                         @RequestParam("file") MultipartFile file) {
        System.out.println(file);

        String uploadUrl = fileService.upload(file);
        return Result.ok(uploadUrl).message("文件上传成功");
    }


    @ApiOperation("VOD-服务端-视频上传")
    @PostMapping("addVod")
    public Result addVod(@ApiParam(name = "file", value = "文件", required = true)
                       @RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String videoId = fileService.addVod(inputStream, originalFilename);
        return Result.ok(videoId); //返回视频id
    }

    @ApiOperation("VOD-客户端-视频上传")
    @GetMapping("sign")
    public Result sign() {
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(TXCosConfig.ACCESS_KEY_ID);
        sign.setSecretKey(TXCosConfig.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天

        try {
            String signature = sign.getUploadSignature();
            System.out.println("上传成功" +"signature : " + signature);
            return Result.ok(signature);
        } catch (Exception e) {
            throw new BusinessException(20001, "VOD-客户端-视频上传失败");
        }
    }

    @ApiOperation("VOD-删除视频")
    @DeleteMapping("removeById/{FileId}")
    public Result removeById(@ApiParam(value = "FileId", required = true) @PathVariable String FileId) {
        fileService.removeVideo(FileId);
        return Result.ok("null");
    }
}
