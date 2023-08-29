package com.qsl.ggktparent.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.order.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.order.OrderFormVo;
import com.qsl.ggktparent.vo.order.OrderInfoQueryVo;
import com.qsl.ggktparent.vo.order.OrderInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
public interface OrderInfoService extends IService<OrderInfo> {

    // 条件分页查询
    Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo);

    // 根据微信用户id生成课程订单
    Long submitOrder(OrderFormVo orderFormVo, HttpServletRequest req);

    // 更新订单状态：已支付
    void updateOrderStatus(Object outTradeNo);
}
