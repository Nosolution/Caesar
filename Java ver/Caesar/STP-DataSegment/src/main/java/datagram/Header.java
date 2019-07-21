package datagram;


import static datagram.util.NumberConverter.*;
import static datagram.util.SegmentHelper.link;

/**
 * {@link Segment}Segment的首部，分担Segment的属性，仅包内可见，隐藏实现细节
 * 格式:
 * |----------------|----------------|----------------|----------------|
 * |00------------07|08------------15|16------------23|24------------31|
 * |                             sourceIp(4)                           | -|
 * |                              destIp(4)                            |  | 伪首部
 * |       00       |       10       |           data_length           | -|
 * |            sourcePort           |             destPort            |
 * |                              seqNumber                            |
 * |                              ackNumber                            |
 * |     special    |                      00                          |
 * |              window             |             checkNum            |
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
class Header {
    //首部总共20字节,加上伪首部32字节
    private int sourceIp;
    private int destIp;
    private short length = 0;
    private short sourcePort;
    private short destPort;
    private int seqNumber = 0;
    private int ackNumber = 0;
    private int codeByte = 0; //实占1字节，低位3字节用于占位
    private short windowsSize = 0;
    private short checkSum = 0;

    //特殊位所在位置，从24-29，最高位为31，最低位为0
    private static final int URG_BIT = 29;
    private static final int ACK_BIT = 28;
    private static final int PSH_BIT = 27;
    private static final int RST_BIT = 26;
    private static final int SYN_BIT = 25;
    private static final int FIN_BIT = 24;

    //定义的Header长度，单位为字节
    static final int HEADER_LENGTH = 32;

    /**
     * 将Header实例转换为字节数组
     * 字段按原顺序排列
     *
     * @return Header实例字节数组
     */
    byte[] toByteArray() {
        byte[] res = new byte[0];
        res = link(res, intToByteArray(sourceIp));
        res = link(res, intToByteArray(sourceIp));
        res = link(res, shortToByteArray((short) 10));
        res = link(res, shortToByteArray(length));
        res = link(res, shortToByteArray(sourcePort));
        res = link(res, shortToByteArray(destPort));
        res = link(res, intToByteArray(seqNumber));
        res = link(res, intToByteArray(ackNumber));
        res = link(res, intToByteArray(codeByte));
        res = link(res, shortToByteArray(windowsSize));
        res = link(res, shortToByteArray(checkSum));
        return res;
    }


    /**
     * 从字节数组中解析出首部
     *
     * @param origin 待解析字节数组
     * @return 解析成功的首部
     */
    static Header parse(byte[] origin) {
        if (origin.length != HEADER_LENGTH)
            throw new RuntimeException("Length of header is not " + HEADER_LENGTH + ".");
        //origin长度为32，由Segment保证
        Header header = new Header();
        header.setSourceIp(byteArrayToInt(subArray(origin, 0, 4)));
        header.setDestIp(byteArrayToInt(subArray(origin, 4, 4)));
        header.setLength(byteArrayToShort(subArray(origin, 10, 2)));
        header.setSourcePort(byteArrayToShort(subArray(origin, 12, 2)));
        header.setDestPort(byteArrayToShort(subArray(origin, 14, 2)));
        header.setSeqNumber(byteArrayToInt(subArray(origin, 16, 4)));
        header.setAckNumber(byteArrayToInt(subArray(origin, 20, 4)));
        header.setCodeByte(byteArrayToInt(subArray(origin, 24, 4)));
        header.setWindowsSize(byteArrayToShort(subArray(origin, 28, 2)));
        header.setCodeByte(byteArrayToShort(subArray(origin, 30, 2)));

        return header;
    }

    int getSourceIp() {
        return sourceIp;
    }

    void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }

    int getDestIp() {
        return destIp;
    }

    void setDestIp(int destIp) {
        this.destIp = destIp;
    }

    short getLength() {
        return length;
    }

    void setLength(int length) {
        this.length = (short) length;
    }

    short getSourcePort() {
        return sourcePort;
    }

    void setSourcePort(int sourcePort) {
        this.sourcePort = (short) sourcePort;
    }

    short getDestPort() {
        return destPort;
    }

    void setDestPort(int destPort) {
        this.destPort = (short) destPort;
    }

    int getSeqNumber() {
        return seqNumber;
    }

    void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    int getAckNumber() {
        return ackNumber;
    }


    void setAckNumber(int ackNumber) {
        this.ackNumber = ackNumber;
    }

    private void setCodeByte(int codeByte) {
        this.codeByte = codeByte;
    }


    short getWindowsSize() {
        return windowsSize;
    }

    void setWindowsSize(int windowsSize) {
        this.windowsSize = (short) windowsSize;
    }

    short getCheckSum() {
        return checkSum;
    }

    void setCheckSum(int checkSum) {
        this.checkSum = (short) checkSum;
    }

    boolean isURG() {
        return bitIsSet(URG_BIT);
    }

    void setURG() {
        setCode(URG_BIT);
    }

    void cancelURG() {
        cancelCode(URG_BIT);
    }

    boolean isACK() {
        return bitIsSet(ACK_BIT);
    }

    void setACK() {
        setCode(ACK_BIT);
    }

    void cancelACK() {
        cancelCode(ACK_BIT);
    }

    boolean isPSH() {
        return bitIsSet(PSH_BIT);
    }

    void setPSH() {
        setCode(PSH_BIT);
    }

    void cancelPSH() {
        cancelCode(PSH_BIT);
    }

    boolean isRST() {
        return bitIsSet(RST_BIT);
    }

    void setRST() {
        setCode(RST_BIT);
    }

    void cancelRST() {
        cancelCode(RST_BIT);
    }

    boolean isSYN() {
        return bitIsSet(SYN_BIT);
    }

    void setSYN() {
        setCode(SYN_BIT);
    }

    void cancelSYN() {
        cancelCode(SYN_BIT);
    }

    boolean isFIN() {
        return bitIsSet(FIN_BIT);
    }

    void setFIN() {
        setCode(FIN_BIT);
    }

    void cancelFIN() {
        cancelCode(FIN_BIT);
    }

    /**
     * 判断特殊位是否被设置(1为已设)
     *
     * @param bit 待判断的特殊位，逻辑上的范围为(24-29)
     * @return {@code true}如果该位被设置，反之为@{code false}
     */
    private boolean bitIsSet(int bit) {
        return (codeByte >>> bit & 1) % 2 == 1;
    }

    /**
     * 设置特殊位
     *
     * @param bit 待设置的特殊位，逻辑上的范围为(24-29)
     */
    private void setCode(int bit) {
        if (0 <= bit && bit <= 31)
            codeByte |= 1 << bit;
    }

    /**
     * 取消设置特殊位
     *
     * @param bit 待取消设置的特殊位，逻辑上的范围为(24-29)
     */
    private void cancelCode(int bit) {
        if (0 <= bit && bit <= 31) {
            int b = 1 << bit;
            codeByte &= ~b;
        }
    }


    /**
     * 用于从字节数组中截取子数组
     *
     * @param source 源数组
     * @param start  开始位置
     * @param len    待截取长度
     * @return 截取的子字节数组
     */
    private static byte[] subArray(byte[] source, int start, int len) {
        byte[] res = new byte[len];
        System.arraycopy(source, start, res, 0, len);
        return res;
    }


}
