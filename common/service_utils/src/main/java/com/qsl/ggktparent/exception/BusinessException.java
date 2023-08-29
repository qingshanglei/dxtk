package com.qsl.ggktparent.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private Integer code;  //状态码
    private String msg; // 返回消息


}
