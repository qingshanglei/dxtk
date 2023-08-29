package com.qsl.ggktparent.utils.interceptor;

import com.qsl.ggktparent.utils.AuthContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringMVC拦截器2
 */
//@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public AdminLoginInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        System.out.println("原始方法调用后执行的内容");
        this.initUserLoginVo(request);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        System.out.println("原始方法调用后执行的内容");

//        return true;
    }

    // 原始方法调用完成后执行的内容
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        System.out.println("原始方法调用完成后执行的内容");
    }


    /**
     * 请求头中获取userId
     *
     * @param request
     */
    private void initUserLoginVo(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(userId)) {
            System.out.println("userId：" + userId);
            AuthContextHolder.setAdminId(1L);
        } else {
            AuthContextHolder.setAdminId(Long.parseLong(userId));
        }
    }


}
