package com.qsl.ggktparent.vod.service;

import com.qsl.ggktparent.model.vod.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
public interface VideoService extends IService<Video> {
    // 根据课程id删除小节 及其腾讯云VOD的视频
    void removeVideoByCourseId(Long courseId);

    // 根据小节id删除小节 及其腾讯云VOD的视频
    boolean removeVideoById(String id);

    // 根据课程id查询小节名称
    List<Video> getVideoByCourseId(Long courseId);

    //    根据小节id 获取云端视频资源id和SubAppId 播放视频
    Map<String, Object> getPlayAuth( Long videoId);
}
