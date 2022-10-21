package ex1;

import java.util.concurrent.Exchanger;

public class ExchangerExample {
	
	public static void print(Exchanger e) throws InterruptedException {
		System.out.println("thread 1 " + e.exchange("foo1"));
	}
	
	
  public static void main(String[] args) throws InterruptedException {
    var exchanger = new AdvancedExchange<String>();
    Thread.ofPlatform().start(() -> {
      try {
        System.out.println("thread 1 " + exchanger.exchange("foo1"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    System.out.println("main " + exchanger.exchange(null));
  }
}

/* Q1)
 * Scénario 1 : thread1 foo1 / main null 
 * 
 * MAIN : Start le Thread A
 * THREAD A : Execute la méthode sur foo1 et print thread 1 : foo1
 * MAIN : Execute la méthode exchange sur null et print main : null
 * 
 * Scénario 2 : thread1 null / main foo1
 * MAIN : Start le Thread A
 * MAIN : Execute la méthode exchange sur null et deschedule le MAIN
 * Thread A : Execute la methode exchange sur foo1 et renvoie null puis print
 * MAIN : Execute la méthode exchange sur null et renvoie la valeur précedement appelé par exchange donc print foo1.
 * 
 * 
 * Scénario 3 : main foo1 / thread 1 null
 * 
 * 
 * Q2) On déclare un boolean qui vaut false, on synchronized les actions des threads avec un lock
 * dès que le première appel est passé on passe le boolean a true.
 * 
 * */
