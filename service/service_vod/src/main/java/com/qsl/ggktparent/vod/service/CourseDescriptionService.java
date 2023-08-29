package com.qsl.ggktparent.vod.service;

import com.qsl.ggktparent.model.vod.CourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
public interface CourseDescriptionService extends IService<CourseDescription> {

    // 根据课程id删除课程描述
    void removeCourseDescriptionByCourseId(Long courseId);
}
