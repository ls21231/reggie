package com.ls.reggie.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ls
 * @create 2022 -- 10 -- 13
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("本次拦截到的请求为" + request.getRequestURI());
        Object id = request.getSession().getAttribute("employee");
        log.info("id为" + id);
        if(id == null){
            request.getRequestDispatcher("/employee/login").forward(request,response);
            return false;
        }
        return true;
    }
}
