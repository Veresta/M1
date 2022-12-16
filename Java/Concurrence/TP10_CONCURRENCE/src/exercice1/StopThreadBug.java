package exercice1;

public class StopThreadBug {
  private volatile boolean stop;

  public void runCounter() {
    var localCounter = 0;
    for(;;) {
      if (stop) { //lecture volatile
        break;
      }
      localCounter++;
    }
    System.out.println(localCounter);
  }

  public void stop() {
    stop = true;
  } //écriture volatile

  public static void main(String[] args) throws InterruptedException {
    var bogus = new StopThreadBug();
    var thread = Thread.ofPlatform().start(bogus::runCounter);
    Thread.sleep(100);
    bogus.stop();
    thread.join();
  }
}

// Le problème vient du flag false.
// Le JIT optimise la méthode en créant un champ temporaire ou il stocke le champ false.
// Comme le champ est stocké à false dans la méthode, il ne la voit pas et il y a alors une boucle infinie.

// Pour rendre la classe Thread Safe, on rend le boolean volatile
// Le fait que le champ soit volatile empêche la réorganisation du JIT/CPU.