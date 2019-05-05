package com.personal.config.mybatis;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * MybatisPlus配置
 * @author ylw
 * @date 18-3-14 下午3:54
 * @param
 * @return
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages={"com.personal.mapper"})
public class MybatisPlusConfig {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MybatisPlusProperties properties;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType(DBType.MYSQL.getDb());
        return page;
    }

    /**
     * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
     * 配置文件和mybatis-boot的配置文件同步
     * @return
     */
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }

        mybatisPlus.setConfiguration(properties.getConfiguration());
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            mybatisPlus.setPlugins(this.interceptors);
        }

        if (this.databaseIdProvider != null) {
            mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
        }

        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }else{
            mybatisPlus.setTypeAliasesPackage("com.personal.entity;com.personal.conditions");
        }

        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }

        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            mybatisPlus.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mybatisPlus;
    }

}
