package com.personal.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成器入口
 * @author ylw
 * @date 18-3-15 下午3:43
 * @param
 * @return
 */
public class FawVwGenerator {
    public static void main(String[] args) {
            generate("/home/ylw/Desktop/一起大众/2019-03-19/商城/generater/",
                    "vw_rent_evaluate",
                    "vw_rent_order",
                    "vw_rent_vehicle",
                    "vw_rent_vehicle_image",
                    "vw_rent_order_generate_record");
    }

    private static void generate(String model,String ... tableName){
        File file = new File(model);
        String path = file.getAbsolutePath();
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(path+"/src/main/java");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("ylw");
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        // ----------------数据源配置--------------------
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/faw_vw?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setTablePrefix(new String[]{"vw_"});
        strategy.setInclude(tableName);

        // 微小本后台
        strategy.setSuperEntityClass("com.vw.rent.utils.base.BaseEntity");
        strategy.setSuperEntityColumns(new String[] {"id","create_time","update_time"});

        strategy.setLogicDeleteFieldName("");
        strategy.setRestControllerStyle(true); // 生成Rest风格控制器
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.vw.rent");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 自定义配置，可以在 VM 中xianzai x使用 cfg.key 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("key", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return path+"/src/main/resources/mybatis/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        TemplateConfig tcf = new TemplateConfig();
        tcf.setXml(null);
        mpg.setTemplate(tcf);
        // 执行生成
        mpg.execute();
        System.out.println("生成完成.........");
    }



}
