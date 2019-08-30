package top.andnux.utils.common;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil 字符串工具类
 *
 * @author 张春林 2015-1-3 下午2:06:57
 */
public class StringUtil {

    private static final char UNDERLINE = '_';

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串2
     *
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean isEmail(String email) {
        boolean isExist = false;

        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * 判断是否为数字
     *
     * @param str 字符串
     * @return 是：true
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    /**
     * 转义
     *
     * @param src 字符串
     * @return 字符串
     */
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (j == '\'') {
                tmp.append("\\");
            } else if (j == '\\') {
                tmp.append("\\");
            }
            tmp.append(j);
        }
        return tmp.toString();
    }

    /**
     * 将不定数的几个对象拼接在一起。要求对象重写过toString方法
     *
     * @param param 不定数的对象
     * @return 拼接好的字符串
     */
    public static String append(Object... param) {
        if (param.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Object temp : param) {
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否为null和空
     *
     * @param param 字符串
     * @return 既不为空也不为null时，返回false
     */
    public static boolean isEmpty(String param) {
        return !(param != null && !("".equals(param)) && !("null".equals(param)));
    }

    /**
     * 判断字符串是否为null和空
     *
     * @param param 字符串
     * @return 为空或null时，返回false
     */
    public static boolean isNotEmpty(String param) {
        return !isEmpty(param);
    }

    /**
     * @param o 传入的对象
     * @return String 转化好的字符串
     * @Title: toString
     * @Description: 将对象转化为String
     */
    public static String toString(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    /**
     * @param o 传入的对象
     * @return int 转化好的数字
     * @Title: toInt
     * @Description: 将对象转化为String
     */
    public static int toInt(Object o) {
        if (o == null || isEmpty(o.toString())) {
            return 0;
        } else {
            return (int) toDouble(o.toString());
        }
    }

    public static String toIntString(Object o) {
        int result = toInt(o);
        return append(result);
    }

    /**
     * @param o 传入的对象
     * @return long 转化好的数字
     * @Title: toLong
     * @Description: 将对象转化为String
     */
    public static long toLong(Object o) {
        if (o == null || isEmpty(o.toString())) {
            return 0;
        } else {
            return (long) toDouble(o.toString());
        }
    }

    /**
     * @param o 传入的对象
     * @return double 转化好的数字
     * @Title: toDouble
     * @Description: 将对象转化为String
     */
    public static double toDouble(Object o) {
        if (o == null || isEmpty(o.toString())) {
            return 0;
        } else {
            return Double.valueOf(o.toString());
        }
    }

    /**
     * @param o 传入的对象
     * @return float 转化好的数字
     * @Title: toFloat
     * @Description: 将对象转化为String
     */
    public static float toFloat(Object o) {
        if (o == null || isEmpty(o.toString())) {
            return 0;
        } else {
            return Float.valueOf(o.toString());
        }
    }

    /**
     * 将list转化为object数组
     *
     * @param paramList 参数的list
     * @return Object[] 参数的数组
     */
    public static Object[] listToArray(List<Object> paramList) {
        Object[] o = new Object[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            o[i] = paramList.get(i);
        }
        return o;
    }

    /**
     * 在String两边加上 %
     *
     * @param param 需要加百分号的String
     * @return 加好百分号的String
     */
    public static String likeString(String param) {
        return append("%", param, "%");
    }

    /**
     * 将html转换为code
     *
     * @param s 字符串
     * @return 字符串
     */
    public static final String htmlToCode(String s) {
        if (s == null) {
            return "";
        } else {
            s = s.replace("\n\r", "<br>&nbsp;&nbsp;");
            s = s.replace("\r\n", "<br>&nbsp;&nbsp;");
            s = s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
            s = s.replace(" ", "&nbsp;");
            s = s.replace("\"", "\\" + "\"");
            return s;
        }
    }

    /**
     * 随机字符串
     *
     * @param length 字符串长度
     * @return 生成的随机字符串
     */
    public static String randomString(int length) {
        Random randGen = null;
        char[] numbersAndLetters = null;
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
                    + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * 随机数字
     *
     * @param length 字符串长度
     * @return 生成的随机数字
     */
    public static String randomNumber(int length) {
        Random randGen = null;
        char[] numbers = null;
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbers = "0123456789".toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbers[randGen.nextInt(10)];
        }
        return new String(randBuffer);
    }

    /**
     * md5加密
     *
     * @param text 要加密的字符串
     * @return 加密好的字符串
     */
    public static String toMd5(String text) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes("utf-8"));
            for (int i = 0; i < bytes.length; i++) {
                ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 模糊查询包装
     *
     * @param param 模糊查询关键字
     * @return 包装好的模糊查询字段
     */
    public static String likeSearch(String param) {
        if (isEmpty(param)) {
            return "";
        }
        return StringUtil.append("%", param, "%");
    }

    /**
     * 去除字符串最后一个字符
     *
     * @param param 参数
     * @return 结果
     */
    public static String trimLast(String param) {
        if (isEmpty(param)) {
            return "";
        }
        String result = param.substring(0, param.length() - 1);
        return result;
    }

    /**
     * 判断手机号码的合理性
     *
     * @param param 参数
     * @return 结果
     */
    public static boolean isPhone(String param) {

        boolean isFlag = false;
        if (param.length() == 11 && param.startsWith("1")) {
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 检测邮箱地址是否合法
     *
     * @param email
     * @return true合法 false不合法
     */
    public static boolean isAEmail(String email) {
        if (null == email || "".equals(email)) return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 是否有效的密码
     *
     * @return 不是返回true
     */
    public static boolean isNotValidePwd(String pwd) {
        if (isEmpty(pwd)) {
            return true;
        } else {
            if (pwd.length() < 6 || pwd.length() > 29) {
                return true;
            }
        }
        return false;
    }

    public static boolean toBoolean(Object object) {
        if (object == null) {
            return false;
        }
        Boolean value = false;
        try {
            value = Boolean.valueOf(object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
