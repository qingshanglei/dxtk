package com.qsl.ggktparent.activity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.model.activity.CouponInfo;

import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
public interface CouponInfoService extends IService<CouponInfo> {



    // 条件分页查询-优惠卷详情
    Map<String, Object> selectCouponUsePage(Long id, Long page, Long limit);

}
