package sender;

import java.time.Instant;

/**
 * 用于判断超时的计时器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/7/16
 */
public class Timer {
    private Instant instant; //计时时刻
    private int expiration; //过期长度，单位为秒

    public Timer(int expiration) {
        this.expiration = expiration;
    }

    public void reset() {
        instant = Instant.now();
    }

    public void set(int expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return instant.plusMillis(expiration * 1000).isBefore(Instant.now());
    }
}
