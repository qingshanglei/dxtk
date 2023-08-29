package com.qsl.ggktparent.client.activity;

import com.qsl.ggktparent.model.activity.CouponInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("service-activity") //服务调用
public interface ActivityInfoFeignClient {


    @ApiOperation("根据优惠卷id查询优惠卷信息-服务调用")
    @GetMapping("/api/activity/coupInfo/getCouponInfoByCouponId/{couponId}")
    CouponInfo getCouponInfoByCouponId(@ApiParam("优惠卷id") @PathVariable("couponId") Long couponId);

    @ApiOperation("根据优惠卷id修改-服务调用") // 更新优惠券使用状态
    @PutMapping("/api/activity/coupInfo/updateCouponById/{couponId}/{orderId}")
    Boolean updateCouponById(@ApiParam("优惠卷id") @PathVariable("couponId") Long couponId,
                             @ApiParam("订单id") @PathVariable("orderId") Long orderId);

}
