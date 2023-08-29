package com.qsl.ggktparent.activity.service.impl;

import com.qsl.ggktparent.model.activity.CouponUse;
import com.qsl.ggktparent.activity.mapper.CouponUseMapper;
import com.qsl.ggktparent.activity.service.CouponUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 优惠券领用表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Service
public class CouponUseServiceImpl extends ServiceImpl<CouponUseMapper, CouponUse> implements CouponUseService {

    //    更新优惠券使用状态
    @Override
    public Boolean updateStatusById(Long couponId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("2"); // 优惠卷状态：1：未使用 2：已使用
        couponUse.setUsedTime(new Date());// 使用时间
        return baseMapper.updateById(couponUse) > 0;
    }
}
