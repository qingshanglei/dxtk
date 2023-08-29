package com.qsl.ggktparent.user.service;

import com.qsl.ggktparent.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-18
 */
public interface UserInfoService extends IService<UserInfo> {

    // 根据OpenId查询
    UserInfo getbyOpenId(String openId);
}
