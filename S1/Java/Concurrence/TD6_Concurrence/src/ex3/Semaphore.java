package ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {

    private int nbOfPermits;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition releaseCondition = lock.newCondition();
    private final Condition acquireCondition = lock.newCondition();

    public Semaphore(int nbOfPermits){
        if (nbOfPermits <= 0) throw new IllegalArgumentException("Negative number");
        this.nbOfPermits = nbOfPermits;
    }

    public void release(){
        lock.lock();
        try {
            nbOfPermits++;
        }finally {
            lock.unlock();
        }
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while(nbOfPermits == 0){
                releaseCondition.await();
            }
            nbOfPermits--;
        }finally {
            lock.unlock();
        }
    }



}
