package com.qsl.ggktparent.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 腾讯对象存储-上传文件
 */
public interface FileService {
    // COS-文件上传
    String upload(MultipartFile file);

    // VOD-视频上传
    String addVod(InputStream inputStream, String originalFilename);

    // VOD-删除视频
    void removeVideo(String FileId);
}
