package exercice1;

import java.util.Objects;

public class HonorBoard {
  private volatile nameInstance instance;
  private record nameInstance(String firstName, String lastName){
    @Override
    public String toString() {
      return firstName + " " + lastName;
    }
  }
  
  public void set(String firstName, String lastName) {
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);
    this.instance = new nameInstance(firstName, lastName); // écriture volatile
  }
  
  @Override
  public String toString() {
    return instance.toString(); // lecture volatile
  }
  
  public static void main(String[] args) {
    var board = new HonorBoard();
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Mickey", "Mouse");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Donald", "Duck");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        System.out.println(board);
      }
    });
  }
}

// Oui il est toujours possible d'obtenir ces affichages (Donal Duck / Mickey Mouse)