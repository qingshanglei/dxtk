package com.qsl.ggktparent.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.qsl.ggktparent.wechat.config.WXPublicAccount;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.wechat.Menu;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.wechat.MenuVo;
import com.qsl.ggktparent.wechat.service.MenuService;
import com.qsl.ggktparent.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-22
 */

@Api(tags = "微信公众管理菜单")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("查询-树形结构")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> menu = menuService.findMenuInfo();
        return Result.ok(menu);
    }

    @ApiOperation("获取所有一级菜单-下拉框")
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo() {
        List<Menu> menuList = menuService.findOneMenuInfo();
        return Result.ok(menuList);
    }

    @ApiOperation("获取微信公众号access_token")
    @GetMapping("getAccessToken")
    public Result getAccessToken() {
        try {//get请求微信公众号接口 https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            //拼接请求地址
            StringBuffer buffer = new StringBuffer();
            buffer.append("https://api.weixin.qq.com/cgi-bin/token");
            buffer.append("?grant_type=client_credential");
            buffer.append("&appid=%s"); //获取appid
            buffer.append("&secret=%s"); //获取secret
            // 请求路径设置参数:appid,secret
            String url = String.format(buffer.toString(), WXPublicAccount.ACCESS_KEY_ID, WXPublicAccount.ACCESS_KEY_SECRET);
            //  发送http请求
            String tokenString = HttpClientUtils.get(url);
            //   将JSON字符串转为Object
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            // Json数据中获取access_token
            String access_token = jsonObject.getString("access_token");
            return Result.ok(access_token);
        } catch (Exception e) {
            throw new BusinessException(20001, "获取access_token失败");
        }
    }

    // 设置微信公众号菜单栏-数据库数据同步到微信公众号菜单
    @ApiOperation("设置、更新微信公众号菜单栏")
    @GetMapping("syncMenu")
    public Result syncMenu() {
        Boolean flag = menuService.syncMenu();
        return Result.ok(flag);
    }

    @ApiOperation("新增")
    @PostMapping("addtMenu")
    public Result addtMenu(@ApiParam(value = "微信菜单实体类", required = true) @RequestBody Menu menu) {
        boolean flag = menuService.save(menu);
        return Result.ok(flag);
    }

    @ApiOperation("根据id查询微信公众管理菜单")
    @GetMapping("getMenuById/{id}")
    public Result getMenuById(@ApiParam(value = "菜单id", required = true) @PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.ok(menu);
    }

    @ApiOperation("根据id修改")
    @PutMapping("updateMenuById")
    public Result updateMenuById(@ApiParam(value = "微信公众管理菜单", required = true) @RequestBody Menu menu) {
        boolean flag = menuService.updateById(menu);
        return Result.ok(flag);
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("removeById/{id}")
    public Result removeById(@ApiParam(value = "菜单id", required = true) @PathVariable Long id) {
        boolean flag = menuService.removeById(id);
        return Result.ok(flag);
    }

    @ApiOperation("批量删除")
    @DeleteMapping("removeByIds")
    public Result removeByIds(@ApiParam("菜单ids") @RequestBody List<Long> ids) {
        boolean flag = menuService.removeByIds(ids);
        return Result.ok(flag);
    }

}

