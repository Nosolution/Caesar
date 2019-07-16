/**
 * 发送端的主类
 *
 * @author Nosolution
 * @version 1.0
 * @since ${DATE}
 */
public class Sender {
    public static void main(String[] args) {
        if (args.length != 11)
            throw new RuntimeException("The size of args is not as required.");
        String path = args[0];
        String ip = args[1];
        int port = Integer.valueOf(args[2]);
        double pDrop = Double.valueOf(args[3]);
        int seedDrop = Integer.valueOf(args[4]);
        int maxDelay = Integer.valueOf(args[5]);
        double pDelay = Double.valueOf(args[6]);
        int seedDelay = Integer.valueOf(args[7]);
        int mss = Integer.valueOf(args[8]);
        int mws = Integer.valueOf(args[9]) * mss;
        int initalTimeout = Integer.valueOf(args[10]);

    }
}
