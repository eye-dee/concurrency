package tunnel;

import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;
import java.util.concurrent.Callable;

public class TrainGeneratorService implements Callable<Train> {

    @Override
    public Train call() {
        return new Train(UUID.randomUUID().toString(),
                RandomUtils.nextBoolean() ? Duration.UP : Duration.DOWN);
    }
}
