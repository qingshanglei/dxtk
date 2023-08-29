package com.qsl.ggktparent.client.user;

import com.qsl.ggktparent.model.user.UserInfo;
import com.qsl.ggktparent.utils.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 服务调用
@Component // 默认baen
@FeignClient(value = "service-user") // 调用service-user微服务接口
public interface UserInfoFeignClient {

    @ApiOperation("根据用户id查询用户信息")
    @GetMapping("/admin/user/userInfo/getById/{id}")
    UserInfo getById(@ApiParam(value = "id", required = true) @PathVariable("id") Long id);

}
