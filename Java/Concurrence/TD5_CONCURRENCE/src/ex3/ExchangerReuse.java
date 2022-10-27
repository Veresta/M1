package ex3;

import java.util.Objects;

public class ExchangerReuse<T> {
    private final Object lock  = new Object();
    private T value;
    private T othervalue;
    private STATE state;

    public ExchangerReuse(){
        state = STATE.BEGIN;
    }

    public T exchange(T entry) throws InterruptedException {
        Objects.requireNonNull(entry);
        synchronized (lock) {

            if(state == STATE.BEGIN){
                value = entry;
                state = STATE.IN_PROGRESS;
                while(state != STATE.BEGIN){
                    lock.wait();
                }
                return othervalue;
            }

            if(state == STATE.IN_PROGRESS){
                othervalue = entry;
                state = STATE.FINISHED;
                while (state != STATE.BEGIN){
                    lock.wait();
                }
            }

            if(state == STATE.FINISHED){
                state = STATE.BEGIN;
                lock.notifyAll();
            }

            return value;
        }
    }
}
