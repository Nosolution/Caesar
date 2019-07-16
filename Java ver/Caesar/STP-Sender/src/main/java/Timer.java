import java.time.Instant;

/**
 * 用于判断超时的计时器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Timer {
    private Instant origin;
    private int expiration;

    public void reset() {
        origin = Instant.now();
    }

    public void reset(int expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return origin.plusMillis(expiration * 1000).isBefore(Instant.now());
    }
}
