package datagram;

import static datagram.util.SegmentHelper.link;

/**
 * STP的PDU
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Segment {
    private Header header;
    private byte[] payload;

    public static Segment parse(byte[] origin) {
        if (origin.length < Header.HEADER_LENGTH)
            throw new RuntimeException("Segment parsing failed.");
        byte[] headerPart = new byte[Header.HEADER_LENGTH];
        System.arraycopy(origin, 0, headerPart, 0, Header.HEADER_LENGTH);
        Segment segment = new Segment();
        segment.setHeader(Header.parse(headerPart));
        byte[] pl = new byte[origin.length - Header.HEADER_LENGTH];
        System.arraycopy(origin, Header.HEADER_LENGTH, pl, 0, pl.length);
        segment.setPayload(pl);
        return segment;
    }

    public byte[] toByteArray() {
        byte[] headerPart = header.toByteArray();
        return link(headerPart, payload);
    }

    public Segment() {
        header = new Header();
        this.payload = new byte[0];
    }

    public Segment(byte[] payload) {
        header = new Header();
        this.payload = payload;
    }

    private void setHeader(Header header) {
        this.header = header;
    }


    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }


    public int getSourceIp() {
        return header.getSourceIp();
    }

    public void setSourceIp(int sourceIp) {
        header.setSourceIp(sourceIp);
    }

    public int getDestIp() {
        return header.getDestIp();
    }

    public void setDestIp(int destIp) {
        header.setDestIp(destIp);
    }

    public short getLength() {
        return header.getLength();
    }

    public void setLength() {
        header.setLength(payload.length);
    }

    public short getSourcePort() {
        return header.getSourcePort();
    }

    public void setSourcePort(int sourcePort) {
        header.setSourcePort(sourcePort);
    }

    public short getDestPort() {
        return header.getDestPort();
    }

    public void setDestPort(int destPort) {
        header.setDestPort(destPort);
    }

    public int getSeqNumber() {
        return header.getSeqNumber();
    }

    public void setSeqNumber(int seqNumber) {
        header.setSeqNumber(seqNumber);
    }

    public int getAckNumber() {
        return header.getAckNumber();
    }


    public void setAckNumber(int ackNumber) {
        header.setAckNumber(ackNumber);
    }

    public short getWindowsSize() {
        return header.getWindowsSize();
    }

    public void setWindowsSize(int windowsSize) {
        header.setWindowsSize(windowsSize);
    }

    public short getCheckSum() {
        return header.getCheckSum();
    }

    public void setCheckSum(int checkSum) {
        header.setCheckSum(checkSum);
    }

    public boolean isURG() {
        return header.isURG();
    }

    public void setURG() {
        header.setURG();
    }

    public void cancelURG() {
        header.cancelURG();
    }

    public boolean isACK() {
        return header.isACK();
    }

    public void setACK() {
        header.setACK();
    }

    public void cancelACK() {
        header.cancelACK();
    }

    public boolean isPSH() {
        return header.isPSH();
    }


    public void setPSH() {
        header.setPSH();
    }

    public void cancelPSH() {
        header.cancelPSH();
    }

    public boolean isRST() {
        return header.isRST();
    }

    public void setRST() {
        header.setRST();
    }

    public void cancelRST() {
        header.cancelRST();
    }

    public boolean isSYN() {
        return header.isSYN();
    }

    public void setSYN() {
        header.setSYN();
    }

    public void cancelSYN() {
        header.cancelSYN();
    }

    public boolean isFIN() {
        return header.isFIN();
    }

    public void setFIN() {
        header.setFIN();
    }

    public void cancelFIN() {
        header.cancelFIN();
    }
}
