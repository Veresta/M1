package ex1;

public class ExchangerPerso<E> {
	
	private final Object lock  = new Object();
	private boolean firstAffectation;
	private E value;
	
	public E exchange(E entry) throws InterruptedException {
		synchronized(lock) {
			if(!firstAffectation) {
				value = entry;
				firstAffectation = true;
				while(firstAffectation) {
					lock.wait();
				}
				return value;
			}
			else{
				var tmp = value;
				value = entry;
				firstAffectation = false;
				lock.notify();
				return tmp;
			}
		}
	}
}
