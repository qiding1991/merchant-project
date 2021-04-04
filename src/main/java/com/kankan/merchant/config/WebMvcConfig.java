package com.kankan.merchant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <qiding@kuaishou.com>
 * Created on 2021-04-04
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加全局的 CORS 配置
        registry.addMapping("/**")  //匹配所有的 URL ,相当于全局配置
                .allowCredentials(true) //允许Cookie
                .allowedMethods("*") //允许所有请求 Method
                .allowedHeaders("*") //允许所有请求 Heade
                .allowedOrigins("*")
                .maxAge(1800L); //有效期1800秒，两小时
    }
}
