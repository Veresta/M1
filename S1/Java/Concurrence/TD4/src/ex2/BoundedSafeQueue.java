package ex2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;

public class BoundedSafeQueue<V> {
	
	private Deque<V> list = new ArrayDeque<>(20);;
	private final Object lock = new Object();
	
	private void put(V value) throws InterruptedException {
		Objects.requireNonNull(value);
		synchronized(lock) {
			while(!(list.offerLast(value))) {
				lock.wait();
			}
			lock.notify();
		}
	}
	
	private V take() throws InterruptedException {
		synchronized (lock) {
			while (list.size() == 0) {
				lock.wait();
			}
			return list.removeFirst();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		var test = new BoundedSafeQueue<Integer>();
		for(var i = 0; i < 10; i++) {
			Thread.ofPlatform().start(() -> {
				for(;;) {
					try {
						Thread.sleep(2000);
						test.put(new Random().nextInt());
						System.out.println(test.take());
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			
			});
		}
	}
}
