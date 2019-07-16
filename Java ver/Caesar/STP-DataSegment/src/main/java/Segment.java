import java.io.InputStream;
import java.io.OutputStream;

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

    public static Segment parse(InputStream is) {
        return null;
    }

    public void writeToSteam(OutputStream os) {

    }

    public Segment() {
    }

    public Segment(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
