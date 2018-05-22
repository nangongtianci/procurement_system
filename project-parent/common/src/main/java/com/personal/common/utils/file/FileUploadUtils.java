package com.personal.common.utils.file;

import com.personal.common.utils.base.UUIDUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件上传工具
 * @author ylw
 * @date 18-5-21 下午1:23
 * @param
 * @return
 */
public class FileUploadUtils {

    /**
     * 保存文件，直接以multipartFile形式
     * @param multipartFile
     * @param path 文件保存绝对路径
     * @return 返回文件名
     * @throws IOException
     */
    public static String saveImg(MultipartFile multipartFile, String path, String browsePath) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String fileName = UUIDUtils.getUUID() + ".png";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName));
        byte[] bs = new byte[1024];
        int len;
        while ((len = fileInputStream.read(bs)) != -1) {
            bos.write(bs, 0, len);
        }
        bos.flush();
        bos.close();
        return browsePath+fileName;
    }
}
