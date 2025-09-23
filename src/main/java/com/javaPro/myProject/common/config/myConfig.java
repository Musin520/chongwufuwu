package com.javaPro.myProject.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class myConfig implements WebMvcConfigurer {
    /*
     * 静态资源映射配置
     * 注意：由于已改为OSS存储，不再需要本地文件映射
     * 如果需要其他静态资源映射，可以在这里添加
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 临时保留本地图片映射，用于显示已存在的图片
        // 新上传的图片将使用OSS存储
        registry.addResourceHandler("/product/**").addResourceLocations("file:"+System.getProperty("user.dir") + "/src/main/resources/static/img/");

        // 静态资源映射
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
