package com.qsl.ggktparent.client.vod;

import com.qsl.ggktparent.model.vod.Course;
import com.qsl.ggktparent.model.vod.Teacher;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@Component // 默认bean
@FeignClient("service-vod") // 服务调用service-vod
public interface CourseFeignClient {

    @ApiOperation("根据关键字查询课程信息")
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findByKeyword(@ApiParam(value = "关键字", required = true) @PathVariable("keyword") String keyword);

    @ApiOperation("根据课程Id查询课程信息") // 服务调用根上面那个连了几张表的不同
    @GetMapping("/api/vod/course/getCourseById/{courseId}")
    Course getCourseByCourseId(@ApiParam("课程id") @PathVariable("courseId") Long courseId);

    @ApiOperation("根据Id查询教师信息-服务调用")
    @GetMapping("/admin/vod/teacher/getTeacherById/{teacherId}")
    Teacher getTeacherById(@PathVariable("teacherId") Long teacherId);
}
