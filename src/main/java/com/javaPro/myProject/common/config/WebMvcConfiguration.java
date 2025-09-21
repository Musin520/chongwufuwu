package com.javaPro.myProject.common.config;

import com.javaPro.myProject.common.handle.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Bean
//    登陆拦截器LoginInterceptor
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }
    @Override
//    拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())//添加拦截器
                .excludePathPatterns("/login","uploading","/web/login","/web/register",
                        "/logout",
                        "/toLogin",
                        "/toRegister",
                        "/web/photoTemplate"
                )//去除对这些接口地址的拦截
                .addPathPatterns("/web/*","/*");
    }
}
