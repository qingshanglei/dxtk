package com.qsl.ggktparent.live.controller;


import com.qsl.ggktparent.live.service.LiveCourseConfigService;
import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.live.LiveCourseConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 直播课程配置表 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Api(tags = "直播课程配置表")
@RestController
@RequestMapping("/admin/live/liveCourseConfig")
public class LiveCourseConfigController {

    @Autowired
    private LiveCourseConfigService liveCourseConfigService;

    @ApiOperation("获取直播配置信息")
    @GetMapping("getLiveCourseConfig/{liveCourseId}")
    public Result getLiveCourseConfigByliveCourseId(@ApiParam("直播课程id") @PathVariable Long liveCourseId) {
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getLiveCourseConfig(liveCourseId);
        return Result.ok(liveCourseConfig);
    }


    @ApiOperation("修改直播配置信息-欢拓云直播")
    @PutMapping("updateLiveCourseConfig")
    public Result updateLiveCourseConfig(@ApiParam("直播课程id") @RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        Boolean flag = liveCourseConfigService.updateLiveCourseConfig(liveCourseConfigVo);
        return Result.ok(flag);
    }


}

