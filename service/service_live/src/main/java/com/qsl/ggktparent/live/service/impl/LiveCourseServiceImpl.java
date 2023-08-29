package com.qsl.ggktparent.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qsl.ggktparent.client.user.UserInfoFeignClient;
import com.qsl.ggktparent.client.vod.CourseFeignClient;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.live.mtcloud.CommonResult;
import com.qsl.ggktparent.live.mtcloud.MTCloud;
import com.qsl.ggktparent.live.service.LiveCourseAccountService;
import com.qsl.ggktparent.live.service.LiveCourseDescriptionService;
import com.qsl.ggktparent.model.live.LiveCourse;
import com.qsl.ggktparent.live.mapper.LiveCourseMapper;
import com.qsl.ggktparent.live.service.LiveCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsl.ggktparent.model.live.LiveCourseAccount;
import com.qsl.ggktparent.model.live.LiveCourseDescription;
import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.model.vod.Teacher;
import com.qsl.ggktparent.utils.DateUtil;
import com.qsl.ggktparent.vo.live.LiveCourseFormVo;
import com.qsl.ggktparent.vo.live.LiveCourseVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author 青衫泪
 * @since 2023-06-04
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {

    @Autowired
    private CourseFeignClient courseFeignClientl;
    @Autowired
    private LiveCourseDescriptionService liveCourseDescriptionService;
    @Autowired
    private LiveCourseAccountService liveCourseAccountService;
    @Autowired
    private MTCloud mtCloudClient;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    // 分页查询直播课程
    @Override
    public Page<LiveCourse> pageLiveCourse(Long page, Long limit) {
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        // 查询直播课程
        Page<LiveCourse> liveCoursePage = baseMapper.selectPage(pageParam, null);
        List<LiveCourse> LiveCourseList = liveCoursePage.getRecords(); // 数据

        for (LiveCourse liveCourse : LiveCourseList) {
            // 根据教师id查询教师信息
            Teacher teacher = courseFeignClientl.getTeacherById(liveCourse.getTeacherId());
            liveCourse.getParam().put("teacherName", teacher.getName()); // 教师名称
            liveCourse.getParam().put("teacherLevel", teacher.getLevel()); // 教师头衔 1高级讲师 2首席讲师
        }
        return liveCoursePage;
    }

    //    添加直播-欢拓云直播(直播课程表,直播账号表,直播课程描述表)
    @Override
    public boolean saveliveCourse(LiveCourseFormVo liveCourseFormVo) {
        //LiveCourseFormVo --> LiveCourse
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo, liveCourse);
        // 根据id获取讲师信息
        Teacher teacher = courseFeignClientl.getTeacherById(liveCourseFormVo.getTeacherId());


        try {
            // 设置添加直播的值
            HashMap<Object, Object> options = new HashMap<>();
            options.put("scenes", 2); // /直播类型。1: 教育直播(默认)，2: 生活直播。 说明：根据平台开通的直播类型填写
            options.put("password", liveCourseFormVo.getPassword());

            /** 添加直播-欢拓云直播
             *   course_name 课程名称
             *   account 发起直播课程的主播账号
             *   start_time 课程开始时间,格式: 2015-01-10 12:00:00
             *   end_time 课程结束时间,格式: 2015-01-10 13:00:00
             *   nickname  昵称
             *   accountIntro 主播介绍
             *   options 其他参数
             */
            String res = mtCloudClient.courseAdd(liveCourse.getCourseName(),
                    teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options
            );

            System.out.println("res:" + res);
            // 欢拓云直播返回数据转成Json
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            // 判断欢拓云直播是否添加直播，添加成功数据添加进数据库
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) { // 成功

                /* 新增直播基本信息*/
                JSONObject data = commonResult.getData();
                Long course_id = data.getLong("course_id"); // 获取直播id
                liveCourse.setCourseId(course_id);
                baseMapper.insert(liveCourse);

                // 新增直播描述信息
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription()); //课程简介
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                liveCourseDescriptionService.save(liveCourseDescription);

                // 新增直播账号信息
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(data.getString("bid")); // 主播账号（欢拓系统的主播id）
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setZhuboKey(data.getString("zhubo_key"));// 主播登录秘钥
                liveCourseAccount.setAdminKey(data.getString("admin_key"));//助教/客服登录秘钥
                liveCourseAccount.setUserKey(data.getString("user_key"));// 观众/学生登录秘钥
                liveCourseAccountService.save(liveCourseAccount);

                return true;
            } else {
                System.out.println(commonResult.getmsg());
                throw new BusinessException(20001, "直播创建失败");
            }

        } catch (Exception e) {
            throw new BusinessException(20001, "直播创建失败");
        }
    }

    //    删除直播课程-欢拓云直播 (直播课程表，直播账号表,直播课程描述表)
    @Override
    public Boolean remoteById(Long liveCourseId) {
        // 查询直播课程
        LiveCourse liveCourse = baseMapper.selectById(liveCourseId);
        if (liveCourse != null) {
            try {
                // 删除直播课程-欢拓云直播
                Long courseId = liveCourse.getCourseId(); // 课程id
                String mtClouds = mtCloudClient.courseDelete(courseId.toString());

                // 删除直播课程表
                int liveCours = baseMapper.deleteById(liveCourseId);
                // 删除直播账号表
                LambdaQueryWrapper<LiveCourseAccount> lqwLCA = new LambdaQueryWrapper<>();
                lqwLCA.like(LiveCourseAccount::getLiveCourseId, liveCourseId);
                liveCourseAccountService.remove(lqwLCA);
                // 删除直播课程描述表
                LambdaQueryWrapper<LiveCourseDescription> lqwLCD = new LambdaQueryWrapper<>();
                lqwLCD.like(LiveCourseDescription::getLiveCourseId, liveCourseId);
                liveCourseDescriptionService.remove(lqwLCD);
                return true;
            } catch (Exception e) {
                throw new BusinessException(20001, "删除直播课程失败");
            }
        } else {
            return false;
        }
    }

    //    根据id查询直播课程，直播课程描述
    @Override
    public LiveCourseFormVo getLiveAndDescriptionById(Long liveCourseId) {
        // 查询直播课程
        LiveCourse liveCourse = baseMapper.selectById(liveCourseId);
        // 查询直播课程描述
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getliveByliveCourseId(liveCourseId);

        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
        BeanUtils.copyProperties(liveCourse, liveCourseFormVo); // liveCourse数字转数据liveCourseFormVo
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }

    //    根据id修改直播信息-欢拓云直播，直播课程表，直播课程描述表
    @Override
    public Boolean updateliveCourse(LiveCourseFormVo liveCourseFormVo) {
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo, liveCourse);// liveCourseFormVo数据复制到 liveCourse

        // 查询讲师
        Teacher teacher = courseFeignClientl.getTeacherById(liveCourse.getTeacherId());

        try {
            // 根据id修改直播信息-欢拓云直播
            HashMap<Object, Object> options = new HashMap<>();
            /** 添加直播-欢拓云直播
             * course_id 课程ID
             * account 发起直播课程的主播账号
             * course_name 课程名称
             * start_time 课程开始时间,格式:2015-01-01 12:00:00
             * end_time 课程结束时间,格式:2015-01-01 13:00:00
             * nickname 	主播的昵称
             * accountIntro 	主播的简介
             * options 		可选参数
             */
            String res = mtCloudClient.courseUpdate(liveCourse.getCourseId().toString(),
                    teacher.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);

            //   欢拓云直播返回结果转换
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            // 判断是否修改欢拓云直播数据成功
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) { //成功
                JSONObject dataJson = commonResult.getData(); // 获取欢拓云直播数据

                // 修改直播课程表
                liveCourse.setCourseId(dataJson.getLong("course_id"));
                baseMapper.updateById(liveCourse);

                // 根据直播课程id查询直播课程描述
                LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getliveByliveCourseId(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription()); //设置直播课程描述
                // 修改直播课程描述表
                liveCourseDescriptionService.updateById(liveCourseDescription);

                return true;

            } else { // 失败
                throw new BusinessException(20001, "修改直播信息失败");
            }

        } catch (Exception e) {
            throw new BusinessException(20001, "修改直播信息失败");
        }
    }

    // 获取最近的直播信息
    @Override
    public List<LiveCourseVo> findLatelyList() {
        List<LiveCourseVo> liveCourseVoList = baseMapper.findLatelyList(); // page, limit
        System.out.println("liveCourseVoList: " + liveCourseVoList);
        // 判断是否没直播过
        if (liveCourseVoList != null) {
            for (LiveCourseVo liveCourseVo : liveCourseVoList) {

                //  根据教师id查询教师
                Teacher teacher = courseFeignClientl.getTeacherById(liveCourseVo.getTeacherId());
                liveCourseVo.setTeacher(teacher);

                liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));
            }
        }

        return liveCourseVoList;
    }

    // 用户获取欢拓云直播access_token
    @Override
    public JSONObject getLiveAccessToken(Long LiveCourseId, Long userId) {
        // id查询直播课程
        LiveCourse liveCourse = baseMapper.selectById(LiveCourseId);
        // id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        System.out.println("课程id" + LiveCourseId);
        try {
            HashMap<Object, Object> options = new HashMap<>();
            /** 获取欢拓云直播access_token
             * course_id      课程ID
             * uid            用户唯一ID
             * nickname       用户昵称
             * role           用户角色，枚举见:ROLE 定义
             * expire         有效期,默认:3600(单位:秒)
             * options        可选项，包括:gender:枚举见上面GENDER定义,avatar:头像地址*/
            String res = mtCloudClient.courseAccess(liveCourse.getCourseId().toString(),
                    userId.toString(),
                    userInfo.getNickName(),
                    MTCloud.ROLE_USER, // 普通用户
                    80 * 80 * 80, options);

            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            System.out.println("commonResult" + commonResult);
            if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) { // 获取欢拓云直播access_token成功
                JSONObject accessToken = commonResult.getData();
                System.out.println("AccessToken:" + accessToken.getString("access_token"));
                return accessToken;
            }
        } catch (Exception e) {
            throw new BusinessException(20001, "获取欢拓云直播access_token失败");
        }
        return null;
    }

    //    根据id查询课程   直播课程表，直播账号表,直播课程描述表,教师表
    @Override
    public Map<String, Object> getInfo(Long courseId) {
        //    根据id查询直播课程，直播课程描述
        LiveCourseFormVo LiveCourseDescription = this.getLiveAndDescriptionById(courseId);
        // 查询教师
        Teacher teacher = courseFeignClientl.getTeacherById(LiveCourseDescription.getTeacherId());
        System.out.println("description: " + LiveCourseDescription + "teacher:" + teacher);

        Map<String, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("LiveCourseFormVo", LiveCourseDescription);

        LiveCourseVo liveCourse = new LiveCourseVo();
        BeanUtils.copyProperties(LiveCourseDescription, liveCourse);
        map.put("LiveStatus", this.getLiveStatus(liveCourse)); //直播状态

        return map;
    }

    /**
     * 直播状态 0：未开始 1：直播中 2：直播结束
     *
     * @return
     */
    private Integer getLiveStatus(LiveCourseVo liveCourse) {
        int liveStatus = 0;
        Date curTime = new Date();
        // 日期对比大小
        if (DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if (DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }
}
