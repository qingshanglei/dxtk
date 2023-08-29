package com.qsl.ggktparent.client.order;

import com.qsl.ggktparent.model.order.OrderDetail;
import com.qsl.ggktparent.model.order.OrderInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("service-order") // 服务调用
public interface OrderInfoFeignClient {

    @ApiOperation("根据订单交易编号查询订单")
    @GetMapping("/admin/order/orderInfo/getOrderDetailById/{orderId}")
    Map<String, Object> getOrderDetailAndInfoById(@ApiParam("订单交易编号") @PathVariable("orderId") Long orderId);

    @ApiOperation("根据id查询订单表")
    @GetMapping("/admin/order/orderInfo/getOrderInfo/{orderId}")
    OrderInfo getOrderInfo(@ApiParam("订单id") @PathVariable("orderId") Long orderId);

}
