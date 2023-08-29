package com.qsl.ggktparent.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.order.service.WXPayServeice;
import com.qsl.ggktparent.utils.HttpClient;
import io.lettuce.core.protocol.DemandAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WXPayServeiceImpl implements WXPayServeice {

    @Value("${wechat.mpAppId}")
    private String wxAppid; // 公众账号ID（正式号）
    @Value("${wechat.merchant}")
    private String merchant; // 微信支付，商户号
    @Value("${wechat.merchantKey}")
    private String merchantKey; // 微信支付，商户key

    //    下单，微信支付
    @Override
    public Map createJsapi(String orderNo) {  // appid=wxf913bfa3a2c7eeeb
        // 1.封装微信支付需要参数，使用map集合
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", wxAppid); // 公众账号ID
        paramMap.put("mch_id", merchant); // 微信支付，服务号商家id    注意：这里本通过微信获取的，但申请的测试号无权限，只能写死了。
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());  // 随机字符串，不长于32位
        paramMap.put("body", "test");  // 商品描述:订单弹出
        paramMap.put("out_trade_no", orderNo); // 商户订单号
        paramMap.put("total_fee", "1"); // 支付金额,单位为分，  此处0.01
        paramMap.put("spbill_create_ip", "127.0.0.1"); // 用户终端IP
        paramMap.put("notify_url", "http://glkt.atguigu.cn/api/order/wxPay/notify"); // 后端项目接口  接收微信支付结果通知的URL地址，用于接收支付成功或失败的回调信息。在用户发起支付请求后，微信服务器会将支付结果异步发送到该URL地址，商户需要对接收到的支付结果进行处理，例如生成订单、更新订单状态等。通常情况下，notify_url由商户在微信支付平台上进行配置，并保证能够正确响应微信服务器的请求
        paramMap.put("trade_type", "JSAPI");//支付类型： ①付款码支付，②JSAPI支付，③Native支付，④App支付，⑤H5支付，⑥小程序支付，⑦刷脸支付
        /** 根据参数值获取当前微信用户openid
         *   实现逻辑： ①根据订单号获取userid ②根据userid获取openid
         *
         *   注意：因为当前使用测试号，测试号不支持支付功能，为了使用正式服务号进行测试，使用接口 ，调用尚硅谷数据库获取 正式服务号微信openid
         */
        paramMap.put("openid", "oQK5P5uTS5BxR2dH3xkcagIfidnA"); // 支付人微信oppenid

        try {
            // 2.通过httpclient调用微信支付接口
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");  // HttpClientUtils
            // 转成xml格式：参数，商户号Key
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, merchantKey); // 商户号Key
            client.setXmlParam(xmlParam);
            client.setHttps(true);  // 注意：API请求必须使用HTTPS
            client.post(); // post请求

            // 3.微信支付接口返回的数据: 返回的是xml数据
            String xml = client.getContent();
            System.out.println("xml: " + xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            // 判断是否支付成功
            if (resultMap.get("result_code") == null && !resultMap.get("result_code").equals("SUCCESS")) {
                System.out.println("error1" + resultMap);
                throw new BusinessException(20001, "微信支付异常");
            }

            // 调用微信，封装参数数据
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", wxAppid);// 公众号id
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));  // 随机字符串，不长于32位
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5"); // 签名类似： ①MD5(默认)  ②HMAC-SHA256
            parameterMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            String sign = WXPayUtil.generateSignature(parameterMap, merchantKey); //签名

            // 封装参数数据
            Map<String, Object> result = new HashMap<>();
            result.put("appId", wxAppid);
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");  // 微信签名方式
            result.put("paySign", sign);  // 微信签名
            result.put("package", packages);

            System.out.println("result:  " + result);
            return result;
        } catch (Exception e) {
            throw new BusinessException(20001, "微信支付异常");
        }
    }


    // 根据订单号调用微信接口查询支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        // 封装微信接口参数
        Map paramMap = new HashMap();
        paramMap.put("appid", wxAppid); // 微信公众号(服务号/正式号)服务id
        paramMap.put("mch_id", merchant); // 微信支付，服务号商家id
        paramMap.put("out_trade_no", orderNo);  // 商户订单号
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串，不长于32位

        try {
            // 调用微信接口
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            httpClient.setHttps(true);  // 注意：API请求必须使用HTTPS
            httpClient.post();

            // 封装返回结果
            String xml = httpClient.getContent();
            log.error("xml: ", xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            return resultMap;
        } catch (Exception e) {
            throw new BusinessException(20001, "查询微信支付状态失败");
        }
    }
}
