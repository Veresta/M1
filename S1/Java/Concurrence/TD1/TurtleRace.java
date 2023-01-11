//Author : Mathis MENAA

public class TurtleRace {
	
	public static void main(String[] args) throws InterruptedException {
		  System.out.println("On your mark!");
		  Thread.sleep(30_000);
		  System.out.println("Go!");
		  int[] times = {25_000, 10_000, 20_000, 5_000, 50_000, 60_000};
		  for(int value = 0; value < times.length; value++) {
			  final int nb = value;
			  Thread.ofPlatform().start(() -> {
					try {
						Thread.sleep(times[nb]);
						System.out.println("Turtle " + nb + " has finished");
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				});
		  }
		}
}
