/**
 * {@link Segment}Segment的首部
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Header {

    private int sourcePort;
    private int destPort;
    private int seqNumber;
    private int ackNumber;
    private int codeByte;
    private int windowsSize;
    private int checkSum;

    public byte[] toByteArray() {
        return null;
    }

    public Header fromByteArray(byte[] origin) {
        return null;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getAckNumber() {
        return ackNumber;
    }

    public void setAckNumber(int ackNumber) {
        this.ackNumber = ackNumber;
    }

    public int getCodeByte() {
        return codeByte;
    }

    public void setCodeByte(int codeByte) {
        this.codeByte = codeByte;
    }

    public int getWindowsSize() {
        return windowsSize;
    }

    public void setWindowsSize(int windowsSize) {
        this.windowsSize = windowsSize;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }
}
