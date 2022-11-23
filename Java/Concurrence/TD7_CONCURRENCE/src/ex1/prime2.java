package ex1;

import java.util.OptionalLong;
import java.util.concurrent.ThreadLocalRandom;

public class prime2 {
    public static boolean isPrime(long candidate) {
        var isInterrupt = false;
        if (candidate <= 1) {
            return false;
        }
        for (var i = 2; i <= Math.sqrt(candidate) && !isInterrupt; i++) {
            isInterrupt = Thread.interrupted();
            if (candidate % i == 0) {
                return false;
            }
        }
        if(isInterrupt){
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    public static OptionalLong findPrime() {
        var generator = ThreadLocalRandom.current();
        while (!Thread.interrupted()) {
            var candidate = generator.nextLong();
            if (isPrime(candidate)) {
                return OptionalLong.of(candidate);
            }
        }
        //Thread.currentThread().interrupt();
        return OptionalLong.empty();
    }

    public static void main(String[] args) {
        Thread t = Thread.ofPlatform().start(() -> {
            var tmp = findPrime();
            if(tmp.isPresent()){
                System.out.println("Found a random prime : " + tmp.orElseThrow());
            }
        });

        try{
            Thread.sleep(3_000);
            t.interrupt();
            System.out.println("TROP TARD");
        }catch(InterruptedException ignored){
            throw new AssertionError("err");
        }
    }
}
