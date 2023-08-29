package com.qsl.ggktparent.activity.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.activity.service.CouponInfoService;
import com.qsl.ggktparent.activity.service.CouponUseService;
import com.qsl.ggktparent.model.activity.CouponInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "优惠卷接口")
@RestController
@RequestMapping("/api/activity/coupInfo")
public class CoupInfoApiController {
    @Autowired
    private CouponInfoService couponInfoService;
    @Autowired
    private CouponUseService couponUseService;

    @ApiOperation("根据优惠卷id查询优惠卷信息-服务调用")
    @GetMapping("getCouponInfoByCouponId/{couponId}")
    public CouponInfo getCouponInfoByCouponId(@ApiParam("优惠卷id") @PathVariable String couponId) {
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return couponInfo;
    }

    @ApiOperation("根据优惠卷id修改-服务调用") // 更新优惠券使用状态
    @PutMapping("updateCouponById/{couponId}/{orderId}")
    public Boolean updateCouponById(@ApiParam("优惠卷id") @PathVariable Long couponId,
                                    @ApiParam("订单id") @PathVariable Long orderId) {
        Boolean flag = couponUseService.updateStatusById(couponId, orderId);
        return flag;
    }

}
