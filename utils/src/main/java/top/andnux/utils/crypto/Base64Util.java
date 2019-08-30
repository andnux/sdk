package top.andnux.utils.crypto;


import org.bouncycastle.util.encoders.Base64;

public class Base64Util {

    /**
     * 字符编码
     */
    public final static String ENCODING = "UTF-8";

    /**
     * Base64编码
     *
     * @param data 待编码数据
     * @return String 编码数据
     */
    public static byte[]  encode(byte[] data)  {
        // 执行编码
        return  Base64.encode(data);
    }

    /**
     * Base64编码
     *
     * @param data 待编码数据
     * @return String 编码数据
     * @throws Exception
     */
    public static String encode(String data) throws Exception {
        // 执行编码
        byte[] b = encode(data.getBytes());
        return new String(b, ENCODING);
    }

    /**
     * Base64解码
     *
     * @param data 待解码数据
     * @return String 解码数据
     */
    public static byte[] decode(byte[] data) {
        // 执行解码
        return Base64.decode(data);
    }

    /**
     * Base64解码
     *
     * @param data 待解码数据
     * @return String 解码数据
     * @throws Exception
     */
    public static String decode(String data) throws Exception {
        // 执行解码
        return new String(decode(data.getBytes()), ENCODING);
    }
}
