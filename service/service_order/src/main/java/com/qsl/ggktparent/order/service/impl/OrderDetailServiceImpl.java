package com.qsl.ggktparent.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.order.OrderDetail;
import com.qsl.ggktparent.model.order.OrderInfo;
import com.qsl.ggktparent.order.mapper.OrderDetailMapper;
import com.qsl.ggktparent.order.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderDetailService orderDetailService;

    //    根据订单交易编号查询订单
    @Override
    public Map<String, Object> getOrderDetailAndInfoById(Long orderId) {
        // 根据订单交易编号查询订单
        LambdaQueryWrapper<OrderInfo> lqw = new LambdaQueryWrapper<>();
        lqw.like(OrderInfo::getOutTradeNo, orderId);
        OrderInfo orderInfo = orderInfoService.getOne(lqw);

        // 根据订单id查询订单
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        map.put("courseName", orderDetail.getCourseName()); //课程名称
        map.put("outTradeNo", orderInfo.getOutTradeNo()); //订单编号
        map.put("finalAmount", orderInfo.getFinalAmount()); // 支付金额
        map.put("createTime", orderInfo.getCreateTime()); // 支付时间
        return map;
    }
}
