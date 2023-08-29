package com.qsl.ggktparent.live.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.live.service.LiveCourseAccountService;
import com.qsl.ggktparent.live.service.LiveCourseConfigService;
import com.qsl.ggktparent.live.service.LiveCourseGoodsService;
import com.qsl.ggktparent.live.service.LiveCourseService;
import com.qsl.ggktparent.model.live.LiveCourse;
import com.qsl.ggktparent.model.live.LiveCourseAccount;
import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.qsl.ggktparent.model.live.LiveCourseGoods;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.live.LiveCourseFormVo;
import com.qsl.ggktparent.vo.live.LiveCourseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Api(tags = "直播课程表")
@RestController
@RequestMapping("/admin/live/liveCourse")
public class LiveCourseController {

    @Autowired
    private LiveCourseService liveCourseService;
    @Autowired
    private LiveCourseAccountService liveCourseAccountService;
    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;

    @ApiOperation("分页查询直播课程")
    @GetMapping("pageLiveCourse/{page}/{limit}")
    public Result pageLiveCourse(@ApiParam("当前页") @PathVariable Long page,
                                 @ApiParam("每页记录数") @PathVariable Long limit) {
        Page<LiveCourse> pageParam = liveCourseService.pageLiveCourse(page, limit);
        return Result.ok(pageParam);
    }

    @ApiOperation("添加直播课程-欢拓云直播")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo) {
        boolean flag = liveCourseService.saveliveCourse(liveCourseFormVo);
        return Result.ok(flag);
    }

    @ApiOperation("删除直播课程") // （欢拓云直播） 直播课程表，直播账号表,直播课程描述表
    @DeleteMapping("remoteById/{liveCourseId}")
    public Result remoteById(@ApiParam("直播课程id") @PathVariable Long liveCourseId)  {
        Boolean flag = liveCourseService.remoteById(liveCourseId);
        return Result.ok(flag);
    }


    @ApiOperation("根据id查询直播课程")
    @GetMapping("getliveById/{liveCourseId}")
    public Result getliveById(@ApiParam("直播课程id") @PathVariable Long liveCourseId) {
        LiveCourse liveCourse = liveCourseService.getById(liveCourseId);
        return Result.ok(liveCourse);
    }

    @ApiOperation("根据id查询直播课程，直播课程描述") // 直播课程表，直播账号表,
    @GetMapping("getLiveAndDescriptionById/{liveCourseId}")
    public Result getLiveAndDescriptionById(@ApiParam("直播课程id") @PathVariable Long liveCourseId) {
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveAndDescriptionById(liveCourseId);
        return Result.ok(liveCourseFormVo);
    }


    @ApiOperation("根据id修改直播信息-欢拓云直播，直播课程表，直播课程描述表") // 直播课程表，直播账号表,直播课程描述表
    @PutMapping("updateliveCourse")
    public Result updateliveCourse(@ApiParam("直播实体类") @RequestBody LiveCourseFormVo liveCourseFormVo) {
        Boolean flag = liveCourseService.updateliveCourse(liveCourseFormVo);

        return Result.ok(flag);
    }

    @ApiOperation("获取直播账号信息")
    @GetMapping("getLiveCourseAccountById/{liveCourseId}")
    public Result<LiveCourseAccount> getliveAccountByliveCourseId(@ApiParam("直播课程id") @PathVariable Long liveCourseId) {
        LiveCourseAccount courseAccount = liveCourseAccountService.getLiveCourseAccountById(liveCourseId);
        return Result.ok(courseAccount);
    }




    @ApiOperation("查询所有商品")
    @GetMapping("getLiveCourseGoodsAll")
    public Result getLiveCourseGoodsAll() {
        List<LiveCourseGoods> goodsList = liveCourseGoodsService.list(null);
        return Result.ok(goodsList);
    }

    @ApiOperation("查询所有直播课程")
    @GetMapping("getLiveCourseAll")
    public Result getLiveCourseAll() {
        List<LiveCourse> courseList = liveCourseService.list(null);
        System.out.println("w查询所有直播课程: " + courseList);
        return Result.ok(courseList);
    }

}

