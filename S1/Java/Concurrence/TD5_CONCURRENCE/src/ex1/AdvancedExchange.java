package ex1;

public class AdvancedExchange<E> {
	
	private final Object lock  = new Object();
	private E value;
	private STATE state;
	
	public AdvancedExchange(){
		state = STATE.NO_VALUE;
	}
	
	
	public E exchange(E entry) throws InterruptedException {
		synchronized(lock) {
			return switch(state) {
			case NO_VALUE -> {
				value = entry;
				state = STATE.VAL1HERE;
				while(state.equals(STATE.VAL1HERE)) {
					lock.wait();
				}
				yield value;
				}
			case VAL1HERE -> {
				var tmp = value;
				value = entry;
				state = STATE.NO_VALUE;
				lock.notify();
				yield tmp;
				}
			};
		}
	}	
}
