package com.qsl.ggktparent.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qsl.ggktparent.exception.BusinessException;
import com.qsl.ggktparent.model.vod.Subject;
import com.qsl.ggktparent.vo.vod.SubjectEeVo;
import com.qsl.ggktparent.vod.listener.SubjectListener;
import com.qsl.ggktparent.vod.mapper.SubjectMapper;
import com.qsl.ggktparent.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程科目 服务实现类
 *
 * @author 青衫泪
 * @since 2023-05-07
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    // 课程查询-树形结构_懒加载
    @Override
    public List<Subject> selectList(Long id) {
        QueryWrapper<Subject> qw = new QueryWrapper<>();
        qw.eq("parent_id", id);
        // MybatisPlus注入Mapper层的两种方式：①@AuAutowired注解注入，②BaseMapper注入
        List<Subject> subjects = baseMapper.selectList(qw); // 查询parent_id为0的数据
        //  TODO  根据parent_id判断是否有下一层数据，有hasChildren=true
        for (Subject subject : subjects) {
            Long subjectId = subject.getId();
            // 查询parent_id是否有下一层数据，有hasChildren设置为true
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return subjects;
    }

    // 查询parent_id是否有下一层数据，有true
    private boolean isChildren(Long id) {
        QueryWrapper<Subject> qw = new QueryWrapper<>();
        qw.eq("parent_id", id);
        Integer count = baseMapper.selectCount(qw);
        return count > 0; // 1>0:true  0>0:false
    }

    // 导出分类课程
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel"); //设置下载文件类型mime类型
            response.setCharacterEncoding("utf-8"); //防止中文乱码
            String fileName = URLEncoder.encode("课程分类文件", "UTF-8");//设置文件名称
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<Subject> subjectList = baseMapper.selectList(null); //查询所有数据

            // 定义表头样式
//            WriteCellStyle headerStyle = new WriteCellStyle();
//            headerStyle.setHorizontalAlignment(HorizontalAlignment.CENTER); //表头数据居中
            // 创建sheet对象

//           List<Subject> ==相同实体类复制==》  List<SubjectEeVo>
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>(subjectList.size());
            for (Subject subject : subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                //    subject对象复制到subjectEeVo对象，一个个set也行。 注意：只复制相同名称的。
                BeanUtils.copyProperties(subject, subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            // EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类sheel") // 设置sheel名称
                    .doWrite(subjectEeVoList);

        } catch (Exception e) {
            // 抛出自定义异常
            throw new BusinessException(20001, "导入失败");
        }
    }

    @Autowired // 注入Excel表格监听器
    private SubjectListener subjectListener;

    //    导入课程
    @Override
    public void importDictData(MultipartFile file) {

        try {
            // EasyExcel  注意：此处不能通过new subjectListener监听器。
            EasyExcel.read(file.getInputStream(), SubjectEeVo.class, subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException(20001, "导入失败");
        }

    }


}
