//Author : Mathis MENAA

public class HelloThread {
	
	/**
	 * Create threads which each one print 5000 numbers in the terminal.
	 * @param nb
	 */
	private static void createThread(int nb) {
		for(int val = 0; val < nb; val++) {
			final int number = val;
			Thread.ofPlatform().start(() -> {
				for(int value = 0; value <= 5000; value++) {
					if(value != number) {System.out.println("hello " + number + " " +value);}
				}
			});
		}
	}
	
	public static void main(String[] args) {
		createThread(4);
	}
}
