package com.qsl.ggktparent.activity.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.activity.service.CouponInfoService;
import com.qsl.ggktparent.activity.service.CouponUseService;
import com.qsl.ggktparent.model.activity.CouponInfo;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.activity.CouponInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Api(tags = "优惠券信息")
@RestController
//@CrossOrigin // 支持跨域
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result getById(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                          @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                          @ApiParam(value = "条件查询对象", required = false) CouponInfo couponInfo

    ) {
        System.out.println("couponInfo?:  "+couponInfo);
        Page<CouponInfo> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<CouponInfo> lqw = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(couponInfo.getCouponName())) {
            lqw.like(CouponInfo::getCouponName, couponInfo.getCouponName());
        }
        if (!StringUtils.isEmpty(couponInfo.getCouponType())) {
            lqw.like(CouponInfo::getCouponType, couponInfo.getCouponType());
        }
        if (!StringUtils.isEmpty(couponInfo.getCreateTime())) {
            lqw.ge(CouponInfo::getCreateTime, couponInfo.getCreateTime()); //大于等于
        }
        if (!StringUtils.isEmpty(couponInfo.getExpireTime())) {
            lqw.le(CouponInfo::getExpireTime, couponInfo.getExpireTime());// 小于等于
        }
        Page<CouponInfo> pageModel = couponInfoService.page(pageParam, lqw);
        return Result.ok(pageModel);
    }

    @ApiOperation("新增")
    @PostMapping("add")
    public Result add(@RequestBody CouponInfo couponInfo) {
        boolean flag = couponInfoService.save(couponInfo);
        return Result.ok(flag);
    }

    @ApiOperation("根据id查询")
    @GetMapping("getById/{couponId}")
    public Result getById(@ApiParam(value = "优惠券信息id", required = true) @PathVariable String couponId) {
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return Result.ok(couponInfo);
    }

    @ApiOperation("修改")
    @PutMapping("update")
    public Result update(@RequestBody CouponInfo couponInfo) {
        boolean flag = couponInfoService.updateById(couponInfo);
        return Result.ok(flag);
    }

    @ApiOperation("删除")
    @DeleteMapping("delete/{id}")
    public Result delete(@ApiParam(value = "couponId", required = true) @PathVariable Long id) {
        boolean flag = couponInfoService.removeById(id);
        return Result.ok(flag);
    }

    @ApiOperation("批量删除")
    @DeleteMapping("deleteAll")
    public Result deleteAll(@ApiParam(value = "couponIds", required = true) @RequestBody List<String> couponIds) {
        boolean flag = couponInfoService.removeByIds(couponIds);
        return Result.ok(flag);
    }


    @ApiOperation("条件分页查询-优惠卷详情")
    @GetMapping("selectCouponUsePage/{id}/{page}/{limit}")
    public Result selectCouponUsePage(@ApiParam(value = "coupon_id", required = true) @PathVariable Long id,
                                      @ApiParam(value = "当前页", required = true) @PathVariable Long page,
                                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit) {
        Map<String, Object> map = couponInfoService.selectCouponUsePage(id, page, limit);

        return Result.ok(map);
    }


}

