class ExempleReordering {
  int a = 0;
  int b = 0;

  public static void main(String[] args) {
    var e = new ExempleReordering();
    Thread.ofPlatform().start(() -> {
      System.out.println("a = " + e.a + "  b = " + e.b);
    });
    e.a = 1;
    e.b = 2;
  }
}

//Affichage : a = 1  b = 2 / a = 0 b = 0 / a = 1 b = 0

/* Scénario 1 :
 * Thread Main : Start le Thread A
 * Thread A : Effectue l'assignation a = 1
 * Thread Main : print a = 1 b = 0
 * 
 * Scénario 2 :
 * Thread Main : Start le Thread A
 * Thread Main : print a = 0 b = 0
 * 
 * Scénario 3 :
 * Thread Main : Start le Thread A
 * Thread A :  Effectue l'assignation a = 1
 * Thread A :  Effectue l'assignation b = 2
 * Thread Main : print a = 1 b = 2
 * 
 */
