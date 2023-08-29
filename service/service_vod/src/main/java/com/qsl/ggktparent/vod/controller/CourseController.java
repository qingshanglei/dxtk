package com.qsl.ggktparent.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.vod.Course;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.vod.CourseFormVo;
import com.qsl.ggktparent.vo.vod.CoursePublishVo;
import com.qsl.ggktparent.vo.vod.CourseQueryVo;
import com.qsl.ggktparent.vod.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 课程 前端控制器
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Api(tags = "课程基本信息-点播管理")
@CrossOrigin // 请求支持跨域
@RestController
@RequestMapping(value = "/admin/vod/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("条件分页查询-课程视频(点播管理)")
    @GetMapping("PageCource/{page}/{limit}")
    public Result PageCource(@ApiParam(name = "page", value = "当前页面") @PathVariable Long page,
                             @ApiParam(name = "limit", value = "每页记录数") @PathVariable Long limit,
                             @ApiParam(name = "courseVo", value = "查询对象") CourseQueryVo courseQueryVo) {
        Page<Course> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.findPage(pageParam, courseQueryVo);
        return Result.ok(map);
    }

    @ApiOperation("课程发布-添加课程基本信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo) {
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        //返回课程id
        return Result.ok(courseId);
    }


    @ApiOperation("根据id查询课程基本信息-数据回填")
    @GetMapping("getById/{id}")
    public Result getById(@ApiParam("id") @PathVariable Long id) {
        CourseFormVo courseFormVo = courseService.getCourseInfoById(id);
        return Result.ok(courseFormVo);
    }

    @ApiOperation("根据id修改课程基本信息")
    @PostMapping("updateById")
    public Result updateById(@RequestBody CourseFormVo courseFormVo) {
        courseService.updateCourseId(courseFormVo);
        return Result.ok(courseFormVo.getId());
    }

    //=================发布课程
    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@ApiParam(value = "课程id", required = true) @PathVariable Long id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }

    @ApiOperation("根据课程id发布课程")
    @PutMapping("publishCourseById/{id}")
    public Result publishCourseById(@ApiParam(value = "课程id", required = true) @PathVariable Long id) {
        Boolean flag = courseService.publishCourseById(id);
        return Result.ok(flag);
    }

    @ApiOperation("根据课程id删除课程")
    @DeleteMapping("remoteById/{id}")
    public Result remoteById(@ApiParam(value = "课程id", required = true) @PathVariable Long id) {
        int count = courseService.removeCourseById(id);
        return Result.ok(count);
    }


    @ApiOperation("查询所有课程")
    @GetMapping("getCourseAll")
    public Result getCourseAll() {
        List<Course> course = courseService.getCourseAll();
        return Result.ok(course);
    }

}

