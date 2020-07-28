package com.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class TokenInterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    TokenInterceptor tokenInterceptor(){
     return new TokenInterceptor();
    }
    @Autowired
    TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry ){
       registry.addInterceptor(this.tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/login","/project");
    }


}
