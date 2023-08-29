package com.qsl.ggktparent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.vod.CourseDescription;
import com.qsl.ggktparent.vod.mapper.CourseDescriptionMapper;
import com.qsl.ggktparent.vod.service.CourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

    // 根据课程id删除课程描述
    @Override
    public void removeCourseDescriptionByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseDescription> lqw = new LambdaQueryWrapper<>();
        lqw.like(CourseDescription::getCourseId,courseId);
        baseMapper.delete(lqw);
    }
}
