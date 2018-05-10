package tunnel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class Task implements Delayed {

    private final long creationTime = System.currentTimeMillis();
    private final int millis;
    private final Runnable task;

    @Override
    public long getDelay(TimeUnit unit) {
        return System.currentTimeMillis() - (creationTime + unit.convert(millis, TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(o.getDelay(TimeUnit.MILLISECONDS), getDelay(TimeUnit.MILLISECONDS));
    }
}
