package com.qsl.ggktparent.live.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.live.LiveCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.live.LiveCourseFormVo;
import com.qsl.ggktparent.vo.live.LiveCourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
public interface LiveCourseService extends IService<LiveCourse> {

    // 分页查询直播课程
    Page<LiveCourse> pageLiveCourse(Long page, Long limit);

//    添加直播-欢拓云直播
    boolean saveliveCourse(LiveCourseFormVo liveCourseFormVo);

//    删除直播课程-欢拓云直播 (直播课程表，直播账号表,直播课程描述表)
    Boolean remoteById(Long liveCourseId);

//    根据id查询直播课程，直播课程描述
    LiveCourseFormVo getLiveAndDescriptionById(Long liveCourseId);

//    根据id修改直播信息-欢拓云直播，直播课程表，直播课程描述表
    Boolean updateliveCourse(LiveCourseFormVo liveCourseFormVo);

    // 获取最近的直播信息
    List<LiveCourseVo> findLatelyList();

    // 用户获取欢拓云直播access_token
    JSONObject getLiveAccessToken(Long LiveCourseId, Long userId);

//    根据id查询课程   直播课程表，直播账号表,直播课程描述表,教师表
    Map<String, Object> getInfo(Long courseId);
}
