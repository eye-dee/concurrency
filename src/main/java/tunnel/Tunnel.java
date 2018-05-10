package tunnel;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;

@Log4j2
@Getter
public class Tunnel {
    private final Map<Duration, Queue<Train>> queue = new ConcurrentHashMap<>();
    private final DelayQueue<Task> delayQueue;
    private final List<BlockingQueue<Train>> ways = new ArrayList<>();

    Tunnel(DelayQueue<Task> delayQueue) {
        this.delayQueue = delayQueue;
        for (int i = 0; i < Constants.TUNNEL_AMOUNT; i++) {
            ways.add(new ArrayBlockingQueue<>(Constants.TRAIN_PER_TUNNEL, true));
        }
        for (Duration duration : Duration.values()) {
            queue.put(duration, new ConcurrentLinkedQueue<>());
        }
    }

    public void addTrain(final Train train) {
        log.info(train);
        queue.get(train.getDuration()).add(train);
    }

    public void solve() {
        for (BlockingQueue<Train> way : ways) {
            if (way.isEmpty()) {
                for (Duration duration : Duration.values()) {
                    val train = queue.get(duration).poll();
                    if (train != null) {
                        addIntoTunnel(way, train);
                        break;
                    }
                }
            }

            if (way.isEmpty()) {
                break;
            }

            val curDuration = way.element().getDuration();
            val train = queue.get(curDuration).poll();
            if (train != null) {
                addIntoTunnel(way, train);
            }

        }

        delayQueue.add(new Task(10, this::solve));
    }

    private void addIntoTunnel(BlockingQueue<Train> way, Train train) {
        way.add(train);
        log.info("{} into tunnel", train);
        delayQueue.add(new Task(Constants.TIME_IN_TUNNEL, () -> {
            way.remove(train);
            log.info("{} removed from tunnel", train);
        }));
    }
}
