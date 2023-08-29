package com.qsl.ggktparent.vod.controller;


import com.qsl.ggktparent.model.vod.Video;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 课程视频 前端控制器
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Api(tags = "小节、课时表")
//@CrossOrigin   // 请求支持跨域
@RestController
@RequestMapping(value = "/admin/vod/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增")
    @PostMapping("save")
    public Result add(@RequestBody Video video) {
        boolean flag = videoService.save(video);
        return Result.ok(flag);
    }

    @ApiOperation("根据Id查看小节")
    @GetMapping("getById/{id}") //
    public Result getById(@ApiParam(name = "id", value = "小节id", required = true) @PathVariable Long id) {
        Video video = videoService.getById(id);
        return Result.ok(video);
    }

    @ApiOperation("修改")
    @PutMapping("updateById")
    public Result updateById(@RequestBody Video video) {
        boolean flag = videoService.updateById(video);
        return Result.ok(flag);
    }

    @ApiOperation("删除小节")
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        boolean flag = videoService.removeVideoById(id);
        return Result.ok(flag);
    }
}

