package com.personal.config.web;

import com.personal.config.interceptor.CustomerTokenSurvivalTimeInterceptor;
import com.personal.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private RedisService redisService;

    /**
     * 图片上传设置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大KB,MB
        factory.setMaxFileSize("2MB");
        // 设置总上传数据总大小
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }

    /**
     * 自动转换时间格式
     * @param registry date
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @desc 注册拦截器
     * @idea 所有注册拦截器均采用先添加拦截路径，后排除路径模式
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // token存活时间
        registry.addInterceptor(new CustomerTokenSurvivalTimeInterceptor(redisService))
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**","/customer/login","/customer/logout","/common/send/**");
    }
}
