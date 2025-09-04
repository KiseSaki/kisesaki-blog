package com.kisesaki.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kisesaki.blog.file.config.FileUploadProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Web MVC 配置类
 * 
 * 功能特性：
 * 1. 静态资源映射配置
 * 2. 文件上传路径映射
 * 3. 其他 Web 相关配置
 * 
 * @author KiseSaki
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;

    /**
     * 配置静态资源处理器
     * 
     * 将 /files/** 的请求映射到实际的文件上传目录
     * 例如：GET /api/files/hash/filename.jpg -> uploads/hash/filename.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件上传目录的静态资源映射
        String uploadPath = fileUploadProperties.getPath();

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600) // 缓存1小时
                .resourceChain(true);

        log.info("Static resource mapping configured: /files/** -> file:{}/", uploadPath);

        // 如果需要其他静态资源，可以继续添加
        // registry.addResourceHandler("/static/**")
        // .addResourceLocations("classpath:/static/");
    }
}
