package com.qsl.ggktparent.live.mapper;

import com.qsl.ggktparent.model.live.LiveCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qsl.ggktparent.vo.live.LiveCourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    // 获取最近的直播信息  @Param("page") Long page,@Param("limit") Long limit
    List<LiveCourseVo> findLatelyList();
}
