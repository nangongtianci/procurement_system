package com.msb.common.utils.base;

import com.msb.common.utils.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @Title: StringUtils.java
* @Package com.guoan.common.utils
* @Description: 字符串工具类，用于实现一些字符串的常用操作
* @author 赵彤  
* @date 2013-10-10 下午05:40:08
* @version V1.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static final String NUMBERS_AND_LETTERS         = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS                     = "0123456789";
    public static final String LETTERS                     = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS             = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS          = "abcdefghijklmnopqrstuvwxyz";

    public static final String defaultKeyAndValueSeparator = ":";
    public static final String defaultValueEntitySeparator = ",";
    public static final String defaultKeyOrValueQuote      = "\"";

    /**
     * 判断字符串是否为空或长度为0
     * 
     * @param str
     * @return 若字符串为null或长度为0, 返回true; 否则返回false.
     * 
     * <pre>
     *      isEmpty(null)   =   true;
     *      isEmpty("")     =   true;
     *      isEmpty("  ")   =   false;
     * </pre>
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 判断字符串是否为空或长度为0，或仅由空格组成
     * 
     * @param str
     * @return 若字符串为null或长度为0或仅由空格组成, 返回true; 否则返回false.
     * 
     * <pre>
     *      isBlank(null)   =   true;
     *      isBlank("")     =   true;
     *      isBlank("  ")   =   true;
     *      isBlank("a")    =   false;
     *      isBlank("a ")   =   false;
     *      isBlank(" a")   =   false;
     *      isBlank("a b")  =   false;
     * </pre>
     */
    public static boolean isBlank(String str) {
        return (isEmpty(str) || (str.trim().length() == 0));
    }

    /**
     * 添加多参数非空判断
     * @param strings
     * @return
     */
    public static boolean isNotBlank(String... strings) {
        if (strings == null)
            return false;
        for (String str : strings)
            if (str == null || "".equals(str.trim()))
                return false;
        return true;
    }

    /**
     * 判断一堆字符串中，是否有一个为空
     * @param strs
     * @return
     */
    public static boolean isAnyBlank(String... strs){
        boolean result = false;
        for(String str : strs){
            if(isBlank(str)){
                result = true;
                break;
            }
        }
        return result;
    }
    /**
     * 将字符串首字母大写后返回
     * 
     * @param str 原字符串
     * @return 首字母大写后的字符串
     * 
     * <pre>
     *      capitalizeFirstLetter(null)     =   null;
     *      capitalizeFirstLetter("")       =   "";
     *      capitalizeFirstLetter("1ab")    =   "1ab"
     *      capitalizeFirstLetter("a")      =   "A"
     *      capitalizeFirstLetter("ab")     =   "Ab"
     *      capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     */
    public static String capitalizeFirstLetter(String str) {
        return (isEmpty(str) || !Character.isLetter(str.charAt(0))) ? str : Character.toUpperCase(str.charAt(0))
                                                                            + str.substring(1);
    }

    /**
     * 如果不是普通字符，则按照utf8进行编码
     * 
     * @param str 原字符
     * @return 编码后字符，编码错误返回null
     * 
     * <pre>
     *      utf8Encode(null)        =   null
     *      utf8Encode("")          =   "";
     *      utf8Encode("aa")        =   "aa";
     *      utf8Encode("啊啊啊啊")   = "编码后字符";
     * </pre>
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return str;
    }

    /**
     * 如果不是普通字符，则按照utf8进行编码，编码异常则返回defultReturn
     * 
     * @param str 源字符串
     * @param defultReturn 默认出错返回
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * null字符串转换为长度为0的字符串
     * 
     * @param str 待转换字符串
     * @return
     * @see
     * <pre>
     *  nullStrToEmpty(null)    =   "";
     *  nullStrToEmpty("")      =   "";
     *  nullStrToEmpty("aa")    =   "";
     * </pre>
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 以固定格式得到当前时间的字符串
     * 
     * @param format 时间格式，同时间的format，如"yyyy-MM-dd HH:mm:ss"
     * @return 时间字符串，若format为空或长度为空或只包含空格，则默认为"yyyy-MM-dd HH:mm:ss"
     * 
     * <pre>
     *      currentDate(null)                   = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("")                     = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("   ")                  = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("yyyy-MM-dd")           = "yyyy-MM-dd"
     *      currentDate("yyyy/MM/dd HH:mm:ss")  = "yyyy/MM/dd HH:mm:ss"
     * </pre>
     */
    public static String currentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(isBlank(format) ? "yyyy-MM-dd HH:mm:ss" : format);
        return dateFormat.format(new Date());
    }

    /**
     * 以"yyyy-MM-dd HH:mm:ss"格式得到当前时间
     * 
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String currentDate() {
        return currentDate("");
    }

    /**
     * 以固定格式得到当前时间的字符串，包含毫秒
     * 
     * @param format 时间格式，同时间的format，如"yyyy-MM-dd HH:mm:ss SS"
     * @return 时间字符串，若format为空或长度为空或只包含空格，则默认为"yyyy-MM-dd HH:mm:ss SS"
     * 
     * <pre>
     *      currentDate(null)                   = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("")                     = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("   ")                  = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("yyyy-MM-dd")           = "yyyy-MM-dd"
     *      currentDate("yyyy/MM/dd HH:mm:ss SS")  = "yyyy/MM/dd HH:mm:ss SS"
     * </pre>
     */
    public static String currentDateInMills(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(isBlank(format) ? "yyyy-MM-dd HH-mm-ss SS" : format);
        return dateFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 以"yyyy-MM-dd HH:mm:ss SS"格式得到当前时间
     * 
     * @return "yyyy-MM-dd HH:mm:ss SS"
     */
    public static String currentDateInMills() {
        return currentDateInMills("");
    }

    public static String getRandomNum(){ //生成6位随机数字
        String randomString = new Random().nextDouble() + "";
        return randomString.substring(randomString.indexOf(".") + 1, randomString.indexOf(".") + 7);
    }

    /**
     * 处理时间，用来显示状态更新时间
     * 
     * @param time
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String processTime(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Date now = new Date();
        Date orginalTime = new Date(time);
        long nowDay = now.getTime() - (now.getHours() * 3600 + now.getMinutes() * 60 + now.getSeconds()) * 1000;
        long yesterday = nowDay - oneDay;
        String nowHourAndMinute = toDoubleDigit(orginalTime.getHours()) + ":" + toDoubleDigit(orginalTime.getMinutes());
        if (time >= now.getTime()) {
            return "刚刚";
        } else if ((now.getTime() - time) < (60 * 1000)) {
            return (now.getTime() - time) / 1000 + "秒前 " + nowHourAndMinute + " ";
        } else if ((now.getTime() - time) < (60 * 60 * 1000)) {
            return (now.getTime() - time) / 1000 / 60 + "分钟前 " + nowHourAndMinute + " ";
        } else if ((now.getTime() - time) < (24 * 60 * 60 * 1000)) {
            return (now.getTime() - time) / 1000 / 60 / 60 + "小时前 " + nowHourAndMinute + " ";
        } else if (time >= nowDay) {
            return "今天 " + nowHourAndMinute;
        } else if (time >= yesterday) {
            return "昨天 " + nowHourAndMinute;
        } else {
            return toDoubleDigit(orginalTime.getMonth()) + "-" + toDoubleDigit(orginalTime.getDate()) + " "
                   + nowHourAndMinute + ":" + toDoubleDigit(orginalTime.getSeconds());
        }
    }

    /**
     * 将一位整数十位加0变成两位整数
     * 
     * @param number
     * @return
     */
    public static String toDoubleDigit(int number) {
        if (number >= 0 && number < 10) {
            return "0" + ((Integer)number).toString();
        }
        return ((Integer)number).toString();
    }

    /**
     * 得到href链接的innerHtml
     * 
     * @param href href内容
     * @return href的innerHtml
     *         <ul>
     *         <li>空字符串返回""</li>
     *         <li>若字符串不为空，且不符合链接正则的返回原内容</li>
     *         <li>若字符串不为空，且符合链接正则的返回最后一个innerHtml</li>
     *         </ul>
     * @see
     * <pre>
     *      getHrefInnerHtml(null)                                  = ""
     *      getHrefInnerHtml("")                                    = ""
     *      getHrefInnerHtml("mp3")                                 = "mp3";
     *      getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     *      getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     *      getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     *      getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     *      getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     *      getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }
        // String hrefReg = "[^(<a)]*<[\\s]*a[\\s]*[^(a>)]*>(.+?)<[\\s]*/a[\\s]*>.*";
        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * 得到固定长度的随机字符串，字符串由数字和字母混合组成
     * 
     * @param length 长度
     * @return
     * @see {@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由数字混合组成
     * 
     * @param length 长度
     * @return
     * @see {@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由字母混合组成
     * 
     * @param length 长度
     * @return
     * @see {@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由大写字母混合组成
     * 
     * @param length 长度
     * @return
     * @see {@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由小写字母混合组成
     * 
     * @param length 长度
     * @return
     * @see {@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由source中字符混合组成
     * 
     * @param source 源字符串
     * @param length 长度
     * @return
     *         <ul>
     *         <li>若source为null或为空字符串，返回null</li>
     *         <li>否则见{@link StringUtils#getRandom(char[] sourceChar, int length)}</li>
     *         </ul>
     */
    public static String getRandom(String source, int length) {
        return StringUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * 得到固定长度的随机字符串，字符串由sourceChar中字符混合组成
     *
     * @param sourceChar 源字符数组
     * @param length 长度
     * @return
     *         <ul>
     *         <li>若sourceChar为null或长度为0，返回null</li>
     *         <li>若length小于0，返回null</li>
     *         </ul>
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder("");
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * html的转移字符转换成正常的字符串
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        } else {
            return source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;",
                                                                                                              "\"");
        }
    }

    /**
     * 比较两个String
     *
     * @param actual
     * @param expected
     * @return
     *         <ul>
     *         <li>若两个字符串都为null，则返回true</li>
     *         <li>若两个字符串都不为null，且相等，则返回true</li>
     *         <li>否则返回false</li>
     *         </ul>
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * 将key和value键值对转换成map，结果会忽略key和value中的空格，忽略为空的key
     *
     * @param source key和value键值对
     * @param keyAndValueSeparator 键值对中key和value分隔符
     * @param valueEntitySeparator 键值对中两个entity分隔符
     * @param keyOrValueQuote 键值对中key或者value的引用号
     * @return
     * @see
     *      <ul>
     *      <li>parseKeyAndValueToMap("key1:value1,key2,value2", ":", ",")={(key1, value1), (key2, value2)}</li>
     *      <li>parseKeyAndValueToMap(" key1:value1 ,key2,value2", ":", ",")={(key1, value1), (key2, value2)}</li>
     *      <li>parseKeyAndValueToMap(" key1: value1 ,key2,value2", ":", ",")={(key1, value1), (key2, value2)}</li>
     *      </ul>
     */
    public static Map<String, String> parseKeyAndValueToMap(String source, String keyAndValueSeparator,
                                                            String valueEntitySeparator, String keyOrValueQuote) {
        if (isEmpty(source)) {
            return null;
        }

        if (isEmpty(keyAndValueSeparator)) {
            keyAndValueSeparator = defaultKeyAndValueSeparator;
        }
        if (isEmpty(valueEntitySeparator)) {
            valueEntitySeparator = defaultValueEntitySeparator;
        }
        if (isEmpty(keyOrValueQuote)) {
            keyOrValueQuote = defaultKeyOrValueQuote;
        }
        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        String[] keyAndValueArray = source.split(valueEntitySeparator);
        if (keyAndValueArray != null) {
            int seperator;
            for (String valueEntity : keyAndValueArray) {
                if (!isEmpty(valueEntity)) {
                    seperator = valueEntity.indexOf(keyAndValueSeparator);
                    if (seperator != -1 && seperator < valueEntity.length()) {
                        MapUtils.putMapNotEmptyKey(keyAndValueMap,
                                                   RemoveBothSideSymbol(valueEntity.substring(0, seperator).trim(),
                                                                        keyOrValueQuote),
                                                   RemoveBothSideSymbol(valueEntity.substring(seperator + 1).trim(),
                                                                        keyOrValueQuote));
                    }
                }
            }
        }
        return keyAndValueMap;
    }

    /**
     * 将key和value键值对转换成map，结果会忽略key和value中的空格，忽略为空的key
     *
     * @param source key和value键值对
     * @return
     * @see
     *      <ul>
     *      <li>见{@link StringUtils#parseKeyAndValueToMap(String, String, String, String)}</li>
     *      </ul>
     */
    public static Map<String, String> parseKeyAndValueToMap(String source) {
        return parseKeyAndValueToMap(source, defaultKeyAndValueSeparator, defaultValueEntitySeparator,
                                     defaultKeyOrValueQuote);
    }

    /**
     * 去掉字符串两边的符号，返回去掉后的结果
     * 
     * @param source 原字符串
     * @param symbol 符号
     * @return
     */
    public static String RemoveBothSideSymbol(String source, String symbol) {
        if (isEmpty(source) || isEmpty(symbol)) {
            return source;
        }

        int firstIndex = source.indexOf(symbol);
        int lastIndex = source.lastIndexOf(symbol);
        try {
            return source.substring(((firstIndex == 0) ? symbol.length() : 0),
                                    ((lastIndex == source.length() - 1) ? (source.length() - symbol.length()) : source.length()));
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * 字符串匹配(仅支持"*"匹配).
     * @param pattern 模板
     * @param str 要验证的字符串
     * @return
     */
    public static boolean simpleWildcardMatch(String pattern, String str) {
        return wildcardMatch(pattern, str, "*");
    }

    /**
     * 字符串匹配.
     * @param pattern   模板
     * @param str   要验证的字符串
     * @param wildcard 通配符
     * @return
     */
    public static boolean wildcardMatch(String pattern, String str, String wildcard) {
        if (StringUtils.isEmpty(pattern) || StringUtils.isEmpty(str)) {
            return false;
        }
        final boolean startWith = pattern.startsWith(wildcard);
        final boolean endWith = pattern.endsWith(wildcard);
        String[] array = StringUtils.split(pattern, wildcard);
        int currentIndex = -1;
        int lastIndex = -1 ;
        switch (array.length) {
            case 0:
                return true ;
            case 1:
                currentIndex = str.indexOf(array[0]);
                if (startWith && endWith) {
                    return currentIndex >= 0 ;
                }
                if (startWith) {
                    return currentIndex + array[0].length() == str.length();
                }
                if (endWith) {
                    return currentIndex == 0 ;
                }
                return str.equals(pattern) ;
            default:
                for (String part : array) {
                    currentIndex = str.indexOf(part);
                    if (currentIndex > lastIndex) {
                        lastIndex = currentIndex;
                        continue;
                    }
                    return false;
                }
                return true;
        }
    }

    /**
     * 判断是否为移动电话号码。
     * @param mobileNum
     * @return
     */
    public static boolean isMobileNO(String mobileNum){
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobileNum);
        return m.find();
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        return count > 0;
//        float result = count / chLength;
//        if (result > 0.4) {
//            return true;
//        } else {
//            return false;
//        }
    }

//    public static void main(String[] args) {
//        System.out.println(isMessyCode("Ã©Å¸Â©Ã©Â¡ÂºÃ¥Â¹Â³"));
//        System.out.println(isMessyCode("???"));
//    }

    /**
     * 获取用户手机的昵称化版本
     * 比如：18601375468 -> 186*****468
     * @return
     */
    public static String shortMobilephone(String mobilephone){
            /**
             * 不为空，并且长度为11
             */
           if(StringUtils.isNotBlank(mobilephone) && mobilephone.length() == 11){
               return mobilephone.substring(0,3) + "****" + mobilephone.substring(7, 11);
           } else {
               return "****";
           }
    }

    /**
     *
     * @param price 价格，应为Long，小数点后两位
     * @param pre
     * @return
     */
    public static String formatPrice(String price, String pre){
        return ((StringUtils.isNotBlank(price) && Double.valueOf(price) > 0) ? (pre + "￥" + price) : "--");
    }

    public static String formatPrice(String price){
        return ((StringUtils.isNotBlank(price) && Double.valueOf(price) > 0) ? ("￥" + price) : "--");
    }

    /**
     * 返回日期对应的字符串，用于创建图片存储的路径
     * @return
     */
//    public static String calendarPath(){
//        Calendar now = Calendar.getInstance();
//        int year = now.get(Calendar.YEAR);
//        int month = now.get(Calendar.MONTH) + 1;
//        int day = now.get(Calendar.DAY_OF_MONTH);
//        return year + FtpHelper.FILE_SEPARATOR + month + FtpHelper.FILE_SEPARATOR + day + FtpHelper.FILE_SEPARATOR;
//    }

    /**
     * 判断与其中任意一个相等
     * @param string
     * @param searchStrings
     * @return
     */
    public static boolean equalsWithAny(CharSequence string, CharSequence... searchStrings) {
        if(!isEmpty(string) && !ArrayUtils.isEmpty(searchStrings)) {
            CharSequence[] arr$ = searchStrings;
            int len$ = searchStrings.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                CharSequence searchString = arr$[i$];
                if(equals(string, searchString)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 当字符串为null时，返回null，以便在转json数据时有所显示
     * @return
     */
    public static String ifNull(String str){
        return str == null ? "" : str;
    }

    /**
     * 多個id进行拼接，'id1','id2'
     * @param id
     * @return
     */
    public static String getArrayId(String id){
        String roids="";
        if(id.contains(",")) {
            for (String s : id.split(",")) {
                roids +="'" + s + "',";
            }
            roids = roids.substring(0, roids.length()-1);
            return roids;
        }else{
            roids ="'"+id+"'";
        }
        return roids;
    }


    public static String getAccordInSqlStr(String ... ids){
        if(ArrayUtils.isEmpty(ids)){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if(ids.length == 1){
            return sb.append("'").append(ids[0]).append("'").toString();
        }
        for(int i = 0; i < ids.length; i++){
            sb.append("'").append(ids[i]).append("'").append(",");
        }
        String result = sb.toString();
        result = result.substring(0,result.length()-1);
        return result;
    }

}