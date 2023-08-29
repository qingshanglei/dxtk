package com.qsl.ggktparent.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.client.order.OrderInfoFeignClient;
import com.qsl.ggktparent.client.vod.CourseFeignClient;
import com.qsl.ggktparent.model.order.OrderDetail;
import com.qsl.ggktparent.model.order.OrderInfo;
import com.qsl.ggktparent.model.vod.Course;
import com.qsl.ggktparent.wechat.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO 微信公众号
@Slf4j
@Service // bean
public class MessageServiceImpl implements MessageService {

    @Autowired // 服务调用
    private CourseFeignClient courseFeignClient;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    //    接收微信服务器发送来的消息-并返回消息
    @Override
    public String receiveMessage(Map<String, String> parseXml) {
        String content = "";
        String msgType = parseXml.get("MsgType"); // 获取消息类型， text(文本)，image/voice等。
        switch (msgType) { //消息是否为空并判断消息的类型
            case "text":  // 判断消息是否为普通文本类型
                content = this.search(parseXml);
                break;
            case "event": //关注、取消关注、点击关于我们
                String event = parseXml.get("Event"); //事件
                String evenKey = parseXml.get("EvenKey");
                log.error("事件:", event, evenKey);
                // 关注公众号
                if ("subscribe".equals(event)) {
                    content = this.subscribe(parseXml);
                } else if ("unsubscribe".equals(event)) {// 取消关注公众号
                    content = "success";
                } else if ("CLICK".equals(event)) { //关于我们   对应数据库：type和meun_key字段  老师是需要加：&& "aboutUs".equals(evenKey)
                    log.error("点击关于我们，返回指定消息222");
                    content = this.aboutUs(parseXml);
                } else {
                    content = "success";
                }
                break;
            default: // 其他情况
                content = "success";
        }
        return content;
    }

    // TODO 模板消息
    @SneakyThrows // @SneakyThrows: 将方法内抛出的受检查异常转化为不受检查异常并抛出,替代try-catch块
    @Override
    public Boolean pushPayMessage(Long orderId) {
        // 指定发送消息的用户
        String openid = "oQK5P5uTS5BxR2dH3xkcagIfidnA"; //对应公众号用户列表微信号
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid) // 消息推送到指定用户的id
                .templateId("pt2jq-ReAmvceeRjbyUO7NInxJSfF2imL85MicM19l0")// 模板id
                .url("http://4034ae16.r12.cpolar.top/order") //前台路径，点击模板消息访问的页面
                .build();


        Map<String, Object> order = orderInfoFeignClient.getOrderDetailAndInfoById(orderId);
        Object getId = order.get("getId");
        Object courseName = order.get("courseName");
        Object outTradeNo = order.get("outTradeNo");
        Object finalAmount = order.get("finalAmount");
        Object createTime = order.get("createTime"); //支付时间
        System.out.println("courseName" + courseName + "  outTradeNo:"
                + outTradeNo + "  finalAmount:" + finalAmount + "  createTime:" + createTime);
        // 配置要发送的消息:  对应参数，消息，
        templateMessage.addData(new WxMpTemplateData("first", "亲爱的用户：您有一笔订单支付成功。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", courseName.toString(), "#272727")); // 订单商品
        templateMessage.addData(new WxMpTemplateData("keyword2", outTradeNo.toString(), "#272727"));//订单编号
        templateMessage.addData(new WxMpTemplateData("keyword3", finalAmount.toString(), "#272727"));//支付金额
        templateMessage.addData(new WxMpTemplateData("keyword4", createTime.toString(), "#272727"));//支付时间
        templateMessage.addData(new WxMpTemplateData("remark", "感谢你购买课程，如有疑问，随时咨询！", "#272727"));

//        //发送消息
        String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

        System.out.println(msg);

