package com.qsl.ggktparent.vod.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * Excel表格-读操作(监听器)
 */
public class ExcelListener extends AnalysisEventListener<User> {

    //    一行一行读取excel表格的内容，把每行的内容封装到user对象中
    //    注意：从excel表格的第二行开始读取。   第一行默认为表头。
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println("Excel内容: "+user);
    }

    // 读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("Excel表头：" + headMap);
    }

    // 读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
