package com.qsl.ggktparent.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.live.LiveCourseDescription;
import com.qsl.ggktparent.live.mapper.LiveCourseDescriptionMapper;
import com.qsl.ggktparent.live.service.LiveCourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Service
public class LiveCourseDescriptionServiceImpl extends ServiceImpl<LiveCourseDescriptionMapper, LiveCourseDescription> implements LiveCourseDescriptionService {

    // 根据直播课程id查询
    @Override
    public LiveCourseDescription getliveByliveCourseId(Long liveCourseId) {
        LambdaQueryWrapper<LiveCourseDescription> lqw = new LambdaQueryWrapper<LiveCourseDescription>()
                .like(LiveCourseDescription::getLiveCourseId, liveCourseId);
        LiveCourseDescription description = baseMapper.selectById(lqw);
        return description;
    }
}
