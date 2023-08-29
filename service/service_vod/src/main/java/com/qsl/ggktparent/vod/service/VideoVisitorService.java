package com.qsl.ggktparent.vod.service;

import com.qsl.ggktparent.model.vod.VideoVisitor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-13
 */
public interface VideoVisitorService extends IService<VideoVisitor> {

    // 课程统计
    Map<String, Object> findCount(Long courseId, String startDate, String endDate);
}
