package com.qsl.ggktparent.order.api;


import com.qsl.ggktparent.model.order.OrderInfo;
import com.qsl.ggktparent.order.service.OrderInfoService;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.order.OrderFormVo;
import com.qsl.ggktparent.vo.order.OrderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "支付订单")
@RestController
@RequestMapping("api/order/orderInfo")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("根据微信用户id生成课程订单")
    @PostMapping("submitOrder")
    public Result submitOrder(@ApiParam("订单实体类") @RequestBody OrderFormVo orderFormVo,
                              @ApiParam("Http请求") HttpServletRequest req) {
        System.out.println("根据微信用户id生成课程订单");
        Long orderId = orderInfoService.submitOrder(orderFormVo, req);
        return Result.ok(orderId);
    }

    @ApiOperation("根据订单id查询订单信息")
    @GetMapping("getInfo/{orderId}")
    public Result getInfo(@ApiParam("订单id") @PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        return Result.ok(orderInfo);
    }


}
