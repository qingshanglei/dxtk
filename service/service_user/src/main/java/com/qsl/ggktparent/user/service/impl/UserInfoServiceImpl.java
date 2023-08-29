package com.qsl.ggktparent.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.user.mapper.UserInfoMapper;
import com.qsl.ggktparent.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-18
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    // 根据OpenId查询
    @Override
    public UserInfo getbyOpenId(String openId) {
        LambdaQueryWrapper<UserInfo> lqw = new LambdaQueryWrapper<>();
        lqw.like(UserInfo::getOpenId, openId);
        System.out.println("22222");
//        UserInfo userInfo = baseMapper.selectById(lqw); // 这个接口不行
      //  查询此openId是否唯一。。。
        UserInfo userInfo = baseMapper.selectOne(lqw);
        return userInfo;
    }
}
