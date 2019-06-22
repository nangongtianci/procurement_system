package com.msb.config.system.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "quickers.generate")
@Configuration
public class FileConfig {
    // excel保存位置
    private String excelSavePath;
    // 图片上传路径
    private String imgPath;
    // SSL证书路径
    private String verifyPath;
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

    public String getVerifyPath() {
        return verifyPath;
    }

    public void setVerifyPath(String verifyPath) {
        this.verifyPath = verifyPath;
    }
}
