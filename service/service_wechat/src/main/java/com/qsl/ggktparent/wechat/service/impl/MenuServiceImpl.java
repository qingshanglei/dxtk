package com.qsl.ggktparent.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.wechat.Menu;
import com.qsl.ggktparent.vo.wechat.MenuVo;
import com.qsl.ggktparent.wechat.mapper.MenuMapper;
import com.qsl.ggktparent.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-22
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired // 注入微信公众号
    private WxMpService wxMpService;

    // 查询所有菜单-树形结构（本次使用stream流方式）
    @Override
    public List<MenuVo> findMenuInfo() {
        // 查询所有数据
        List<Menu> menuList = baseMapper.selectList(null);
//        // stream流过滤器， 获取parentid==0， 第一层数据
        List<Menu> oneMenuList = menuList.stream().filter(menu -> menu.getParentId().longValue() == 0).collect(Collectors.toList());
        // 封装返回数据对象
        List<MenuVo> list = new ArrayList<>();

        for (Menu oneMenu : oneMenuList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu, menuVo); // oneMenu对象数据复制到menuVo
            // 获取第二层数据
            List<Menu> twoMenuList = menuList.stream().filter(menu -> menu.getParentId().longValue() == oneMenu.getId()).collect(Collectors.toList());

            List<MenuVo> children = new ArrayList<>();
            for (Menu twoMenu : twoMenuList) {
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo); // twoMenu对象数据复制到twoMenuVo
                // 返回第二层数据
                children.add(twoMenuVo);
            }
            // 返回第二层数据
            menuVo.setChildren(children);
            // 返回第一层数据
            list.add(menuVo);
        }
        return list;
    }

    //    获取所有一级菜单-下拉框
    @Override
    public List<Menu> findOneMenuInfo() {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.like(Menu::getParentId, 0);
        return baseMapper.selectList(lqw);
    }


    // TODO  同步微信公众号菜单--->获取数据库数据，封装并返回指定格式数据给微信API
//    @SneakyThrows // @SneakyThrows注解 将方法内抛出的受检查异常转化为不受检查异常并抛出,替代try-catch块
    @Override
    public Boolean syncMenu() {
        // 查询所有数据-树形结构
        List<MenuVo> menuVoList = this.findMenuInfo();
        // 封装button里面结构，数据格式
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
//            json对象 一级菜单
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());// 获取菜单名称
            // josn数组 二级菜单
            JSONArray subButton = new JSONArray();
            // 遍历二级目录
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType()); // 获取菜单类型
                if (twoMenuVo.getType().equals("view")) { //
                    view.put("name", twoMenuVo.getName());
//                    view.put("url", "http://22e89868.r12.cpolar.top/#" + twoMenuVo.getUrl());// 前台项目路径 注意：带#号的必须拼接，否则会自动在最后面拼接。
                    view.put("url", "http://22e89868.r12.cpolar.top" + twoMenuVo.getUrl());
                    log.error("=======sdfsdf =>" + view.getString("url")); // http://22e89868.r12.cpolar.top/course/14
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey()); // 菜单key,用于消息接口推送
                }
                subButton.add(view);
            }
            one.put("sub_button", subButton); //封装二级数据
            buttonList.add(one);//封装一级数据
        }
        // 封装最外层button部分
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        try {
            // 调用微信post请求    注意：此处weixin-java-mp依赖默认获取了微信公众号access_token。
            String menuId = this.wxMpService.getMenuService().menuCreate(button.toString());
            System.out.println("====++>menuId: " + menuId);
            return true;
        } catch (Exception e) {
            throw new BusinessException(20001, "同步微信公众号菜单栏数据失败");
        }
    }
}
