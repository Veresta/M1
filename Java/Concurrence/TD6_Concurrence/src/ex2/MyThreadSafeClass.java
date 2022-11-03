package ex2;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadSafeClass {

    private int nbFound;

    private final ArrayList<Long> primeList = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition waitCondition = lock.newCondition();
    private final static int GOAL = 10;


    public void addPrime(long prime){
        lock.lock();
        try {
            if(nbFound == GOAL){
                waitCondition.signal();
                return;
            }
            primeList.add(prime);
            nbFound++;
        } finally {
            lock.unlock();
        }
    }

    public long waitTenPrime() throws InterruptedException {
        lock.lock();
        try {
            while (nbFound != GOAL){
                waitCondition.await();
            }
            return primeList.stream().mapToLong(Long::valueOf).sum();
        }finally {
            lock.unlock();
        }

    }
}
