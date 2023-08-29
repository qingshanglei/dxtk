package com.qsl.ggktparent.live.service;

import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.live.LiveCourseConfigVo;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {

    // 获取直播配置信息
    LiveCourseConfig getLiveCourseConfig(Long liveCourseId);

    //    修改直播配置信息-欢拓云直播
    Boolean updateLiveCourseConfig(LiveCourseConfigVo liveCourseConfigVo);
}
