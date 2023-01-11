package exercice1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CheapestPooledWithGlobalTimeout {

    private final String item;
    private final int poolSize;
    private final int timeoutMilliPerRequest;
    private final int timeoutMilliGlobal;

    public CheapestPooledWithGlobalTimeout(String item, int poolSize, int timeoutMilliPerRequest, int timeoutMilliGlobal) {
        this.item = item;
        this.poolSize = poolSize;
        this.timeoutMilliPerRequest = timeoutMilliPerRequest;
        this.timeoutMilliGlobal = timeoutMilliGlobal;
    }

    public Optional<Answer> retrieve() throws InterruptedException {
        var response = new ArrayList<Callable<Answer>>();
        var executorService = Executors.newFixedThreadPool(poolSize);
        IntStream.range(0, Request.ALL_SITES.size()).forEach(index -> response.add(() -> {
            var request = new Request(Request.ALL_SITES.get(index), item);
            return request.request(timeoutMilliPerRequest);
        }));
        var future = executorService.invokeAll(response, timeoutMilliGlobal, TimeUnit.MILLISECONDS);
        executorService.shutdown();
        return future.stream()
                .filter(tmp -> tmp.state() == Future.State.SUCCESS)
                .map(Future::resultNow)
                .filter(Answer::isSuccessful)
                .min(Answer::compareTo);
    }

    public static void main(String[] args) {
        var agregator = new CheapestPooledWithGlobalTimeout("pikachu", 10, 2_000, 1_000);
        try {
            var answer = agregator.retrieve();
            System.out.println(answer); // Optional[pikachu@darty.fr : 214]
        } catch (InterruptedException e) {
            throw new AssertionError("Interrupted");
        }


    }
}
