package com.qsl.ggktparent.wechat.service;

import com.qsl.ggktparent.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-22
 */
public interface MenuService extends IService<Menu> {

    // 查询-树形结构-懒加载
    List<MenuVo> findMenuInfo();

//    查询所有一级菜单-下拉框
    List<Menu> findOneMenuInfo();

    // 同步微信公众号菜单
    Boolean syncMenu();
}
