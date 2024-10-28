package com.pcp.funeralsvc.config;

import com.pcp.funeralsvc.web.interceptor.HttpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private HttpInterceptor httpInterceptor;
    @Autowired
    public WebConfiguration(HttpInterceptor httpInterceptor) {
        this.httpInterceptor = httpInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor)
                .addPathPatterns("/**");

//        registry.addInterceptor(httpInterceptor)
//                .addPathPatterns("/pcp/funeral/**");
    }

}
