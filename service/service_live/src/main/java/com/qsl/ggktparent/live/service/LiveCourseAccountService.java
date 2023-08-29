package com.qsl.ggktparent.live.service;

import com.qsl.ggktparent.model.live.LiveCourseAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
public interface LiveCourseAccountService extends IService<LiveCourseAccount> {

    // 获取直播账号信息
    LiveCourseAccount getLiveCourseAccountById(Long liveCourseId);
}
