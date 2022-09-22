import java.util.ArrayList;

//Author : Mathis MENAA

public class HelloThreadJoin {

	/**
	 * Create threads which each one print 5000 numbers in the terminal. 
	 * When all thread are over print "le programme est fini".
	 * @param nb
	 * @throws InterruptedException
	 */
	private static void createThread(int nb) throws InterruptedException {
		ArrayList <Thread> threads = new ArrayList<>();
		for(int val = 0; val < nb; val = val + 1) {
			final int number = val;
			Thread thread = Thread.ofPlatform().start(() -> {
				for(int value = 0; value <= 5000; value++) {
					if(value != number) {System.out.println("hello " + number + " " +value);}
				}
			});
			threads.add(thread);
		}
		
		threads.stream().forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});

		System.out.println("le programme est fini");
	}
	
	public static void main(String[] args) throws InterruptedException {
		createThread(4);
		
	}
}
