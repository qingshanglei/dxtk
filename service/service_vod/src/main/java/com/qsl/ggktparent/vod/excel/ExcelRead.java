package com.qsl.ggktparent.vod.excel;

import com.alibaba.excel.EasyExcel;

/**
 * Excel表格-读操作_调用读监听器
 */
public class ExcelRead {
    public static void main(String[] args) {
        // 文件路径和名称
        String fileName = "G:\\Excel表格.xlsx";
        /**
         * 调用读取方法:
         * fileName：文件路径及名称，
         * Readable：读取的实体类
         * ExcelListener: 监听器
         * sheet:
         */
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();
    }
}
