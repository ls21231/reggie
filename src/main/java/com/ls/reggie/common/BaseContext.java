package com.ls.reggie.common;

/**
 *
 * 基于ThreadLocal的封装工具类，用于保存和获取当前用户的id
 * 在进行自动填充时，一个请求由一个线程进行处理，当进行保存或者更新时，
 * 从 过滤器，Controller方法，MybatisPlus的自动填充，同一个线程在进行
 * 可以在过滤器时将用户信息保存到TheadLocal中，自动填充时进行获取
 * @ls
 * @create 2022 -- 10 -- 13
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getThreadLocal(){
        return threadLocal.get();
    }
}
