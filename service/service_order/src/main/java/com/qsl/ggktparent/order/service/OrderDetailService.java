package com.qsl.ggktparent.order.service;

import com.qsl.ggktparent.model.order.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
public interface OrderDetailService extends IService<OrderDetail> {

//    根据订单交易编号查询订单
    Map<String, Object> getOrderDetailAndInfoById(Long orderId);
}
