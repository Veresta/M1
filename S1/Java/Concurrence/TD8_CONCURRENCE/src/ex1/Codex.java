package ex1;

import java.util.concurrent.ArrayBlockingQueue;

public class Codex {
    public static void main(String[] args) {
        var queue = new ArrayBlockingQueue<String>(1);
        var decode = new ArrayBlockingQueue<String>(1);
        for(int i = 0; i < 3; i++){
            Thread.ofPlatform().start(() -> {
                while(!Thread.interrupted()){
                    try{
                        var res = CodeAPI.receive();
                        System.out.println("Reception : " + res);
                        queue.put(res);
                    }catch (InterruptedException e){
                        throw new AssertionError();
                    }
                }
            });
        }

        for(int j = 0; j < 2; j++){
            Thread.ofPlatform().start(()->{
                while(!Thread.interrupted()){
                    try{
                        var rep = CodeAPI.decode(queue.take());
                        System.out.println("Decode : " + rep);
                        decode.put(rep);
                    }catch (InterruptedException e){
                        throw new AssertionError();
                    }catch (IllegalArgumentException a){
                        continue; // Reprend la boucle
                    }
                }
            });
        }

        Thread.ofPlatform().start(()->{
            while(!Thread.interrupted()){
                try{
                    CodeAPI.archive(decode.take());
                }catch (InterruptedException e){
                    throw new AssertionError();
                }
            }
        });
    }
}
