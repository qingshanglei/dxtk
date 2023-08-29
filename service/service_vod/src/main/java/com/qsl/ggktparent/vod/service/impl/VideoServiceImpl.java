package com.qsl.ggktparent.vod.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.vod.Video;
import com.qsl.ggktparent.vod.mapper.VideoMapper;
import com.qsl.ggktparent.vod.service.FileService;
import com.qsl.ggktparent.vod.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.vod.util.Signature;
import com.qsl.ggktparent.vod.util.getVideoPlaySignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private FileService fileService;

    @Value("${tencent.vod.appID}")
    private Integer appId;
    @Value("${tencent.cos.file.secretkey}")
    private String secretkey;

    // 删除小节 及其腾讯云(VOD)的视频
    @Override
    public boolean removeVideoById(String id) {
        // 查询小节
        Video video = baseMapper.selectById(id);
        //  获取video里面视频id
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) { //视频id不为空，删除视频
            fileService.removeVideo(videoSourceId);
        }
        // 删除小节
        int flag = baseMapper.deleteById(id);
        return flag > 0;
    }

    // 根据课程id查询小节名称
    @Override
    public List<Video> getVideoByCourseId(Long courseId) {
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<Video>().like(Video::getCourseId, courseId);
        List<Video> videoList = baseMapper.selectList(lqw);
        return videoList;
    }


    // 根据课程id删除小节 及其腾讯云(VOD)的视频
    @Override
    public void removeVideoByCourseId(Long courseId) {
        // 根据课程id课程所有小节
        QueryWrapper<Video> qw = new QueryWrapper<>();
        qw.like("course_id", courseId);
        List<Video> videoList = baseMapper.selectList(qw);

        // 遍历所有小节集合得到每个小节，获取每个小节视频id
        for (Video video : videoList) {
            // 获取小节里视频的id
            String videoSourceId = video.getVideoSourceId();
            // 判断视频id是否为空，不为空，删除腾讯云(VOD)视频
            if (!StringUtils.isEmpty(videoSourceId)) {
                fileService.removeVideo(videoSourceId);
            }
        }

        // 根据课程id删除课程所有小节
        baseMapper.delete(qw);
    }


    private GarbageCollectorMXBean GarbageCollectorMXBean;


    @Autowired
    private Signature signature;

    //    根据小节id 获取云端视频资源id和SubAppId 播放视频
//    获取Token类型的psign(签名)方法
    @Override
    public Map<String, Object> getPlayAuth(Long videoId) {
        Video video = baseMapper.selectById(videoId);
        if (video == null) {
            throw new BusinessException(20001, "小节信息不存在");
        }

        // 生成播放签名
        String psign = signature.getPsign(video.getVideoSourceId()); // 视频id

        log.error("token====》" + psign);
        Map<String, Object> map = new HashMap<>();
        map.put("VideoSourceId", video.getVideoSourceId()); // 云端视频资源id
        map.put("appId", appId);// Vod的SubAppId
        map.put("psign", psign);// psign(播放器签名)
        return map;
    }


}

