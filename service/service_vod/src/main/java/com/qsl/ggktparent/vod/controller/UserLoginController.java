package com.qsl.ggktparent.vod.controller;

import com.qsl.ggktparent.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "登录接口")
@RestController
@RequestMapping(value = "/admin/vod/user")
@CrossOrigin // 解决跨域问题①-当前接口支持跨域
public class UserLoginController {

    @ApiOperation("登录")
    @PostMapping("login")
    public Result login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin-token");
        return Result.ok(map);
    }

    @ApiOperation("登录返回用户信息")
    @GetMapping("info")
    public  Result info(){
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "admin");
        map.put("introduction", "用户介绍");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name", "张楚岚");
        return Result.ok(map);
    }

}
