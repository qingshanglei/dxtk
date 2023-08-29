package com.qsl.ggktparent.vod.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.vod.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.vod.CourseFormVo;
import com.qsl.ggktparent.vo.vod.CoursePublishVo;
import com.qsl.ggktparent.vo.vod.CourseQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
public interface CourseService extends IService<Course> {

    // 条件分页查询-课程视频(点播管理)
    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    // 课程发布-添加课程基本信息
    Long saveCourseInfo(CourseFormVo courseFormVo);

    // 根据id查询课程基本信息
    CourseFormVo getCourseInfoById(Long id);

    // 根据id修改课程基本信息
    void updateCourseId(CourseFormVo courseFormVo);

    // 根据课程id查询课程信息
    CoursePublishVo getCoursePublishVo(Long id);

    // 根据课程id发布课程
    Boolean publishCourseById(Long id);

    // 根据课程id删除课程
    int removeCourseById(Long courseId);

    // 根据课程分类id查询课程列表
    Map<String, Object> getCourseBySubjectId(Long subjectParentId, Long page, Long limit);

    // 根据课程id查询课程信息
    Map<String, Object> getCourseById(String courseId);

    //    查询所有课程
    List<Course> getCourseAll();
}
