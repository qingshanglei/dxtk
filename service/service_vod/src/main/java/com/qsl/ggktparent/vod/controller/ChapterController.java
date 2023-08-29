package com.qsl.ggktparent.vod.controller;


import com.qsl.ggktparent.model.vod.Chapter;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.vod.ChapterVo;
import com.qsl.ggktparent.vod.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程 前端控制器
 * @author 青衫泪
 * @since 2023-05-08
 */
@Api(tags = "视频课程-章节表")
//@CrossOrigin  // 请求支持跨域
@RestController
@RequestMapping("/admin/vod/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("查询大纲列表（章节，小节列表）")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getNestedTreeList(@ApiParam(value = "课程id") @PathVariable String courseId) {
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);
        return Result.ok(chapterVoList);
    }

    // 添加章节   修改-根据id查询     修改       删除章节
    @ApiOperation("添加章节")
    @PostMapping("save")
    public Result add(@RequestBody Chapter chapter) {
        boolean count = chapterService.save(chapter);
        return Result.ok(count);
    }

    @ApiOperation("修改-根据id查询")
    @GetMapping("getById/{id}")
    public Result getById(@ApiParam(value = "章节id") @PathVariable Long id) {
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }

    @ApiOperation("修改")
    @PutMapping("updateById")
    public Result update(@ApiParam(value = "章节对象") @RequestBody Chapter chapter) {
        boolean flag = chapterService.updateById(chapter);
        return Result.ok(flag);
    }

    @ApiOperation("根据章节id删除章节")
    @DeleteMapping("deleteById/{id}")
    public Result delete(@ApiParam(name = "chapterId",value = "章节id") @PathVariable String id) {
        boolean flag = chapterService.removeChapterById(id);
        return Result.ok(flag);
    }
}

