package com.qsl.ggktparent.vod.controller;


import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.service.VideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-13
 */
@Api(tags = "课程统计表")
@RestController
@RequestMapping(value = "/admin/vod/videoVisitor")
public class VideoVisitorController {

    @Autowired
    private VideoVisitorService videoVisitorService;

    @ApiOperation("课程统计")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@ApiParam(value = "课程id", required = true) @PathVariable Long courseId,
                            @ApiParam(value = "开始时间", required = true) @PathVariable @DateTimeFormat(pattern = "yyyy-HH-dd") String startDate,
                            @ApiParam(value = "结束时间", required = true) @PathVariable @DateTimeFormat(pattern = "yyyy-HH-dd") String endDate) {
        Map<String, Object> map = videoVisitorService.findCount(courseId, startDate, endDate);
        return Result.ok(map);
    }


}

