package com.personal.config.system;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kuaige.generate")
public class FileConfig {
    // excel保存位置
    private String excelSavePath;
    // 图片上传路径
    private String imgPath;
    // 静态文件浏览位置
    private String staticBrowsePath;

    public String getExcelSavePath() {
        return excelSavePath;
    }

    public void setExcelSavePath(String excelSavePath) {
        this.excelSavePath = excelSavePath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getStaticBrowsePath() {
        return staticBrowsePath;
    }

    public void setStaticBrowsePath(String staticBrowsePath) {
        this.staticBrowsePath = staticBrowsePath;
    }
}
