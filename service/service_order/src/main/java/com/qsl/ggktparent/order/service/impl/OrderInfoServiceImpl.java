package com.qsl.ggktparent.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.client.activity.ActivityInfoFeignClient;
import com.qsl.ggktparent.client.user.UserInfoFeignClient;
import com.qsl.ggktparent.client.vod.CourseFeignClient;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.activity.CouponInfo;
import com.qsl.ggktparent.model.order.OrderDetail;
import com.qsl.ggktparent.model.order.OrderInfo;
import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.model.vod.Course;
import com.qsl.ggktparent.order.mapper.OrderInfoMapper;
import com.qsl.ggktparent.order.service.OrderDetailService;
import com.qsl.ggktparent.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.utils.AuthContextHolder;
import com.qsl.ggktparent.utils.OrderNoUtils;
import com.qsl.ggktparent.vo.order.OrderFormVo;
import com.qsl.ggktparent.vo.order.OrderInfoQueryVo;
import com.qsl.ggktparent.vo.order.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private ActivityInfoFeignClient activityInfoFeignClient;

    //    条件分页查询
    @Override
    public Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        Long userId = orderInfoQueryVo.getUserId();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String phone = orderInfoQueryVo.getPhone();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();

        LambdaQueryWrapper<OrderInfo> lqw = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(userId)) { // 参数不为空
            lqw.like(OrderInfo::getUserId, userId);
        }
        if (!StringUtils.isEmpty(outTradeNo)) { // 参数不为空
            lqw.like(OrderInfo::getOutTradeNo, outTradeNo);
        }
        if (!StringUtils.isEmpty(phone)) { // 参数不为空
            lqw.like(OrderInfo::getPhone, phone);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) { // 参数不为空
            lqw.ge(OrderInfo::getCreateTime, createTimeEnd);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) { // 参数不为空
            lqw.le(OrderInfo::getCreateTime, createTimeBegin);
        }
        if (!StringUtils.isEmpty(orderStatus)) { // 参数不为空
            lqw.like(OrderInfo::getOrderStatus, orderStatus);
        }

        // 条件分页查询
        Page<OrderInfo> pages = baseMapper.selectPage(pageParam, lqw);
        long totalCount = pages.getTotal(); // 每页记录数
        long pageCount = pages.getPages(); // 当前页面
        List<OrderInfo> records = pages.getRecords();
        // 订单里面包含详情内容，封装详情数据，根据订单id查询详情
        records.stream().forEach(item -> {
            this.getOrderDetail(item);
        });

        //所有需要数据封装map集合，最终返回
        Map<String, Object> map = new HashMap<>();
        map.put("total", totalCount);
        map.put("pageCount", pageCount);
        map.put("records", records);
        System.out.println("==++++========+++++====" + map.get("records"));
        return map;
    }

    // 根据微信用户id生成课程订单
    @Override
    public Long submitOrder(OrderFormVo orderFormVo, HttpServletRequest req) {
        // 1.判断该课程是否已购买
        Long userId = AuthContextHolder.getUserId(); // 用户id
        LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
        lqw.like(OrderDetail::getUserId, userId);
        OrderDetail orderDetail = orderDetailService.getOne(lqw);
        if (orderDetail != null) {
            throw new BusinessException(20001, "已购买过该课程");
        }

        // 2.根据课程id判断课程是否存在
        Long courseId = orderFormVo.getCourseId();
        Course course = courseFeignClient.getCourseByCourseId(courseId);
        if (course == null) {
            throw new BusinessException(20001, "课程不存在");
        }

        // 3.根据用户id判断用户是否存在
        UserInfo userInfo = userInfoFeignClient.getById(1L);
        if (userInfo == null) {
            throw new BusinessException(20001, "用户不存在");
        }

        Long couponId = orderFormVo.getCouponId(); // 优惠卷id
        BigDecimal bigDecimal = new BigDecimal(1.00);
        if (couponId != null) {
            // 根据优惠卷id判断优惠卷是否存在
            CouponInfo couponInfo = activityInfoFeignClient.getCouponInfoByCouponId(couponId);
            bigDecimal = couponInfo.getAmount(); // 优惠卷金额
        }

        // order_detail 订单明细
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setCourseId(orderFormVo.getCourseId()); // 课程id


        //  4.order_info生成订单
        // 4.1 封装订单数据， 现在少了session_id，地区id
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(1L);
        orderInfo.setNickName(userInfo.getNickName());// 用户昵称
        orderInfo.setPhone(userInfo.getPhone());// 电话
        orderInfo.setOriginAmount(course.getPrice());// 课程原始金额
        orderInfo.setCouponReduce(bigDecimal); // 优惠卷金额
        log.error("===>" + orderInfo.getOriginAmount() + "====" + orderInfo.getCouponReduce());
//        orderDetail.setFinalAmount(orderInfo.getOriginAmount() - orderInfo.getCouponReduce()); //课程最终金额: 原始金额 - 优惠券金额
        BigDecimal subtract = orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce());
//        orderDetail.setFinalAmount(subtract); //课程最终金额: 原始金额 - 优惠券金额
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo()); // 订单交易编号
        orderInfo.setTradeBody(course.getTitle());// 订单描述
        baseMapper.insert(orderInfo);

        // 5.更新优惠卷状态:  优惠卷id，订单id
        if (orderFormVo.getCouponUseId() != null) {
            activityInfoFeignClient.updateCouponById(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        Long orderInfoId = orderInfo.getId();
        log.error("返回订单id:" + orderInfoId);

        // 返回订单id
        return orderInfoId;
    }

    // 更新订单状态：已支付
    @Override
    public void updateOrderStatus(Object outTradeNo) {


    }

    //查询课程名称
    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        // 根据订单id，查询订单详情
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());
        if (orderDetail != null) {
            orderInfo.getParam().put("courseName", orderDetail.getCourseName());
        }
        return orderInfo;
    }
}
