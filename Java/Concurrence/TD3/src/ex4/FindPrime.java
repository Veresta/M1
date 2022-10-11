package ex4;
import java.util.Random;

public class FindPrime {
	
  public static boolean isPrime(long l) {
    if (l <= 1)
      return false;
    for (long i = 2L; i <= l / 2; i++) {
      if (l % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) throws InterruptedException {
    var nbThreads = 4;
    //var rdv = new StupidRendezVous<Long>();
    var rdv = new RendezVous<Long>();
    for (var i = 0; i < nbThreads; i++) {
      var fi = i;
      Thread.ofPlatform().daemon().start(() -> {
        var random = new Random();
        for (;;) {
          var nb = 10_000_000_000L + (random.nextLong() % 10_000_000_000L);
          if (isPrime(nb)) {
            rdv.set(nb);
            System.out.println("A prime number was found in thread " + fi);
            return;
          }
        }
      });
    }
    Long prime = rdv.get();
    System.out.println("I found a large prime number : " + prime);
  }
}

/*
 * Que se passe-t-il lorsqu'on exécute ce code ?
 * 
 * Thread Main : Lance les 4 Threads
 * 
 * Autres Thread : Cherche un nb premier et lorsque un Thread à trouvé, ce fameux thread se termine, termine le thread main
 * Et comme ce sont des Threads démons et que le dernier thread utilisateur est terminé alors ils s'arretent.
 * 
 */

/*
 * Commenter l'instruction Thread.sleep(1) dans la méthode get puis ré-exécuter le code. Que se passe-t-il ? Expliquer où est le bug ?
 * 
 * Exécution de boucle infinie dans les threads démons, donc le thread main ne termine jamais.
 */

