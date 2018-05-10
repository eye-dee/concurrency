package tunnel;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainHandler implements Runnable {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(2);
    private static final DelayQueue<Task> DELAY_QUEUE = new DelayQueue<>();
    private static final Tunnel TUNNEL = new Tunnel(DELAY_QUEUE);
    private static final TrainGeneratorService TRAIN_GENERATOR = new TrainGeneratorService();
    private static final MainHandler MAIN_HANDLER = new MainHandler();
    private static final HealthCheck HEALTH_CHECK = new HealthCheck(TUNNEL, DELAY_QUEUE);

    public static void main(String[] args) {
        EXECUTOR_SERVICE.scheduleWithFixedDelay(MAIN_HANDLER, 100, 100, TimeUnit.MILLISECONDS);
        EXECUTOR_SERVICE.schedule(MAIN_HANDLER::submitRandomTrain, 1, TimeUnit.SECONDS);
        EXECUTOR_SERVICE.schedule(TUNNEL::solve, 10, TimeUnit.MILLISECONDS);
        EXECUTOR_SERVICE.scheduleAtFixedRate(HEALTH_CHECK::check, 100, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        List<Task> tasks = new ArrayList<>();
        DELAY_QUEUE.drainTo(tasks);
        for (Task task : tasks) {
            task.getTask().run();
        }
    }

    public void submitRandomTrain() {
        TUNNEL.addTrain(TRAIN_GENERATOR.call());

        DELAY_QUEUE.add(new Task(RandomUtils.nextInt(10, 100), this::submitRandomTrain));
    }
}
