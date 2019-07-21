package datagram.util;

/**
 * 二进制数运算器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/19
 */
public class BinaryCalc {

    /**
     * 二进制反码运算
     */
    static int inverseAdd(int x, int y) {
        int s = add(x, y);
        //如果最高位产生进位则结果+1
        if (Math.min(x, y) < 0 && s > 0)
            s += 1;
        return s;
    }

    /**
     * 二进制补码加法
     */
    private static int add(int x, int y) {
        if (y == 0) return x;
        int sum, carry;
        sum = x ^ y;
        carry = (x & y) << 1;
        return add(sum, carry);
    }
}
