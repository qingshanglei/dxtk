package com.qsl.ggktparent.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.live.LiveCourseAccount;
import com.qsl.ggktparent.live.mapper.LiveCourseAccountMapper;
import com.qsl.ggktparent.live.service.LiveCourseAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Service
public class LiveCourseAccountServiceImpl extends ServiceImpl<LiveCourseAccountMapper, LiveCourseAccount> implements LiveCourseAccountService {

    // 获取直播账号信息
    @Override
    public LiveCourseAccount getLiveCourseAccountById(Long liveCourseId) {
        LiveCourseAccount courseAccount = baseMapper.selectById(liveCourseId);
        return courseAccount;
    }
}
