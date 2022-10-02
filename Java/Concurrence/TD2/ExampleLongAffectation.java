class ExampleLongAffectation {
  long l = -1L;

  public static void main(String[] args) {
    var e = new ExampleLongAffectation();
    Thread.ofPlatform().start(() -> {
      System.out.println("l = " + e.l);
    });
    e.l = 0;
  }
}

//Affichage : l = -1 / l = 0

/* Scénario 1 :
 * Thread Main : Start Thread A 
 * Thread Main : print l = -1
 * 
 * Scénario 2 :
 * Thread Main : Start Thread A
 * Thread A : Assigne l = 0
 * Thread Main : print l = 0
 * 
 */