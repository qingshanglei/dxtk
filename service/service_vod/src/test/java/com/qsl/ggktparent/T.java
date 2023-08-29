package com.qsl.ggktparent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import net.minidev.json.writer.BeansMapper;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class T {

    @Test
    void ss() {
        // 服务器路径
        String url1 = "https://ggkt-1316853721.cos.ap-guangzhou.myqcloud.com/2023/05/07/972374569794487396e91c9f0665d1351.jpg";
        // 数据库路径
        String url2 = "https://ggkt-1316853721.cos.ap-guangzhou.mycloud.com/2023/05/07/972374569794487396e91c9f0665d1351.jpg";
        System.out.println(url1.equals(url2));
    }


    @Test
    void BeanUtilsTest() {
        User user = new User(01, "张楚岚", "北京");
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(user, teacher); //user对象复制到teacher对象
        System.out.println(teacher); // 结果：Teacher(id=1, name=张楚岚)
    }

    // 内部实体类
    @Data
    @AllArgsConstructor
    private class User {
        private int id;
        private String name;
        private String address;
    }

    // 内部实体类
    @Data
    private class Teacher {
        private int id;
        private String name;
    }

    // 日期
    @Test
    void t() {
        long createTime = new Date().getTime() / 1000;
        long createTime2 = System.currentTimeMillis();
        Calendar cr = Calendar.getInstance();
        cr.set(2022,2,1);
//        System.out.println("SSSS:"+cr);
        System.out.println("SSSS:"+createTime);
        System.out.println("SSSS2:"+createTime2);

        // 获取当前时间戳（单位毫秒）
        long currentTimestamp = System.currentTimeMillis();
        // 将毫秒数转换为秒数并转换为Long类型
        long currentSecond = currentTimestamp / 1000L;
        System.out.println("当前时间戳（秒）：" + currentSecond);
        System.out.println("1684985816".length() == "1684985816".length());
    }

}
