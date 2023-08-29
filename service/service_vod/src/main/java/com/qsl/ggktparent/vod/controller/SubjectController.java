package com.qsl.ggktparent.vod.controller;


import com.qsl.ggktparent.model.vod.Subject;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 课程科目 前端控制器
 *
 * @author 青衫泪
 * @since 2023-05-07
 */
@Api(tags = "课程分类")
//@CrossOrigin // 支持跨域
@RestController
@RequestMapping(value = "/admin/vod/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("课程查询-树形结构_懒加载")
    @GetMapping("/getChildSubject/{id}")
    public Result getSubjectService(@PathVariable("id") Long id) {
        List<Subject> list = subjectService.selectList(id);
        return Result.ok(list);
    }

    @ApiOperation("导出分类课程")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response) {
        subjectService.exportData(response);
    }

    @ApiOperation("导入课程")
    @PostMapping("importData")
    public void importData(MultipartFile file) {
        subjectService.importDictData(file);
    }

    @ApiOperation("新增课程分类")
    @PostMapping("saveSubject")
    public Result saveSubject(@ApiParam("实体类") @RequestBody Subject subject) {
        boolean flag = subjectService.save(subject);
        return Result.ok(subject);
    }


}

