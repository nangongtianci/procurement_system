package com.personal.common.utils.encode;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 赵彤
 * @version V1.0
 * @Title: MD5Util.java
 * @Package com.guoan.common.utils.encode
 * @Description: MD5工具类.
 * @date 2013-10-10 下午05:27:31
 */
public class MD5Util {
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件MD5值
     *
     * @param file
     * @return
     * @throws IOException
     * @date 2012-1-9下午3:15:43
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return bufferToHex(messagedigest.digest());
    }

    /**
     * 密码字符串MD5加密 32位小写
     *
     * @param str
     * @return
     * @date 2012-1-9下午3:16:04
     */
    public static synchronized String getStringMD5(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        byte[] buffer = str.getBytes();
        messagedigest.update(buffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换    
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同    
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换    
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    //测试
    public static void main(String[] args) {
        System.out.println(MD5Util.getStringMD5("qweasdas123456"));//c163b192c0ac147da683456c6100f0d9
    }
}    