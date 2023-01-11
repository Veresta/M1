package ex2;

public class HonorBoard {
	private String firstName;
	private String lastName;
	private final Object lock = new Object();

	public void set(String firstName, String lastName) {
		synchronized (lock) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

	@Override
	public String toString() {
		synchronized(lock) {
			return firstName + ' ' + lastName;
		}	
	}

	public static void main(String[] args) {
		var board = new HonorBoard();
		Thread.ofPlatform().start(() -> {
			for (;;) {
				board.set("Mickey", "Mouse");
			}
		});

		Thread.ofPlatform().start(() -> {
			for (;;) {
				board.set("Donald", "Duck");
			}
		});

		Thread.ofPlatform().start(() -> {
			for (;;) {
				System.out.println(board.firstName() + ' ' + board.lastName());
			}
		});
	}
}

/*
 * La classe n'est pas Thread Safe car exemple suivant :
 * 
 * Thread Main : Start Thread A 
 * Thread Main : Start Thread B 
 * Thread Main : Start Thread C 
 * Thread A : call set method and modify first name 
 * Thread B : call set method and modify first name 
 * Thread A : call set method and modify last name
 * Thread C : print board
 * 
 * ===> Donald Mouse
 */

/*
 * Non ce n'est pas possible car les champs ne sont pas final.
 * 
 */
