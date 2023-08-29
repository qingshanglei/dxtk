package com.qsl.ggktparent.live.service;

import com.qsl.ggktparent.model.live.LiveCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
public interface LiveCourseDescriptionService extends IService<LiveCourseDescription> {

    // 根据直播课程id查询
    LiveCourseDescription getliveByliveCourseId(Long liveCourseId);
}
