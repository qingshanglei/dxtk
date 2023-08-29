package com.qsl.ggktparent.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.qsl.ggktparent.client.vod.CourseFeignClient;
import com.qsl.ggktparent.utils.Result;
import com.qsl.ggktparent.utils.SHA1;
import com.qsl.ggktparent.wechat.service.MenuService;
import com.qsl.ggktparent.wechat.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号
 */
@Slf4j
@Api(tags = "微信公众号消息")
@RestController
@RequestMapping("/api/wechat/message") // 对应者微信公众号接口配置
public class MessageController {


    @Autowired
    private MessageService messageService;

    public static final String token = "ggkt";

    @ApiOperation("验证消息的确来自微信服务器") // 服务器有效性验证
    @GetMapping
    public String verifyToken(HttpServletRequest request) {
        String signature = request.getParameter("signature"); // 获取微信加密签名，包含了token参数和请求中signature参数、nonce参数。
        String timestamp = request.getParameter("timestamp"); // 时间戳
        String nonce = request.getParameter("nonce"); // 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        System.out.println("==> signature" + signature);
        log.info("====> signature: {} nonce: {} echostr: {} timestamp: {}", signature, nonce, echostr, timestamp);

        if (this.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return echostr;
    }

    /**
     * echostr	随机字符串
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return
     */
    private Boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] str = {token, timestamp, nonce}; // 定义数组
        // 排序
        Arrays.sort(str);
        // 拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        // sha1加密
        String temp = SHA1.encode(buffer.toString());
        // 已微信的signature进行匹对
        return signature.equals(temp);
    }

    @ApiOperation("接收微信服务器发送来的消息-并返回xml格式消息")
    @PostMapping
    public String receiveMessage(HttpServletRequest request) throws Exception {
        // 获取消息
//        WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(request.getInputStream());
//        System.out.println("=接收微信服务器发送来的消息xml格式==>" + JSONObject.toJSONString(wxMpXmlMessage));

        // 获取消息
        Map<String, String> parseXml = this.parseXml(request);
        String str = messageService.receiveMessage(parseXml);
        System.out.println("=接收微信服务器发送来的消息==>" + str);

        return str;
    }

    // xml
    private Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 返回结果
        Map<String, String> map = new HashMap<>();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        List<Element> elementList = root.elements();
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());//名称，标题
        }
        inputStream.close(); // 释放资源
        return map;
    }


    @ApiOperation("模板消息")
    @GetMapping("/pushPayMessage/{orderId}")
    public Result pushPayMessage(@ApiParam("订单交易编号") @PathVariable Long orderId) {
        Boolean flag = messageService.pushPayMessage(orderId);
        return Result.ok(flag);
    }
}
