package com.qsl.ggktparent.live.api;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qsl.ggktparent.live.service.LiveCourseService;
import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.qsl.ggktparent.utils.AuthContextHolder;
import com.qsl.ggktparent.utils.JwtHelper;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.live.LiveCourseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "直播课程")
@RestController
@Slf4j
@RequestMapping("/api/live/liveCourse")
public class LiveCourseApiController {

    @Autowired
    private LiveCourseService liveCourseService;

    @ApiOperation("根据id查询课程")  // 直播课程表,直播课程描述表,教师表
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(@ApiParam("直播课程id") @PathVariable Long courseId) {
        System.out.println("根据id查询课程234");
        Map<String, Object> map = liveCourseService.getInfo(courseId);
        return Result.ok(map);
    }

    @ApiOperation("用户获取欢拓云直播access_token观看直播")
    @GetMapping("getLiveAccessToken/{LiveCourseId}")
    public Result getLiveAccessToken(@ApiParam("LiveCourseId") @PathVariable Long LiveCourseId) {
        System.out.println("AuthContextHolder.getUserId()::: ====123========》" + AuthContextHolder.getUserId());
        JSONObject data = liveCourseService.getLiveAccessToken(LiveCourseId, AuthContextHolder.getUserId());
        return Result.ok(data);
    }

    /**
     * /{page}/{limit}
     * @return page, limit
     * @ApiParam("当前页") @PathVariable Long page,
     * @ApiParam("每页记录数") @PathVariable Long limit
     */
    @ApiOperation("获取最近的直播信息") //
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> liveCourseVoList = liveCourseService.findLatelyList();
        return Result.ok(liveCourseVoList);
    }

}

