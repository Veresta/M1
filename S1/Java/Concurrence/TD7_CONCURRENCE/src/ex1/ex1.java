package ex1;

public class ex1 {
    public static void main(String[] args) {
        Thread t = Thread.ofPlatform().start(() -> {
            for (var i = 1;; i++) {
                try {
                    Thread.sleep(1_000);
                    System.out.println("Thread slept " + i + " seconds.");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName());
                    return;
                }
            }
        });
        try{
            Thread.sleep(5_000);
            t.interrupt();
        }catch (InterruptedException e){
            return;
        }

    }
}

//Il n'est pas possible d'arrêter un thread de façon non-coopérative, il faut lui demander avec Interrupt
//récupérer un interrupt exception et terminer la méthode avec un return ou autre.

//Une opération non-bloquante renvoie un code indiquant qu'il n'a pas accès à la ressource.
//Une opération bloquante met le thread courant en attente en attendant que le ressource désirée soit disponible.

//La méthode Interupt() de la classe Thread envoie un signal d'interruption au thread appelant.
