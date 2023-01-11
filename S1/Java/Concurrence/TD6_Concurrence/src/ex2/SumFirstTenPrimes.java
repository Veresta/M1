package ex2;

import java.util.Random;

public class SumFirstTenPrimes {

    private static boolean isPrime(long l) {
        if (l <= 1) {
            return false;
        }
        for (long i = 2L; i <= l / 2; i++) {
            if (l % i == 0) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) throws InterruptedException {
        var tmp = new MyThreadSafeClass();
        for(var i = 0; i < 5; i++){
            Thread.ofPlatform().daemon().start(()->{
                var random = new Random();
                for (;;) {
                    long nb = 1_000_000_000L + (random.nextLong() % 1_000_000_000L);
                    if (isPrime(nb)) {
                        tmp.addPrime(nb);
                    }
                }
            });
        }
        System.out.println("Somme : " + tmp.waitTenPrime());
    }
}
