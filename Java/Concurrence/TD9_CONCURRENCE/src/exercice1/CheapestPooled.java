package exercice1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class CheapestPooled {

    private final String item;

    private final int timeoutMilliPerRequest;

    public CheapestPooled(String item, int timeoutMilliPerRequest) {
        this.item = item;
        this.timeoutMilliPerRequest = timeoutMilliPerRequest;
    }

    public Optional<Answer> retrieve() throws InterruptedException {
        var response = new ArrayList<Callable<Answer>>();
        var executorService = Executors.newFixedThreadPool(Request.ALL_SITES.size());
        IntStream.range(0, Request.ALL_SITES.size()).forEach(index -> response.add(() -> {
            var request = new Request(Request.ALL_SITES.get(index), item);
            return request.request(timeoutMilliPerRequest);
        }));
        var future = executorService.invokeAll(response);
        executorService.shutdown();
        return future.stream()
                .filter(tmp -> tmp.state() == Future.State.SUCCESS)
                .map(Future::resultNow)
                .filter(Answer::isSuccessful)
                .min(Answer::compareTo);
    }


    public static void main(String[] args) throws InterruptedException {
        var agregator = new CheapestPooled("pikachu", 2_000);
        var answer = agregator.retrieve();
        System.out.println(answer); // Optional[pikachu@darty.fr : 214]
    }
}
