package sender;

import datagram.NetInfo;
import datagram.Segment;
import datagram.util.NumberConverter;
import datagram.util.SegmentHelper;

import java.io.*;
import java.net.*;
import java.util.List;


/**
 * 发送端的主类
 *
 * @author Nosolution
 * @version 1.0
 * @since ${DATE}
 */
public class Sender {
    private String path;
    private String ip;
    private InetAddress address;
    private int sourcePort;
    private int destPort;
    private NetInfo info;
    private DatagramSocket ds;
    private Window window;
    private int time_out;
    private PLD pld;
    private List<Segment> pool;
    private static final int MPS = 576 * 1024;

    public static void main(String[] args) throws IOException {
        if (args.length != 11)
            throw new RuntimeException("The size of args is not as required.");
        String path = args[0];
        String ip = args[1];
        int destPort = Integer.valueOf(args[2]);
        double pDrop = Double.valueOf(args[3]);
        int seedDrop = Integer.valueOf(args[4]);
        int maxDelay = Integer.valueOf(args[5]);
        double pDelay = Double.valueOf(args[6]);
        int seedDelay = Integer.valueOf(args[7]);
        int mss = Integer.valueOf(args[8]);
        int mwss = Integer.valueOf(args[9]);
        int initialTimeout = Integer.valueOf(args[10]);

        Sender sender = new Sender(path, ip, destPort, pDrop, seedDrop, maxDelay, pDelay, seedDelay, mwss, mss, initialTimeout);
        sender.start();

    }

    private Sender(String path, String ip, int destPort, double pDrop, int seedDrop, int maxDelay, double pDelay, int seedDelay, int mss, int mwss, int initialTimeout) {
        this.path = path;
        this.ip = ip;
        this.destPort = destPort;
        this.pld = new PLD(pDrop, seedDrop, pDelay, maxDelay, seedDelay);
        this.window = new Window(mss, mwss);
        this.time_out = initialTimeout;
    }

    /**
     * 开始一次完整的传输
     *
     * @throws IOException 传输中发生的IO异常
     */
    private void start() throws IOException {
        connect();
        sendData();
        terminate();
    }

    /**
     * 连接接收端
     */
    private void connect() {

        try {
            ds.setSoTimeout(time_out * 1000);
            address = InetAddress.getByName(ip);
            do {
                sourcePort = 6000 + (int) (Math.random() * 3000);
            } while (!isUsable(sourcePort));

            info = new NetInfo(
                    address.getAddress(),
                    ds.getLocalAddress().getAddress(),
                    NumberConverter.intToByteArray(sourcePort),
                    NumberConverter.intToByteArray(destPort));
            window.setInfo(info);

            int x = (int) (Math.random() * (1 << 12));

            Segment s1 = new Segment();
            SegmentHelper.setNetInfo(s1, info);
            s1.setSYN();
            s1.setLength();
            s1.setSeqNumber(x);
            SegmentHelper.calcCheckNum(s1);

            DatagramPacket p1 = loadPacket(s1.toByteArray());
            ds.send(p1);

            byte[] buffer = new byte[MPS];
            DatagramPacket p2 = new DatagramPacket(buffer, buffer.length);
            try {
                ds.receive(p2);
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout. Link terminated.");
                ds.close();
                throw new RuntimeException("Timeout");
            }

            Segment response = Segment.parse(p2.getData());
            if (!(response.isSYN() && response.isACK() && response.getAckNumber() == x + 1))
                throw new RuntimeException("Not a sync response, terminated.");

            int y = response.getSeqNumber();
            Segment s2 = new Segment();
            SegmentHelper.setNetInfo(s2, info);
            s2.setACK();
            s2.setLength();
            s2.setSeqNumber(x + 1);
            s2.setAckNumber(y + 1);
            SegmentHelper.calcCheckNum(s2);

            DatagramPacket p3 = loadPacket(s2.toByteArray());
            ds.send(p3);

        } catch (IOException e) {
            e.printStackTrace();
            ds.close();
            throw new RuntimeException("Link terminated unexpectedly.");
        }

    }


    /**
     * 传输数据
     *
     * @throws IOException 传输过程中发生的IO异常
     */
    private void sendData() throws IOException {
        File file = new File(path);
        if (!file.exists())
            throw new RuntimeException("File doesn't exist.");
        byte[] data = null;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
            BufferedInputStream in = null;
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            data = bos.toByteArray();
            //TODO 全双工通信
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        window.setData(data);
    }

    /**
     * 终止连接
     */
    private void terminate() {
        try {
            int u = (int) (Math.random() * (1 << 12));

            Segment f1 = new Segment();
            f1.setFIN();
            f1.setLength();
            f1.setSeqNumber(u);
            f1.cancelACK();
            SegmentHelper.calcCheckNum(f1);

            DatagramPacket p1 = loadPacket(f1.toByteArray());
            ds.send(p1);

            try {
                byte[] buffer = new byte[MPS];
                DatagramPacket p2 = new DatagramPacket(buffer, buffer.length);
                ds.receive(p2);
                Segment r1 = Segment.parse(p2.getData());
                if (!(r1.isACK() && r1.getAckNumber() == u + 1)) {
                    throw new RuntimeException("Not a fin response, terminated.");
                }
                buffer = new byte[MPS];
                DatagramPacket p3 = new DatagramPacket(buffer, buffer.length);
                Segment r2 = Segment.parse(p3.getData());
                if (!(r2.isFIN() && r2.isACK() && r2.getAckNumber() == u + 1)) {
                    throw new RuntimeException("Not a fin response, terminated.");
                }
                int w = r2.getSeqNumber();

                Segment f2 = new Segment();
                f2.setACK();
                f2.setSeqNumber(u + 1);
                f2.setAckNumber(w + 1);
                f2.setLength();
                SegmentHelper.calcCheckNum(f2);

                DatagramPacket p4 = loadPacket(f2.toByteArray());
                ds.send(p4);


            } catch (SocketTimeoutException e) {
                System.out.println("Timeout. Link terminated.");
                throw new RuntimeException("Timeout");
            } finally {
                ds.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            ds.close();
            throw new RuntimeException("Link terminated unexpectedly.");
        }


    }

    /**
     * 判断本机某端口是否可用
     *
     * @param port 待判断的端口
     * @return {@code true}如果端口可用，否则为{@code false}
     */
    private boolean isUsable(int port) {
        try {
            new DatagramSocket(port).close();
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将待传输的数据装填进{@link DatagramPacket}中
     *
     * @param data 待传输的数据
     * @return 已装填网络信息和数据的{@link DatagramPacket}实例
     */
    private DatagramPacket loadPacket(byte[] data) {
        return new DatagramPacket(data, data.length, address, destPort);
    }


}
