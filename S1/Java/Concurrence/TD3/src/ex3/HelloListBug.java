package ex3;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class HelloListBug {
	public static void main(String[] args) throws InterruptedException {
		var nbThreads = 4;
		var threads = new Thread[nbThreads];

		var list = new ArrayList<Integer>();

		IntStream.range(0, nbThreads).forEach(j -> {
			Runnable runnable = () -> {
				for (var i = 0; i < 5000; i++) {
					list.add(i);
				}
			};

			threads[j] = Thread.ofPlatform().start(runnable);
		});

		for (Thread thread : threads) {
			thread.join();
		}

		System.out.println("taille de la liste:" + list.size());
	}
}

/* Nouveau comportement observé :

    Exception in thread "Thread-0" java.lang.ArrayIndexOutOfBoundsException: Index 7136 out of bounds for length 6246
	at java.base/java.util.ArrayList.add(ArrayList.java:455)
	at java.base/java.util.ArrayList.add(ArrayList.java:467)
	at ex3.HelloListBug.lambda$1(HelloListBug.java:16)
	at java.base/java.lang.Thread.run(Thread.java:1589)
	
Exception in thread "Thread-2" java.lang.ArrayIndexOutOfBoundsException: Index 7133 out of bounds for length 6246
taille de la liste:9286
	at java.base/java.util.ArrayList.add(ArrayList.java:455)
	at java.base/java.util.ArrayList.add(ArrayList.java:467)
	at ex3.HelloListBug.lambda$1(HelloListBug.java:16)
	at java.base/java.lang.Thread.run(Thread.java:1589)
 */

/* Thread Main : Lance les 4 Threads
 * Thread A : appel Add, swap de thread avant le if
 * Thread B : appel add et affecte à une case x
 * Thread C : appel add et affecte à une case x + 1
 * Thread D : appel add et affecte à une case x + 2
 * ...
 * ...
 * ...
 * ...
 * Thread B : appel add et affecte à une case x + n
 * Thread A : reprend ou il a arreté, il ne rentre pas dans le if car la condition n'est pas respecté et il tente d'affecter
 * la valeur en dehors du tableau (car il n'a pas aggrandis la taille).
 */