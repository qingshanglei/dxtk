package com.qsl.ggktparent.activity.service;

import com.qsl.ggktparent.model.activity.CouponUse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券领用表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
public interface CouponUseService extends IService<CouponUse> {

    // 更新优惠券使用状态
    Boolean updateStatusById(Long  couponId,Long orderId);
}
