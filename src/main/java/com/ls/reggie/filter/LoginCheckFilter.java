package com.ls.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.ls.reggie.common.BaseContext;
import com.ls.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 过滤器，过滤掉没有进行登录的请求
 * @ls
 * @create 2022 -- 10 -- 12
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/")
@Component
public class LoginCheckFilter implements Filter {

    // 路径匹配器，可以匹配通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 1. 获取本次请求的URI
        String URI = httpServletRequest.getRequestURI();

        log.info("拦截到请求" + URI);

        // 该数组里面的请求路径不用拦截
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        // 2. 判断本次请求是否需要处理
        boolean check = check(urls, URI);

        // 3. 如果不需要处理，直接放行

        if(check == true){
            log.info("本次请求不需要处理");
            chain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        // 4. 判断是否登录，如果登录，直接放行

        if(httpServletRequest.getSession().getAttribute("employee") != null){
            Long id = (Long) httpServletRequest.getSession().getAttribute("employee");
            log.info("本次请求已登陆，用户的id为" + id);
            BaseContext.setCurrentId(id);
            chain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }


        if(httpServletRequest.getSession().getAttribute("user") != null){
            Long id = (Long) httpServletRequest.getSession().getAttribute("user");
            log.info("本次请求已登陆，用户的id为" + id);
            BaseContext.setCurrentId(id);
            chain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        // 4. 如果未登录,返回登录页面
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));



    }



    // 匹配请求路径
    public boolean check(String[] urls,String requestURI){

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
