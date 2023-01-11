package ex2;

import java.util.HashMap;
import java.util.Objects;

public class TemperatureManagerBis {

    private final static Object lock = new Object();
    private final HashMap<String, Integer> temperature = new HashMap<>();
    private final int numberOfRooms;
    private int roomHadTemperatures = 0;
    private boolean hasFinished = false;


    public TemperatureManagerBis(int numbRooms){
        if(numbRooms <= 0) throw new IllegalArgumentException();
        numberOfRooms = numbRooms;
    }

    /*
    Ajoute la température de la pièce room dans la hashmap des temperatures si elle a pas déjà été calculé.
    Si toutes les pièces ont leur température mesuré alors, on passe le flag hasFinished à True et notify la fonction de print.
    Ensuite tant que toutes les pièces ont pas de température, on fait wait le thread.
     */
    public void addTemperature(String room, int mesuredTemperature) throws InterruptedException {
        Objects.requireNonNull(room);
        synchronized (lock){

            if(!temperature.containsKey(room)){
                temperature.put(room,mesuredTemperature);
                System.out.println("Temperature in room " + room + " : " + mesuredTemperature);
                roomHadTemperatures++;
            }

            if(roomHadTemperatures==numberOfRooms) {
                hasFinished = true;
                lock.notify();
            }

            while (roomHadTemperatures != numberOfRooms){
                lock.wait();
                if(!hasFinished) break;
            }
        }
    }

    /*
        Affiche la température moyenne des pièces.
        Tant que toutes les pièces n'ont pas eu leur température mesurées, on fait wait le thread courant.
        Une fois toutes les températures capturés, on affiche la moyenne, on reset nos valeurs et on notifyAll.
     */
    public void averageTemperature() throws InterruptedException {
        synchronized (lock) {
            while (!hasFinished){
                lock.wait();
            }
            System.out.println(temperature.values().stream().mapToInt(Integer::intValue).average().getAsDouble());
            temperature.clear();
            hasFinished = false;
            roomHadTemperatures = 0;
            lock.notifyAll();
        }
    }
}