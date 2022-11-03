package ex1;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedSafeQueue<V> {

  private final ReentrantLock lock = new ReentrantLock();
  private final Condition putCondition = lock.newCondition();
  private final Condition takeCondition = lock.newCondition();
  private final ArrayDeque<V> fifo = new ArrayDeque<>();
  private final int capacity;

  public BoundedSafeQueue(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException();
    }
    this.capacity = capacity;
  }

  public void put(V value) throws InterruptedException {
      lock.lock();
      try {
        while (fifo.size() == capacity) {
          takeCondition.await();
        }
        fifo.add(value);
        putCondition.signal();
      }finally {
        lock.unlock();
      }
  }

  public V take() throws InterruptedException {
      lock.lock();
      try {
        while (fifo.isEmpty()) {
          putCondition.await();
        }
        takeCondition.signal();
        return fifo.remove();
      }finally {
        lock.unlock();
      }
  }

  public static void main(String[] args) throws InterruptedException {
    var test = new BoundedSafeQueue<String>(4);
    for(var i = 0; i < 3; i++) {
      Thread.ofPlatform().start(() -> {
        for(;;) {
          try {
            Thread.sleep(2000);
            test.put(Thread.currentThread().getName());
            System.out.println(test.take());
          } catch (InterruptedException e) {
            throw new AssertionError(e);
          }
        }

      });
    }
  }
}