package ex2;

import intermediate.Answer;
import intermediate.Request;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class Fastest {

    private final String item;
    private final int timeoutMilliPerRequest;

    private final ArrayList<Thread> threadArrayList = new ArrayList<>();


    public Fastest(String item, int timeoutMilliPerRequest) {
        this.item = item;
        this.timeoutMilliPerRequest = timeoutMilliPerRequest;
    }

    /**
     * @return the first site
     */
    public Optional<Answer> retrieve() throws InterruptedException {
        var list = new ArrayBlockingQueue<Answer>(Request.ALL_SITES.size());
        IntStream.range(0, Request.ALL_SITES.size()).forEach(index -> {
            threadArrayList.add(Thread.ofPlatform().start(()->{
                try{
                    var request = new Request(Request.ALL_SITES.get(index), this.item);
                    var answer = request.request(this.timeoutMilliPerRequest);
                    list.put(answer);
                }catch (InterruptedException e){
                }
            }));
        });
        threadArrayList.forEach(Thread::interrupt);
        for(var i = 0; i < Request.ALL_SITES.size(); i++){
            var response = list.take();
            if(response.isSuccessful()){
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) throws InterruptedException {
        var agregator = new Fastest("tortank", 2_000);
        var answer = agregator.retrieve();
        System.out.println(answer); // Optional[tortank@... : ...]
    }
}