package com.qsl.ggktparent.vod.api;

import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "腾讯云(VOD)视频点播")
@RestController
@RequestMapping("api/vod")
public class VodApiController {


    @Autowired
    private VideoService videoService;


    @ApiOperation("根据小节id 获取云端视频资源id和SubAppId 播放视频")
    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(@ApiParam("小节,视频id") @PathVariable Long videoId) {
        return Result.ok(videoService.getPlayAuth(videoId));
    }

}
