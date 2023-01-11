package ex3;

import java.util.HashMap;
import java.util.Objects;

public class ThePriceIsRight {

    private final Object lock = new Object();
    private final int realPrice;
    private final int participants;

    private int nbPropose;

    private final HashMap<String, Integer> proposition = new HashMap<>();

    private boolean allHasPropose;

    public ThePriceIsRight(int prix, int participants){
        if(prix < 0 || participants <= 0) throw new IllegalArgumentException();
        this.realPrice = prix;
        this.participants = participants;
    }

    public boolean propose(int value){
        synchronized (lock) {
            if (proposition.containsKey(Thread.currentThread().getName())) {
                return false;
            }

            proposition.put(Thread.currentThread().getName(), value);
            nbPropose += 1;
            return true;
        }
    }

    private int distance(int price) {
        return Math.abs(price - realPrice);
    }
}
