package ex1;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UnboundedSafeQueue<V> {

  private final ReentrantLock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private final ArrayDeque<V> fifo = new ArrayDeque<>();

  public void add(V value) {
    lock.lock();
    try {
      fifo.add(value);
      condition.signal();
    }finally {
      lock.unlock();
    }
  }

  public V take() throws InterruptedException {

    lock.lock();
    try {
      while (fifo.isEmpty()) {
        condition.await();
      }
      return fifo.remove();
    }finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    var test = new UnboundedSafeQueue<String>();
    for(var i = 0; i < 3; i++) {
      Thread.ofPlatform().start(() -> {
        for(;;) {
          try {
            Thread.sleep(2000);
            test.add(Thread.currentThread().getName());
            System.out.println(test.take());
          } catch (InterruptedException e) {
            throw new AssertionError(e);
          }
        }

      });
    }
  }
}