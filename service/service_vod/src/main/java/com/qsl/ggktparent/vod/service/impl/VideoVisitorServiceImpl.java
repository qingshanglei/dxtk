package com.qsl.ggktparent.vod.service.impl;

import com.qsl.ggktparent.model.vod.VideoVisitor;
import com.qsl.ggktparent.vo.vod.VideoVisitorCountVo;
import com.qsl.ggktparent.vod.mapper.VideoVisitorMapper;
import com.qsl.ggktparent.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-13
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    // 课程统计
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        // 条件判断
        List<VideoVisitorCountVo> videoVisitorList = baseMapper.findCount(courseId, startDate, endDate);

        //创建map集合
        Map<String, Object> map = new HashMap<>();
        // 获取所有日期
        List<Date> dateList = videoVisitorList.stream().map(VideoVisitorCountVo::getJoinTime).collect(Collectors.toList());
        // 获取所有日期对应的数量
        List<Integer> countList = videoVisitorList.stream().map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());

        // 数据添加到map集合
        map.put("xData", dateList);
        map.put("yData", countList);
        return map;
    }
}
