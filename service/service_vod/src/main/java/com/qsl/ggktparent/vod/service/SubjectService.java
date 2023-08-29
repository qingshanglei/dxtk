package com.qsl.ggktparent.vod.service;

import com.qsl.ggktparent.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 课程科目 服务类
 *
 * @author 青衫泪
 * @since 2023-05-07
 */
public interface SubjectService extends IService<Subject> {

    // 课程查询-树形结构_懒加载
    List<Subject> selectList(Long id);

    // 导出分类课程
    void exportData(HttpServletResponse response);

    //     导入课程
    void importDictData(MultipartFile file);

}