        return true;
    }


    // 点击关于我们，返回指定消息
    private String aboutUs(Map<String, String> parseXml) {
        String text = this.text(parseXml, "硅谷课堂现开设Java、HTML5前端+全栈、大数据、" +
                "全链路UI/UE设计、人工智能、大数据运维+Python自动化、" +
                "Android+HTML5混合开发等多门课程；同时，通过视频分享、" +
                "谷粒学苑在线课堂、大厂学苑直播课堂等多种方式，" +
                "满足了全国编程爱好者对多样化学习场景的需求，" +
                "已经为行业输送了大量IT技术人才。");
        return text;
    }


    /**
     * 根据课程名查询课程,返回图文消息
     * 图文消息个数：当用户发送文本、图片、语音、视频、图文、地理位置这6种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     *
     * @param parseXml
     * @return
     */
    private String search(Map<String, String> parseXml) {
        String fromusername = parseXml.get("FromUserName");
        String tousername = parseXml.get("ToUserName"); //
        String content = parseXml.get("Content"); // 获取消息
        Long createTime = System.currentTimeMillis() / 1000L; // 消息创建时间，毫秒转秒
        StringBuffer text = new StringBuffer();
        // 根据课程名称查询课程
        List<Course> courseList = courseFeignClient.findByKeyword(content);
        System.out.println("根据课程名称查询课程: " + CollectionUtils.isEmpty(courseList));
        if (CollectionUtils.isEmpty(courseList)) {
            text.append(this.text(parseXml, "请重新输入关键字，没有匹配到相关视频课程"));
        } else {
            // 一次只能返回一个
            Random random = new Random();// 随机数
            int num = random.nextInt(courseList.size()); // 根据数组长度获取随机数
            Course course = courseList.get(num);
            StringBuffer articles = new StringBuffer();
            articles.append("<item>");
            articles.append("<Title><![CDATA[" + course.getTitle() + "]]></Title>"); // 课程名称
            articles.append("<Description><![CDATA[" + course.getTitle() + "]]></Description>"); // 课程名称
            articles.append("<PicUrl><![CDATA[" + course.getCover() + "]]></PicUrl>"); // 课程封面图片路径
            articles.append("<Url><![CDATA[http://glkt.atguigu.cn/#/liveInfo/" + course.getId() + "]]></Url>"); // 视频路径
            articles.append("</item>");

            //
            text.append("<xml>");
            text.append("<ToUserName><![CDATA[" + fromusername + "]]></ToUserName>"); //发送方帐号（一个OpenID）
            text.append("<FromUserName><![CDATA[" + tousername + "]]></FromUserName>"); //发送方帐号（一个OpenID）
            text.append("<CreateTime><![CDATA[" + createTime + "]]></CreateTime>"); //	消息创建时间 （整型）
            text.append("<MsgType><![CDATA[news]]></MsgType>"); //	消息类型，news(图文消息)，可以包括一篇或多篇文章，每篇文章包括标题、描述和图片等信息。
            text.append("<ArticleCount><![CDATA[1]]></ArticleCount>");// ArticleCount： 公众号已发布文章的总数
            text.append("<Articles>"); // 公众号最近发布的文章列表（包含文章标题、摘要、封面图片等）
            text.append(articles);
            text.append("</Articles>");
            text.append("</xml>");
        }
        return text.toString();
    }


    // 关注公众号，返回指定的消息
    private String subscribe(Map<String, String> parseXml) {
        return this.text(parseXml, "感谢你关注“硅谷课堂”，可以根据关键字搜索您想看的视频教程，如：JAVA基础、Spring boot、大数据等");
    }

    /**
     * 回复的消息(文本),转换为xml格式
     *
     * @param parseXml
     * @param content  消息
     * @return
     */
    private String text(Map<String, String> parseXml, String content) {
        System.out.println("回复的消息(文本): " + content);
        String fromUserName = parseXml.get("FromUserName");// 获取发送方帐号
        String toUserName = parseXml.get("ToUserName");// 获取开发者微信号
        // 获取当前时间戳（默认毫秒,转为为秒）
        long createTime = System.currentTimeMillis() / 1000L;
        StringBuffer text = new StringBuffer();
        text.append("<xml>");
        text.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");// 开发者微信号
        text.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");// 发送方帐号（一个OpenID）
        text.append("<CreateTime><![CDATA[" + createTime + "]]></CreateTime>");// 消息创建并发送时间
        text.append("<MsgType><![CDATA[text]]></MsgType>");// 消息类型: text(文本消息)，image(图片消息), voice(语音消息)，video(视频消息)，shortvideo(短视频消息)，location(地理位置消息)，link(链接消息)，enent(事件消息)。
        text.append("<Content><![CDATA[" + content + "]]></Content>"); // 消息内容
        text.append("</xml>"); //消息id，数库自动创建
        log.error("回复的消息", text);
        return text.toString();
    }

}
