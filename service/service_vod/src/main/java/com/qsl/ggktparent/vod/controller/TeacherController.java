package com.qsl.ggktparent.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.vod.Teacher;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.vod.TeacherQueryVo;
import com.qsl.ggktparent.vod.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-04
 */
@Api(tags = "教师接口")
//@CrossOrigin // 解决跨域问题①-当前接口支持跨域
@RestController
@RequestMapping(value = "/admin/vod/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("查询所有")
    @GetMapping("findAll")
    public Result findAllTeacher() {
        List<Teacher> teacherList = teacherService.list();
        return Result.ok(teacherList);
    }


    @ApiOperation("条件分页查询")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPage(@PathVariable Long current,
                           @PathVariable Long limit,
                           @ApiParam("查询实体类-可选参数")
                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {
        Page<Teacher> pageParam = new Page<>(current, limit);

        List<Teacher> list = teacherService.list();
        if (teacherQueryVo == null) { //查询全部
            IPage<Teacher> pageModel = teacherService.page(pageParam, null);
            return Result.ok(pageModel);
        } else {
            //获取条件值
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            // 条件判断
            QueryWrapper<Teacher> qw = new QueryWrapper<>();
            if (!StringUtils.isEmpty(name)) { //判断字符串是否为空
                qw.like("name", name);
            }
            if (!StringUtils.isEmpty(level)) { //判断字符串是否为空
                qw.like("level", level);
            }
            //  查询时间段
            if (!StringUtils.isEmpty(joinDateBegin)) { //判断字符串是否为空
                qw.ge("join_date", joinDateBegin);
            }
            if (!StringUtils.isEmpty(joinDateEnd)) { //判断字符串是否为空
                qw.le("join_date", joinDateEnd);
            }
            //调用方法分页查询
            IPage<Teacher> pageModel = teacherService.page(pageParam, qw);
            return Result.ok(pageModel);
        }
    }

    @ApiOperation("新增教师")
    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody Teacher teacher) {
        System.out.println("2222");
        boolean flag = teacherService.save(teacher);
        return Result.ok(flag);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("removeTeacher/{id}")
    public Result removeTeacher(@PathVariable("id") Long id) {
        boolean flag = teacherService.removeById(id);
        return Result.ok(flag);
    }

    @ApiOperation("根据Id查询所有")
    @GetMapping("getBYid/{Id}")
    public Result<Teacher> getBYid(@PathVariable("Id") Long id) {
        Teacher list = teacherService.getById(id);
        return Result.ok(list);
    }

    @ApiOperation("修改")
    @PutMapping("putTeacher")
    public Result putTeacher(@RequestBody Teacher teacher) {
        boolean flag = teacherService.updateById(teacher);
        return Result.ok(flag);
    }


    @ApiOperation("批量删除")
    @DeleteMapping("delTeacherAll")
    public Result delallTeacher(@ApiParam("批量修改Ids")
                                @RequestBody List<String> ids) {
        boolean flag = teacherService.removeByIds(ids);
        return Result.ok(flag);
    }


    @ApiOperation("根据Id查询教师信息-服务调用")
    @GetMapping("getTeacherById/{teacherId}")
    public Teacher getTeacherById(@PathVariable("teacherId") Long teacherId) {
        Teacher list = teacherService.getById(teacherId);
        return list;
    }
}

