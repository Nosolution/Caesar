package datagram;

/**
 * STP套接字的网络信息
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/19
 */
public class NetInfo {
    private byte[] sourceIp;
    private byte[] destIp;
    private byte[] sourcePort;
    private byte[] destPort;

    public NetInfo(byte[] sourceIp, byte[] destIp, byte[] sourcePort, byte[] destPort) {
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.sourcePort = sourcePort;
        this.destPort = destPort;
    }

    public byte[] getSourceIp() {
        return sourceIp;
    }

    public byte[] getDestIp() {
        return destIp;
    }

    public byte[] getSourcePort() {
        return sourcePort;
    }

    public byte[] getDestPort() {
        return destPort;
    }
}