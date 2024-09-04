package com.sgc.config;

import com.sgc.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtSecretKey))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/users/login/**")
                .excludePathPatterns("/api/users/logout/**")
                .excludePathPatterns("/api/users/captcha/**")
                .excludePathPatterns("/api/customer/users/login/**")
                .excludePathPatterns("/api/customer/users/logout/**")
                .excludePathPatterns("/api/customer/users/captcha/**")
                .excludePathPatterns("/api/admins/login/**")
                .excludePathPatterns("/api/admins/logout/**")
                .excludePathPatterns("/api/admins/captcha/**")
                ;
    }
}