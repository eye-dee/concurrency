package tunnel;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

@Log4j2
@RequiredArgsConstructor
public class HealthCheck {
    private final Tunnel tunnel;
    private final DelayQueue<Task> delayQueue;

    public void check() {
        int i = 0;
        for (BlockingQueue<Train> trains : tunnel.getWays()) {
            ++i;
            log.warn("#{} way filled for {} / {}", i, trains.size(), Constants.TRAIN_PER_TUNNEL);
        }
    }
}
