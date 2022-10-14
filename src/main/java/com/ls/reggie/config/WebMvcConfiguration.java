package com.ls.reggie.config;

import com.ls.reggie.common.JacksonObjectMapper;
import com.ls.reggie.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * MVC的扩展类
 * @ls
 * @create 2022 -- 10 -- 11
 */

@Slf4j
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {


    // 配置资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("成功");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns(
//                "/employee/login",
//                "/employee/logout",
//
//                "/front/**");
//    }



    // 扩展消息转换器，将服务端的数据已JSON的形式相应给客户端
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 创建一个消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，使用自己的
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 添加进消息转换器集合中
        converters.add(0,messageConverter);
    }
}
