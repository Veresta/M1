package exercice1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.HashSet;

public class RandomNumberGenerator {
  private volatile long x;
  private static final VarHandle X_HANDLE;

  static {
    var lookup = MethodHandles.lookup();
    try{
      X_HANDLE = lookup.findVarHandle(RandomNumberGenerator.class, "x", long.class);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }

  public RandomNumberGenerator(long seed) {
    if (seed == 0) {
      throw new IllegalArgumentException("seed == 0");
    }
    x = seed;
  }

  private long calcul(long tmp){
    var res = tmp ^ (tmp >>> 12) ;
    res ^= (tmp << 25);
    res ^= (tmp >>> 27);
    return res*2685821657736338717L;
  }

  public long next() {  // Marsaglia's XorShift
    for(;;){
      var oldvalue = x;
      var newValue = calcul(oldvalue);
      if(X_HANDLE.compareAndSet(this, oldvalue, newValue)){
        return newValue;
      }
    }
    //x.getAndUpdate(x -> ((x ^ (x >>> 12)) ^ (x << 25) ^ (x >>> 27))* 2685821657736338717L);
    //return x.get();
  }
  
  public static void main(String[] args) throws InterruptedException {
    var set0 = new HashSet<Long>();
    var set1 = new HashSet<Long>();
    var set2 = new HashSet<Long>();
    var rng0 = new RandomNumberGenerator(1);
    var rng = new RandomNumberGenerator(1);

    for (int i = 0; i < 10_000; i++) {
      set0.add(rng0.next());
    }

    var thread = Thread.ofPlatform().start(() -> {
      for (var i = 0; i < 5_000; i++) {
        set1.add(rng.next());
      }
    });

    for (var i = 0; i < 5_000; i++) {
      // System.out.println(rng.next());
      set2.add(rng.next());
    }
    thread.join();

    System.out.println("set1: " + set1.size() + ", set2: " + set2.size());
    set1.addAll(set2);
    System.out.println("union (should be 10000): " + set1.size());

    System.out.println("inter (should be true): " + set0.containsAll(set1));
    set0.removeAll(set1);
    System.out.println("inter (should be 0): " + set0.size());
  }
}