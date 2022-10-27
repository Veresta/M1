package ex2;

import java.util.List;

public class ApplicationBis {
    public static void main(String[] args) throws InterruptedException {
        var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

        var temperatures = new TemperatureManagerBis(rooms.size());

        for (String room : rooms) {
            Thread.ofPlatform().start(() -> {
                try {
                    for(;;){
                        temperatures.addTemperature(room, Heat4J.retrieveTemperature(room));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for(;;){
            temperatures.averageTemperature();
        }
    }
}
