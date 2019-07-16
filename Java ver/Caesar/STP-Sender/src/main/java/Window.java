import java.util.List;

/**
 * STP协议中的窗口组件，用于控制数据报的发送
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Window {
    private List<byte[]> dataSlides;
    private long mws;
    private long mms;
    private int sendIndex;
    private int ackIndex;

    public Window(byte[] data, long mws, long mms) {
        this.mws = mws;
        this.mms = mms;
    }

}
