package ex4;

import java.util.Objects;

public class RendezVous<V> {
	private V value;
	private final Object lock = new Object();

	public void set(V value) {
		Objects.requireNonNull(value);
		this.value = value;
	}

	public V get() throws InterruptedException {
		synchronized (lock) {
			while (value == null) {
				Thread.sleep(1); // then comment this line !
			}
		}

		return value;
	}

}
