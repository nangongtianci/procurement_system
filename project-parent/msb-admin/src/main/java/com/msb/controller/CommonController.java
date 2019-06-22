package com.msb.controller;

import com.msb.common.base.controller.BaseMsbAdminController;
import com.msb.common.utils.base.UUIDUtils;
import com.msb.common.utils.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/")
public class CommonController extends BaseMsbAdminController{

    @PostMapping("stream/upload")
    public Result uploadByStream(HttpServletRequest request) throws IOException {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        if (request.getContentLength() > 0) {
            try {
                String originalFileName = request.getHeader("filename");
                inputStream = request.getInputStream();

                Date date = new Date();
                String dateForm = new SimpleDateFormat("yyyy"+ File.separator+"MM").format(date);
                String path = File.separator+"data"+File.separator+"fileTest"+File.separator+dateForm;

                String newFileName = UUIDUtils.getUUID()+originalFileName.substring(originalFileName.lastIndexOf("."));

                File dest = new File(path,newFileName);
                if(!dest.getParentFile().exists()){
                    dest.getParentFile().mkdirs();
                }

                outputStream = new FileOutputStream(dest);

                byte temp[] = new byte[1024];
                int size;
                while ((size = inputStream.read(temp)) != -1) {
                    outputStream.write(temp, 0, size);
                }
                return Result.OK(File.separator+dateForm+File.separator+newFileName);
            } catch (Exception e) {
                return render("上传图片失败!");
            } finally {
                outputStream.close();
                inputStream.close();
            }
        }else {
            return render("上传图片失败!");
        }
    }


    @PostMapping("multipart/upload")
    public Result upload(MultipartFile file) throws IOException {
        try{
            Date date = new Date();
            String dateForm = new SimpleDateFormat("yyyy"+ File.separator+"MM").format(date);
            String path = File.separator+"data"+File.separator+"fileTest"+File.separator+dateForm;

            String originalFileName = file.getOriginalFilename();
            String newFileName = UUIDUtils.getUUID()+originalFileName.substring(originalFileName.lastIndexOf("."));

            File dest = new File(path,newFileName);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return Result.OK(File.separator+dateForm+File.separator+newFileName);
        }catch (Exception e){
            return render("上传图片失败!");
        }
    }
}
