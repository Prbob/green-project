package com.brogrammers.brogrammers.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer { // 이미지 업로드 할 때 사용

    private String resourcePath = "/upload/**"; // view에서 접근할 경로
    private String savePath = "file:///C:/spring/imgs/"; // 실제 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
