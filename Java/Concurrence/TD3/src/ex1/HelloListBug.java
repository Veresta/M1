package ex1;
import java.util.stream.IntStream;

public class HelloListBug {
	public static void main(String[] args) throws InterruptedException {
		var nbThreads = 4;
		var threads = new Thread[nbThreads];

		var list = new ThreadSafeList<Integer>(5_000 * nbThreads);

		IntStream.range(0, nbThreads).forEach(j -> {
			Runnable runnable = () -> {
				for (var i = 0; i < 5_000; i++) {
					list.add(i);
				}
			};

			threads[j] = Thread.ofPlatform().start(runnable);
		});

		for (Thread thread : threads) {
			thread.join();
		}

		System.out.println("taille de la liste:" + list.size());
		list.print();
	}
}