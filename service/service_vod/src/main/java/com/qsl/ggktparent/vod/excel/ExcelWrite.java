package com.qsl.ggktparent.vod.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel表格-写操作
 */
public class ExcelWrite {
    public static void main(String[] args) {
        // 文件路径和名称
        String fileName = "G:\\Excel表格.xlsx";
        // 调用方法   write(OutputStream outputStream):流方式
//        write(String pathName, Class head)：文件名称路径
//         ExcelWriterBuilder write(File file, Class head) :文件
        EasyExcel.write(fileName, User.class) // write(fileName,User):创建完表格了
                .sheet("写操作") //创建表格的每一页(sheet)
                .doWrite(data()); // 写数据
    }

    // for遍历写10条数据
    private static List<User> data() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setName("lucy" + i);
            list.add(user);
        }
        return list;
    }

}
