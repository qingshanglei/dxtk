package com.qsl.ggktparent.order.api;

import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.order.service.OrderInfoService;
import com.qsl.ggktparent.order.service.WXPayServeice;
import com.qsl.ggktparent.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/order/wxPay")
public class WxPayController {

    @Autowired
    private WXPayServeice wxPayServeice;
    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("微信支付") // 调用微信支付接口实现微信支付 outTradeNo
    @GetMapping("createJsapi/{orderNo}")
    public Result createJsapi(@ApiParam("订单交易编号") @PathVariable String orderNo) {
        Map<String, Object> map = wxPayServeice.createJsapi(orderNo);
        return Result.ok(map);
    }


    @ApiOperation("查询微信支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public Result queryPayStatus(@ApiParam("订单No") @PathVariable String orderNo) {
        // 根据订单号调用微信接口查询支付状态
        Map<String, String> resultMap = wxPayServeice.queryPayStatus(orderNo);
        // 判断支付是否成功： 根据微信支付状态接口判断
        if (resultMap == null) {
            throw new BusinessException(20001, "订单未支付");
        } else if (resultMap.get("trade_state").equals("SUCCESS")) { // 已支付
            // 更新订单状态：已支付
            Object outTradeNo = resultMap.get("out_trade_no");
            orderInfoService.updateOrderStatus(outTradeNo);

            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }
}
