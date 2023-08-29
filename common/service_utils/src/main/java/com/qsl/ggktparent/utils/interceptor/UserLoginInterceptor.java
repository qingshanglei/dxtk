package com.qsl.ggktparent.utils.interceptor;

import com.qsl.ggktparent.utils.AuthContextHolder;
import com.qsl.ggktparent.utils.JwtHelper;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringMVC拦截器1
 */
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public UserLoginInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    //原始方法调用前执行的内容
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("原始方法调用后执行的内容");
        this.initUserLoginVo(request);

        return true; // 是否终止请求方法：  false:终止，  true：继续执行
    }

    //原始方法调用后执行的内容
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("原始方法调用后执行的内容");
    }

    // 原始方法调用完成后执行的内容
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("原始方法调用完成后执行的内容");
    }

    /**
     * 请求头中获取token
     *
     * @param request
     */
    private void initUserLoginVo(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println("token:" + token);
//        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(token)) {
            AuthContextHolder.setUserId(1L);
        } else {
            Long userId = JwtHelper.getUserId(token);
            System.out.println("当前用户：" + userId);
            if (StringUtils.isEmpty(userId)) {
                AuthContextHolder.setUserId(1L);
                System.out.println("当前用户1：" + userId);
            } else {
                System.out.println("当前用户3：" + userId);
                AuthContextHolder.setUserId(userId);
            }
        }
    }

}
