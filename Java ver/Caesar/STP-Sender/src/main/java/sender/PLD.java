package sender;

/**
 * 数据包丢失和延迟模块，用于模仿UTP的不可靠传输。
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class PLD {
    private double pDrop;
    private int seedDrop;
    private double pDelay;
    private int maxDelay;
    private int seedDelay;

    PLD(double pDrop, int seedDrop, double pDelay, int maxDelay, int seedDelay) {
        this.pDrop = pDrop;
        this.seedDrop = seedDrop;
        this.pDelay = pDelay;
        this.maxDelay = maxDelay;
        this.seedDelay = seedDelay;
    }

    //TODO 增加逻辑
}
