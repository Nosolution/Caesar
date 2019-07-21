package datagram.util;

import datagram.NetInfo;
import datagram.Segment;


/**
 * Segment的使用帮助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/19
 */
public class SegmentHelper {

    /**
     * 便利地设置
     *
     * @param segment
     * @param info
     */
    public static void setNetInfo(Segment segment, NetInfo info) {
        segment.setSourceIp(NumberConverter.byteArrayToInt(info.getSourceIp()));
        segment.setDestIp(NumberConverter.byteArrayToInt(info.getDestIp()));
        segment.setSourcePort(NumberConverter.byteArrayToInt(info.getSourcePort()));
        segment.setDestPort(NumberConverter.byteArrayToInt(info.getDestPort()));

    }

    /**
     * 用于字节数组合并，结果写入第一个参数所指的数组
     *
     * @param dest
     * @param source
     */
    public static byte[] link(byte[] dest, byte[] source) {
        if (source.length == 0)
            return dest;
        byte[] res = new byte[dest.length + source.length];
        System.arraycopy(dest, 0, res, 0, dest.length);
        System.arraycopy(source, 0, res, dest.length, source.length);
        return res;
    }

    public static void calcCheckNum(Segment segment) {
        segment.setCheckSum(0);
        int r = BinaryCalc.inverseAdd(segment.getSourceIp(), segment.getDestIp());
        r = BinaryCalc.inverseAdd(r, (10 << 16) | segment.getLength());
        r = BinaryCalc.inverseAdd(r, (segment.getSourcePort() << 16) | segment.getDestPort());
        r = BinaryCalc.inverseAdd(r, segment.getSeqNumber());
        segment.setCheckSum(r);
    }

    public static boolean check(Segment segment) {
        return true;
    }


}
