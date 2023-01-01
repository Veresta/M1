package exercice2;

import java.util.concurrent.atomic.AtomicLong;

public class RandomNumberGenerator {
  private final AtomicLong x;
  
  public RandomNumberGenerator(long seed) {
    if (seed == 0) {
      throw new IllegalArgumentException("seed == 0");
    }
    x = new AtomicLong(seed);
  }
  
  public long next() {  // Marsaglia's XorShift
    x.getAndUpdate(x -> ((x ^ (x >>> 12)) ^ (x << 25) ^ (x >>> 27))* 2685821657736338717L);
    return x.get();
  }
  
  public static void main(String[] args) {
    RandomNumberGenerator rng = new RandomNumberGenerator(10);
    for(var i = 0; i < 5_000; i++) {
      System.out.println(rng.next());
    }
  }
}

// Un générateur aléatoire fonctionne à l'aide d'une graine (seed) que l'utilisateur défini.
// À partir de cette graine, le générateur va generate les nombres aléatoires suivants et ainsi de suite.
// Cela implique que la suite de chiffre générer pour une seed donnée sera toujours la meme.

// La classe n'est pas thread safe à cause des potentilles data race sur la seed x.