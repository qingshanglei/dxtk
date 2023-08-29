package com.qsl.ggktparent.vod.mapper;

import com.qsl.ggktparent.model.vod.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qsl.ggktparent.vo.vod.CoursePublishVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    // 根据课程id查询课程信息
    CoursePublishVo selectCoursePublishVoById(Long id);

    // 根据课程分类id查询课程列表
    @MapKey("id") // @MapKey("id")； 返回Map值,返回的Map值对应CourseVo实体类
    Map<String, Object> getCourseBySubjectId(Long subjectParentId);


}
