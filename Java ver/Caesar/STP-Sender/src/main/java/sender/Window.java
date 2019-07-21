package sender;

import datagram.NetInfo;
import datagram.Segment;
import datagram.util.NumberConverter;
import datagram.util.SegmentHelper;

import java.util.Optional;


/**
 * STP协议中的流动窗口组件，用于控制数据报的发送
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Window {
    private byte[] data; //数据段队列
    private int mss; //数据段最大字节数
    private int mwss; //最大窗口字节数对于最大数据字节数的倍数
    private int headIndex; //窗口指针，指向第一个不在窗口中的字节号
    private int sendIndex; //发送指针，指向下一段即将被发送的字节号
    private int ackIndex; //响应指针，指向下一个应被响应的字节号
    private NetInfo info;

    Window(int mss, int mwss) {
        this.mwss = mwss;
        this.mss = mss;
        headIndex = mwss;
        sendIndex = 0;
        ackIndex = 0;
    }

    void setData(byte[] data) {
        this.data = data;
    }

    void setInfo(NetInfo info) {
        this.info = info;
    }

    /**
     * * 弹出一份STP报文，在发送指针已经到达窗口最前端时返回空值
     *
     * @param number 所需数据的第一字节序号
     * @return 包装好的Segment实例
     */
    Optional<Segment> pop(int number) {
        if (sendIndex < headIndex) {
            Segment segment = new Segment();

            //TODO load segment
            byte[] dataFrame = slide(number);
            segment.setPayload(dataFrame);

            segment.setSourceIp(NumberConverter.byteArrayToInt(info.getSourceIp()));
            segment.setDestIp(NumberConverter.byteArrayToInt(info.getDestIp()));
            segment.setSourcePort(NumberConverter.byteArrayToInt(info.getSourcePort()));
            segment.setDestPort(NumberConverter.byteArrayToInt(info.getDestPort()));

            segment.setSeqNumber(sendIndex);
            segment.setLength();
            segment.setWindowsSize(mwss * mss);
            SegmentHelper.calcCheckNum(segment);

            sendIndex += dataFrame.length;
            return Optional.of(segment);
        }
        return Optional.empty();
    }

    /**
     * 复制下一段数据
     *
     * @param number 所需数据的第一字节序号
     * @return 数据切片, 最大长度为mss
     */
    private byte[] slide(int number) {
        byte[] res = new byte[Math.min(mss, headIndex - sendIndex)];
        System.arraycopy(data, sendIndex, res, 0, res.length);
        return res;
    }

    /**
     * 响应服务端发回的响应号
     *
     * @param number 即将被发送数据段的第一字节序号
     */
    void ack(int number) {
        if (number > ackIndex && number <= sendIndex) {
            ackIndex = number;
            ahead();
        }
    }

    /**
     * 重整窗口大小，调整headIndex和sendIndex以放大或缩小窗口
     *
     * @param bytesReceivable 服务端可接收的字节数
     */
    void resize(int bytesReceivable) {
        if (bytesReceivable == mwss * mss)
            return;
        mwss = (int) Math.ceil((double) bytesReceivable / mss);
        headIndex = ackIndex + mwss;
        sendIndex = Math.min(headIndex, sendIndex);
    }


    /**
     * 尝试推进headIndex
     */
    private void ahead() {
        if (headIndex - ackIndex < mwss && headIndex <= data.length) {
            headIndex = Math.min(data.length, ackIndex + mwss);
        }
    }


}
