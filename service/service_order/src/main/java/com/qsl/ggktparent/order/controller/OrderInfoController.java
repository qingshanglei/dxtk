package com.qsl.ggktparent.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.order.OrderDetail;
import com.qsl.ggktparent.model.order.OrderInfo;
import com.qsl.ggktparent.order.service.OrderDetailService;
import com.qsl.ggktparent.order.service.OrderInfoService;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.vo.order.OrderInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Api(tags = "订单管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderDetailService orderDetailService;

    @ApiOperation("条件分页查询订单")
    @GetMapping("{page}/{limit}")
    public Result PageOrderInfo(@ApiParam(value = "当前页面", required = true) @PathVariable Long page,
                                @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                                @ApiParam(value = "条件查询对象", required = false) OrderInfoQueryVo orderInfoQueryVo) {

        Page<OrderInfo> pageParam = new Page<>(page, limit);
        Map<String, Object> map = orderInfoService.findPageOrderInfo(pageParam, orderInfoQueryVo);
        return Result.ok(map);
    }

    @ApiOperation("根据id查询订单表")
    @GetMapping("getOrderInfo/{orderId}")
    public OrderInfo getOrderInfo(@ApiParam("订单id") @PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        return orderInfo;
    }

    @ApiOperation("根据订单交易编号查询订单")
    @GetMapping("getOrderDetailById/{orderId}")
    public Map<String, Object> getOrderDetailAndInfoById(@ApiParam("订单交易编号") @PathVariable("orderId") Long orderId) {
        Map<String, Object> map = orderDetailService.getOrderDetailAndInfoById(orderId);
        return map;
    }


}

