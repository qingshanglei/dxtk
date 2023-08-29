package com.qsl.ggktparent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.model.vod.*;
import com.qsl.ggktparent.vo.vod.ChapterVo;
import com.qsl.ggktparent.vo.vod.CourseFormVo;
import com.qsl.ggktparent.vo.vod.CoursePublishVo;
import com.qsl.ggktparent.vo.vod.CourseQueryVo;
import com.qsl.ggktparent.vod.mapper.CourseMapper;
import com.qsl.ggktparent.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-05-08
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ChapterService chapterService;

    // 条件分页查询-课程视频(点播管理)
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        // 获取条件值
        String title = courseQueryVo.getTitle(); // 名称
        Long subjectId = courseQueryVo.getSubjectId(); //二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId(); // 一级分类
        Long teacherId = courseQueryVo.getTeacherId(); // 讲师
        System.out.println("courseQueryVo:" + courseQueryVo);
        // 条件判断
        LambdaQueryWrapper<Course> lambdaQW = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(title)) { // 判断是否为空
            lambdaQW.like(Course::getTitle, title);
        }
        if (!StringUtils.isEmpty(subjectId)) { // 判断是否为空
            lambdaQW.eq(Course::getSubjectId, subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) { // 判断是否为空
            lambdaQW.eq(Course::getSubjectParentId, subjectParentId);
        }
        if (!StringUtils.isEmpty(teacherId)) { // 判断是否为空
            lambdaQW.eq(Course::getTeacherId, teacherId);
        }
        //  查询
        Page<Course> pages = baseMapper.selectPage(pageParam, lambdaQW);
        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数
        //每页数据集合
        List<Course> records = pages.getRecords();
        //遍历封装讲师和分类名称
        records.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });
        //封装返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("totalPage", totalPage);
        map.put("currentPage", currentPage);
        map.put("size", size);
        map.put("records", records);
        return map;
    }

    // 获取讲师和分类名称
    private Course getTeacherOrSubjectName(Course course) {
        // 根据讲师id查询讲师
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null) { // 讲师不为空
            course.getParam().put("teacherName", teacher.getName());
        }
        // 根据课程专业父级ID查询课程类别名称
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if (subjectOne != null) {
            course.getParam().put("subjectParentTitle", subjectOne.getTitle());
        }
        // 根据课程专业ID查询课程名称
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if (subjectTwo != null) {
            course.getParam().put("subjectTitle", subjectTwo.getTitle());
        }
        return course;
    }

    // 课程发布-添加课程基本信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        /* 课程基本信息表新增数据  */
        Course course = new Course();
        // courseFormVo对象信息复制到course
        BeanUtils.copyProperties(courseFormVo, course);
        courseMapper.insert(course);

        /* 保存课程详情信息  */
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription()); //课程简介
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.save(courseDescription);

        // 返回课程Id
        return course.getId();
    }

    // 根据id查询课程基本信息
    @Override
    public CourseFormVo getCourseInfoById(Long id) {
        //  查询课程基本信息
        Course course = courseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        // course对象数据复制到courseFormVo
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course, courseFormVo);
        //   查询课程描述表
        QueryWrapper<CourseDescription> qw = new QueryWrapper<>();
        qw.eq("course_id", id);
        CourseDescription courseDescription = courseDescriptionService.getOne(qw);
        if (courseDescription != null) {
            // 设置课程简介
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    // 根据id修改课程基本信息
    @Override
    public void updateCourseId(CourseFormVo courseFormVo) {
        Course course = new Course();
        // courseFormVo对象数据复制到course
        BeanUtils.copyProperties(courseFormVo, course);

        // 修改课程基本信息
        courseMapper.updateById(course);
        CourseDescription description = new CourseDescription();
        description.setId(course.getId()); // 设置课程描述id
        description.setDescription(courseFormVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //    根据课程id查询课程信息
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    // 根据课程id发布课程
    @Override
    public Boolean publishCourseById(Long id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(1); // 课程状态 0未发布 1已发布
        return baseMapper.updateById(course) > 0;
    }

    // 根据课程id删除课程
    @Override
    public int removeCourseById(Long courseId) {
        // 根据课程id删除小节
        videoService.removeVideoByCourseId(courseId);

        // 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        // 根据课程id删除课程描述
        courseDescriptionService.removeCourseDescriptionByCourseId(courseId);

        // 根据课程id删除课程课程
        return baseMapper.deleteById(courseId);
    }

    // 根据课程分类id查询课程列表
    @Override
    public Map<String, Object> getCourseBySubjectId(Long subjectParentId, Long page, Long limit) {
        Map<String, Object> map = courseMapper.getCourseBySubjectId(subjectParentId);

        return map;
    }

    // 根据课程id查询课程信息: 课程表，课程简介表，教师表，课程分类表，课程章节表，课程小节表
    @Override
    public Map<String, Object> getCourseById(String courseId) {
        // 课程浏览量加1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);

        // 根据课程id查询课程信息
        Map<String, Object> map = new HashMap<>();
        Course course1 = courseMapper.selectById(courseId); // 课程表
        CourseDescription description = courseDescriptionService.getById(courseId); // 课程简介表
        Teacher teacher = teacherService.getById(course1.getTeacherId()); // 教师表
        Subject subject = subjectService.getById(course1.getSubjectId()); // 课程分类表
        List<ChapterVo> ChapterOrVideoList = chapterService.getNestedTreeList(courseId);// 课程章节、小节表

        map.put("course", course1);
        map.put("description", description != null ? description.getDescription() : "");
        map.put("teacher", teacher);
        map.put("subject", subject);
        map.put("ChapterOrVideoList", ChapterOrVideoList);
        map.put("isBuy", true); //是否购买课程
        return map;
    }

    //    查询所有课程
    @Override
    public List<Course> getCourseAll() {
        List<Course> courseList = baseMapper.selectList(null);
        courseList.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });
        return courseList;
    }

}
