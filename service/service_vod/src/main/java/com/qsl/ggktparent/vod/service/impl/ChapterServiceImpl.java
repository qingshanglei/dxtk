package com.qsl.ggktparent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsl.ggktparent.model.vod.Chapter;
import com.qsl.ggktparent.model.vod.Video;
import com.qsl.ggktparent.vo.vod.ChapterVo;
import com.qsl.ggktparent.vo.vod.VideoVo;
import com.qsl.ggktparent.vod.mapper.ChapterMapper;
import com.qsl.ggktparent.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.vod.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    // 查询大纲列表（章节，小节列表）
    @Override
    public List<ChapterVo> getNestedTreeList(String courseId) {
        // 最终返回数据
        List<ChapterVo> finalChapterList = new ArrayList<>();

        // 根据courseId查询大纲-章节
        LambdaQueryWrapper<Chapter> lqwChapter = new LambdaQueryWrapper<>();
        lqwChapter.eq(Chapter::getCourseId, courseId);
        lqwChapter.orderByAsc(Chapter::getSort, Chapter::getId);
        // 根据查询章节
        List<Chapter> chapterList = baseMapper.selectList(lqwChapter);

        // 根据courseId查询大纲-小节
        LambdaQueryWrapper<Video> lqwVideoVo = new LambdaQueryWrapper<>();
        lqwVideoVo.eq(Video::getCourseId, courseId);
        List<Video> videoList = videoService.list(lqwVideoVo);

        // 遍历所有章节
        for (Chapter chapter : chapterList) {
            // chapter对象数据复制到 chapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            // 添加章节返回数据
            finalChapterList.add(chapterVo);

            // 章节里封装小节
            // 创建List集合-封装章节所有小节
            List<VideoVo> videoVoList = new ArrayList<>();
            for (Video video : videoList) {
                //
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    // video对象数据复制到videoVo
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
                // 添加小节返回数据
                chapterVo.setChildren(videoVoList);
            }
        }
        return finalChapterList;
    }

    // 根据课程id删除章节
    @Override
    public void removeChapterByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> lqw = new LambdaQueryWrapper<>();
        lqw.like(Chapter::getCourseId, courseId);
        baseMapper.delete(lqw);
    }

    // 删除章节 及其小节和 腾讯云视频
    @Override
    public boolean removeChapterById(String id) {
        // 查询章节里所有的小节
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
        lqw.like(Video::getChapterId, id);
        List<Video> videoList = videoService.list(lqw);

        // 获取小节
        for (Video video : videoList) {
            // 获取小节所有视频id不为空的，删除
            if (!StringUtils.isEmpty(video.getId())) {
                videoService.removeVideoById(video.getId().toString());
            }
        }

        // 删除章节
        int flag = baseMapper.deleteById(id);
        return flag > 0;
    }

    // 根据课程id查询章节名称
    @Override
    public List<Chapter> getChapterByCourseId(Long courseId) {
        LambdaQueryWrapper<Chapter> lqw = new LambdaQueryWrapper<Chapter>().like(Chapter::getCourseId, courseId);
        List<Chapter> chapterList = baseMapper.selectList(lqw);
        return chapterList;
    }


}
