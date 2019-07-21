package datagram.util;

/**
 * 转换数字的工具类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/19
 */
public class NumberConverter {

    /**
     * 将int类型数字转化为字节数组，长度为4
     * 字节数组高位为0，低位为3
     *
     * @param i 待转换的数字
     * @return 长度为4的字节数组
     */
    public static byte[] intToByteArray(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };

    }

    /**
     * 将字节数组转换为int类型数字
     *
     * @param b 待转换的数组，要求长度为4
     * @return 转换后的数字
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;

    }

    /**
     * 将short类型数字转化为字节数组，长度为2
     * 字节数组高位为0，低位为1
     *
     * @param s 待转换的数字
     * @return 长度为2的字节数组
     */
    public static byte[] shortToByteArray(short s) {
        return new byte[]{(byte) ((s >> 8) & 0xFF), (byte) (s & 0xFF)};
    }

    /**
     * 将字节数组转换为short类型数字
     *
     * @param b 待转换的数组，要求长度为2
     * @return 转换后的数字
     */
    public static short byteArrayToShort(byte[] b) {
        return (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
    }
}
