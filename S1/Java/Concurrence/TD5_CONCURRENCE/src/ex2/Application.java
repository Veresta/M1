package ex2;

import java.util.List;

public class Application {
  public static void main(String[] args) throws InterruptedException {
    var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

    var temperatures = new TemperatureManager(rooms.size());

    for (String room : rooms) {
      Thread.ofPlatform().start(() -> {
        try {
          temperatures.addTemperature(room, Heat4J.retrieveTemperature(room));
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
    }
    temperatures.averageTemperature();
  }
}