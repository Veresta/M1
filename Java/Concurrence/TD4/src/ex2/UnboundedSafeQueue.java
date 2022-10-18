package ex2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class UnboundedSafeQueue<V> {

	private Deque<V> waitList = new ArrayDeque<>();
	private final Object lock = new Object();

	private void add(V value) {
		Objects.requireNonNull(value);
		synchronized (lock) {
			waitList.addLast(value);
			lock.notify();
		}

	}

	private V take() throws InterruptedException {
		synchronized (lock) {
			while (waitList.size() == 0) {
				lock.wait();
			}
			return waitList.removeFirst();
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
