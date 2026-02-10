package com.qwer.qrorder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 절대경로 기반으로 접근 가능하게 설정
        String uploadPath = "file:" + System.getProperty("user.dir") + "/uploads/";

        System.out.println("[UPLOAD PATH MAPPING] " + uploadPath);

        registry.addResourceHandler("/uploads/**")
        		.addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/")
                .setCachePeriod(0); // 캐싱 방지 (개발 중엔 이게 좋음)
    }
}