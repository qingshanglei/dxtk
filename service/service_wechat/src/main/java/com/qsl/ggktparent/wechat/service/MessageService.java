package com.qsl.ggktparent.wechat.service;

import java.util.Map;

public interface MessageService {

    // 接收微信服务器发送来的消息-并返回消息
    String receiveMessage(Map<String, String> parseXml);

    // 模板消息
    Boolean pushPayMessage(Long orderId);
}
