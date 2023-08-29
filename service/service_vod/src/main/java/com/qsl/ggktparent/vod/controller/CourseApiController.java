package com.qsl.ggktparent.vod.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.vod.Course;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "课程信息返给微信公众号")
@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("根据关键字查询课程信息")
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@ApiParam(value = "关键字", required = true) @PathVariable String keyword) {
        LambdaQueryWrapper<Course> lqw = new LambdaQueryWrapper<>();
        lqw.like(Course::getTitle, keyword);
        List<Course> list = courseService.list(lqw);
        return list;
    }

    @ApiOperation("根据课程分类id查询课程列表")
    @GetMapping("/findPageCourse/{subjectParentId}/{page}/{limit}")
    public Result findPageCourse(@ApiParam("课程一级分类id") @PathVariable Long subjectParentId,
                                 @ApiParam("当前页码") @PathVariable("page") Long page,
                                 @ApiParam("每页记录数") @PathVariable("limit") Long limit) {

        Map<String, Object> map = courseService.getCourseBySubjectId(subjectParentId, page, limit);
        return Result.ok(map);
    }

    @ApiOperation("根据课程id查询课程信息")// 课程表，教师表，课程简介表，课程分类表，课程章节表，课程小节表
    @GetMapping("getInfo/{courseId}")
    public Result getCourseById(@ApiParam(value = "课程id") @PathVariable String courseId) {
        Map<String, Object> map = courseService.getCourseById(courseId);
        return Result.ok(map);
    }

    @ApiOperation("根据课程Id查询课程信息") // 服务调用根上面那个连了几张表的不同
    @GetMapping("getCourseById/{courseId}")
    public Course getCourseByCourseId(@ApiParam("课程id") @PathVariable("courseId") Long courseId) {
        Course course = courseService.getById(courseId);
        return course;
    }

}
