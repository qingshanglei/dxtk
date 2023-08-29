package com.qsl.ggktparent.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.activity.mapper.CouponInfoMapper;
import com.qsl.ggktparent.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.activity.service.CouponUseService;
import com.qsl.ggktparent.client.user.UserInfoFeignClient;
import com.qsl.ggktparent.model.activity.CouponInfo;
import com.qsl.ggktparent.model.activity.CouponUse;
import com.qsl.ggktparent.model.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-17
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponUseService couponUseService;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    // 条件分页查询-优惠卷详情
    @Override
    public Map<String, Object> selectCouponUsePage(Long id, Long page, Long limit) {
        Page<CouponInfo> pageParam = new Page<>(page, limit);
        // 根据id查询优惠卷
        CouponInfo couponInfo = baseMapper.selectById(id);

        // 查询优惠券领用表
        LambdaQueryWrapper<CouponUse> lqw = new LambdaQueryWrapper<>();
        lqw.like(CouponUse::getCouponId, id);
        List<CouponUse> couponUseList = couponUseService.list(lqw);

        Map<String, Object> map = new HashMap<>();
        map.put("couponInfo", couponInfo);
        map.put("CouponUse", couponUseList);

        // 根据用户id获取用户信息表
        couponUseList.stream().forEach(item -> {
            this.getUserInfoBycouponUse(item);
        });
        System.out.println("=====++++==>>>>>>>map" + map);
        return map;
    }



    // 根据用户id获取用户信息表
    private void getUserInfoBycouponUse(CouponUse couponUse) {
        if (couponUse.getUserId() != null) {
            // 根据用户id获取用户信息表
            UserInfo userInfo = userInfoFeignClient.getById(couponUse.getUserId());
            System.out.println(">>>>>>" + userInfo);
            if (userInfo != null) {
                String nickName = userInfo.getNickName();// 用户名
                String phone = userInfo.getPhone();//手机号
                couponUse.getParam().put("nickName", nickName);
                couponUse.getParam().put("phone", phone);
            }
        }
    }


}
