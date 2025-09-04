package com.kisesaki.blog.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "kisesaki.blog.upload")
public class FileUploadProperties {
    private String path;
    private String baseUrl;
}
