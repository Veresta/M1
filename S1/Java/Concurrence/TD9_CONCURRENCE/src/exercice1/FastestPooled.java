package exercice1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class FastestPooled {
    private final String item;

    private final int timeoutMilliPerRequest;

    public FastestPooled(String item, int timeoutMilliPerRequest) {
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
        try {
            var future = executorService.invokeAny(response);
            executorService.shutdown();
            return Optional.of(future);
        } catch (ExecutionException e) {
            executorService.shutdown();
            System.out.println("Pas de réponse");
            return Optional.empty();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        var agregator = new FastestPooled("tortank", 2_000);
        var answer = agregator.retrieve();
        System.out.println(answer); // Optional[pikachu@darty.fr : 214]
    }
}
