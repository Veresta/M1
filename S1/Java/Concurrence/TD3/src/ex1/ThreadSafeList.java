package ex1;
import java.util.ArrayList;
import java.util.Objects;

public class ThreadSafeList<T> {

	private final ArrayList<T> liste;
	private final Object lock = new Object();

	public ThreadSafeList(int size) {
		if (size < 0)
			throw new IllegalArgumentException("negative size");
		liste = new ArrayList<T>(size);
	}

	public void add(T element) {
		Objects.requireNonNull(element);
		synchronized (lock) {
			liste.add(element);
		}
	}

	public int size() {
		synchronized (lock) {
			return liste.size();
		}
	}

	public void print() {
		synchronized (lock) {
			liste.stream().forEach(x -> System.out.println(x));
		}
	}
}

/*
 * Lock : -private final C'est un objet : ça ne peut être ni un primitif, ni
 * null;
 * 
 * C'est un objet Java qui n'utilise pas d'interning ;
 * 
 * On ne veut pas que n'importe qui puisse l'utiliser comme lock (au risque de
 * créer des blocages).
 * 
 * Cet objet sert de référence commune, de point de rendez-vous !
 * 
 */
