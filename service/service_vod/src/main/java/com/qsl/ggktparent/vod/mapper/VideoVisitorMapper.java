package com.qsl.ggktparent.vod.mapper;

import com.qsl.ggktparent.model.vod.VideoVisitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qsl.ggktparent.vo.vod.VideoVisitorCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-13
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    // 课程统计
    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
