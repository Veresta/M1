package intermediate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

public class CheapestSequential {

    private final String item;
    private final int timeoutMilliPerRequest;

    public CheapestSequential(String item, int timeoutMilliPerRequest) {
        this.item = item;
        this.timeoutMilliPerRequest = timeoutMilliPerRequest;
    }

    /**
     * @return the cheapest price for item if it is sold
     */
    public Optional<Answer> retrieve() throws InterruptedException {
        var list = new ArrayList<Answer>();
        for(var elem : Request.ALL_SITES){
            var request = new Request(elem,this.item);
            var answer = request.request(this.timeoutMilliPerRequest);
            if (answer.isSuccessful()) {
                System.out.println("The price is " + answer.price());
                list.add(answer);
            } else {
                System.out.println("The price could not be retrieved from the site");
            }
        }
        return list.stream().min(Answer.ANSWER_COMPARATOR);
    }

    public static void main(String[] args) throws InterruptedException {
        var agregator = new CheapestSequential("pikachu", 2_000);
        var answer = agregator.retrieve();
        System.out.println(answer); // Optional[pikachu@darty.fr : 214]
    }
}