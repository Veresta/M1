package ex2;

import java.util.HashMap;
import java.util.Objects;

public class TemperatureManager {

    private final static Object lock = new Object();
    private final HashMap<String, Integer> temperature = new HashMap<>();
    private final int numberOfRooms;

    private int roomHadTemperatures = 0;

    public TemperatureManager(int nb_rooms){
        if(nb_rooms <= 0) throw new IllegalArgumentException();
        numberOfRooms = nb_rooms;

    }

    public void addTemperature(String room, int mesuredTemperature) throws InterruptedException {
        Objects.requireNonNull(room);
        synchronized (lock){
            if(!temperature.containsKey(room)){
                temperature.put(room,mesuredTemperature);
                System.out.println("Temperature in room " + room + " : " + mesuredTemperature);
                roomHadTemperatures++;
            }
            if(roomHadTemperatures == numberOfRooms) lock.notify();
        }
    }

    public void averageTemperature() throws InterruptedException {
        synchronized (lock) {
            while (roomHadTemperatures != numberOfRooms){
                lock.wait();
            }
            System.out.println(temperature.values().stream().mapToInt(Integer::intValue).average().getAsDouble());
        }
    }
}
