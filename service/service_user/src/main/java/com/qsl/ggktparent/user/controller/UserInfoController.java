package com.qsl.ggktparent.user.controller;


import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-18
 */
@Api(tags = "用户表")
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("根据用户id查询用户信息")
    @GetMapping("getById/{id}")
    public UserInfo getById(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        UserInfo userInfo = userInfoService.getById(id);
        return userInfo;
    }


}

