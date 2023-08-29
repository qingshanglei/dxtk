package com.qsl.ggktparent.vod.service;

import com.qsl.ggktparent.model.vod.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qsl.ggktparent.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
public interface ChapterService extends IService<Chapter> {

    // 查询大纲列表（章节，小节列表）
    List<ChapterVo> getNestedTreeList(String courseId);

    // 根据课程id删除章节
    void removeChapterByCourseId(Long courseId);

    // 删除章节 及其小节和 腾讯云视频
    boolean removeChapterById(String id);

    // 根据课程id查询章节名称
    List<Chapter> getChapterByCourseId(Long courseId);
}
