public class StopThreadBug implements Runnable {
  private boolean stop = false;

  public void stop() {
    stop = true;
  }

  @Override
  public void run() {
    while (!stop) {
      System.out.println("Up");
    }
    System.out.print("Done");
  }

  public static void main(String[] args) throws InterruptedException {
    var stopThreadBug = new StopThreadBug();
    Thread.ofPlatform().start(stopThreadBug::run);
    Thread.sleep(5_000);
    System.out.println("Trying to tell the thread to stop");
    stopThreadBug.stop();
  }
}

//La data-race est le boolean stop.

//Jit peut créer une variable local dans la méthode run, ce qui fait qu'elle n'est jamais actualisé
//donc boucle infinie.

/*
THREAD MAIN : Lance le Thread A 

THREAD A : Dans la méthode run, JIT créer une copie en local de la variable Stop. Ne garde pas l'actualisation

--> Boucle infinie
*/