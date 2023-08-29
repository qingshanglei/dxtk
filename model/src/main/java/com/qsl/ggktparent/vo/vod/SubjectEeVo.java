package com.qsl.ggktparent.vo.vod;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Dict
 *   Excel表格-导入，导出
 * @author qy
 */
@Data
public class SubjectEeVo {

	// @ExcelProperty:Excel表格   value:设置表头名称,   index:从第几列读取Excel表格
	@ExcelProperty(value = "id" ,index = 0)
	private Long id;

	@ExcelProperty(value = "课程分类名称" ,index = 1)
	private String title;

	@ExcelProperty(value = "上级id" ,index = 2)
	private Long parentId;

	@ExcelProperty(value = "排序" ,index = 3)
	private Integer sort;

}

