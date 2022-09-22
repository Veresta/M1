//Author : Mathis MENAA

public class HelloThreadBis {
	
	public static void println(String s){
		  for(var i = 0; i < s.length(); i++){
		    System.out.print(s.charAt(i));
		  }
		  System.out.print("\n");
	}
	
	/**
	 * Create threads which each one print 5000 numbers in the terminal.
	 * @param nb
	 */
	private static void createThread(int nb) {
		for(int val = 0; val < nb; val++) {
			final int number = val;
			Thread thread = Thread.ofPlatform().start(() -> {
				for(int value = 0; value <= 5000; value = value + 1) {
					if(value != number) {println("hello " + number + " " +value);}
				}
			});
		}
	}
	
	public static void main(String[] args) {
		createThread(4);
	}
	

	
}
