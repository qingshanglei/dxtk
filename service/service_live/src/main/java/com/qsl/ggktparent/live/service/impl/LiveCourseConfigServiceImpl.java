package com.qsl.ggktparent.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.live.mtcloud.CommonResult;
import com.qsl.ggktparent.live.mtcloud.MTCloud;
import com.qsl.ggktparent.live.service.LiveCourseGoodsService;
import com.qsl.ggktparent.live.service.LiveCourseService;
import com.qsl.ggktparent.model.live.LiveCourse;
import com.qsl.ggktparent.model.live.LiveCourseAccount;
import com.qsl.ggktparent.model.live.LiveCourseConfig;
import com.qsl.ggktparent.live.mapper.LiveCourseConfigMapper;
import com.qsl.ggktparent.live.service.LiveCourseConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.model.live.LiveCourseGoods;
import com.qsl.ggktparent.vo.live.LiveCourseConfigVo;
import com.qsl.ggktparent.vo.live.LiveCourseGoodsView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 直播课程配置表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Service
public class LiveCourseConfigServiceImpl extends ServiceImpl<LiveCourseConfigMapper, LiveCourseConfig> implements LiveCourseConfigService {

    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;
    @Autowired
    private LiveCourseService liveCourseService;
    @Autowired
    private MTCloud mtCloudClient;

    // 获取直播配置信息
    @Override
    public LiveCourseConfig getLiveCourseConfig(Long liveCourseId) {
        LambdaQueryWrapper<LiveCourseConfig> lqw = new LambdaQueryWrapper<LiveCourseConfig>()
                .like(LiveCourseConfig::getLiveCourseId, liveCourseId);

        LiveCourseConfig liveCourseConfig = baseMapper.selectOne(lqw);
        return liveCourseConfig;
    }

    //    修改直播配置信息-欢拓云直播
    @Override
    public Boolean updateLiveCourseConfig(LiveCourseConfigVo liveCourseConfigVo) {
        // 1.修改直播配置表
        LiveCourseConfig liveCourseConfig = new LiveCourseConfig();
        BeanUtils.copyProperties(liveCourseConfigVo, liveCourseConfig); // liveCourseConfigVo 数据转到 liveCourseConfig
        if (liveCourseConfigVo.getId() == null) {
            baseMapper.insert(liveCourseConfig);
        } else {
            baseMapper.updateById(liveCourseConfig);
        }

        // 2.修改直播商品表
        LambdaQueryWrapper<LiveCourseGoods> lqw = new LambdaQueryWrapper<>();
        lqw.like(LiveCourseGoods::getLiveCourseId, liveCourseConfig.getLiveCourseId());
        liveCourseGoodsService.remove(lqw);
        if (!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())) { // 商品不为空
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());// 批量添加
        }

        // 3.修改直播平台
        Boolean flag = this.updateLiveConfig(liveCourseConfigVo);
        return flag;
    }

    // 3.修改直播平台方法
    private Boolean updateLiveConfig(LiveCourseConfigVo liveCourseConfigVo) {
        // #########获取封装欢拓云直播配置信息
        // 查询直播课程表
        LiveCourse liveCourse = liveCourseService.getById(liveCourseConfigVo.getLiveCourseId());
        //
        HashMap<Object, Object> options = new HashMap<>();
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode()); //界面模式 1全屏模式 0二分屏 2课件模式
        // 观看人数开关
        JSONObject number = new JSONObject();

        number.put("enable", liveCourseConfigVo.getNumberEnable());// 是否开启直播 0否 1是 观看人数
        options.put("number", number.toJSONString());
        // 商城是否开启
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());// 商城是否开启 0未开启 1开启
        number.put("type", liveCourseConfigVo.getStoreType());//1商品列表,2商城链接,3商城二维码
        options.put("store", number.toJSONString());
        // 获取商品列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if (!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for (LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView); // liveCourseGoods 数据转到 liveCourseGoodsView
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }

        // #########修改欢拓云直播配置信息
        try {
            /** 修改生活直播相关配置
             * course_id 		课程ID
             * options 		配置参数  */
            String res = mtCloudClient.courseUpdateLifeConfig(liveCourse.getCourseId().toString(), options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            // 判断是否修改成功，成功：0， 其他:失败
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) { //
                return true;
            }
        } catch (Exception e) {
            throw new BusinessException(20001, "修改配置信息失败");
        }
        return false;
    }


}
