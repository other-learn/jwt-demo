package com.lm.example.jwtdemo.config;

import com.lm.example.jwtdemo.adapter.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 4. 注册拦截器到SpringMvc
 * @author meng-liang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private TokenInterceptor tokenInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.多个的时候，加入的顺序就是拦截器执行的顺序
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }
}
