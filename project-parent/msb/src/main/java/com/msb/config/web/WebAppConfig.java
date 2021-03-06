package com.msb.config.web;

import com.msb.common.cache.RedisService;
import com.msb.config.convert.DateConverter;
import com.msb.config.interceptor.CustomerTokenSurvivalTimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;

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
        factory.setMaxFileSize("5MB");
        // 设置总上传数据总大小
        factory.setMaxRequestSize("20MB");
        return factory.createMultipartConfig();
    }

    /**
     * 配置全局日期转换器
     */
    @Bean
    @Autowired
    public ConversionService getConversionService(DateConverter dateConverter){
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<>();
        converters.add(dateConverter);
        factoryBean.setConverters(converters);
        return factoryBean.getObject();
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
                .excludePathPatterns("/static/**","/customer/login","/customer/logout","/upload"
                        ,"/common/send/**","/bill/scan");
    }
}
