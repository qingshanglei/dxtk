package com.qsl.ggktparent.vod.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel表格-读写操作
 */
@Data //
public class User {

    // TODO @ExcelProperty:Excel表格   value:设置表头名称,   index:从第几列读取Excel表格
    @ExcelProperty(value = "学生id",index = 0)
    private int id;

    @ExcelProperty(value = "用户名称",index = 1)
    private String name;
}
