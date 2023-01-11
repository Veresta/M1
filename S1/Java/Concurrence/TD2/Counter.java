public class Counter {
  private int value;

  public void addALot() {
    for (var i = 0; i < 100_000; i++) {
      this.value++;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    var counter = new Counter();
    var thread1 = Thread.ofPlatform().start(counter::addALot);
    var thread2 = Thread.ofPlatform().start(counter::addALot);
    thread1.join();
    thread2.join();
    System.out.println(counter.value);
  }
}


//Essayez d'expliquer ce que vous observez. Est-il possible que ce code affiche moins que 10000 ? Expliquer précisément pourquoi.
//On arrive pas à la valeur 200000.
//Les threads partagent le meme espace mémoire, ils incrémentent tout les deux la valeur value.
//Lorsqu'un thread incrémente la valeur, elle peut récuperer la valeur non incrémenter et donc perdre l'incrémentation.

