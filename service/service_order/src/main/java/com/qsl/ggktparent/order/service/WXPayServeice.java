package com.qsl.ggktparent.order.service;

import java.util.Map;

public interface WXPayServeice {

//    下单，微信支付:    调用微信支付接口实现微信支付
    Map createJsapi(String orderNo);

    // 根据订单号调用微信接口查询支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
