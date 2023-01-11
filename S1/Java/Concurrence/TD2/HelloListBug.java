import java.util.ArrayList;
import java.util.stream.IntStream;

public class HelloListBug {
	
	public static void main(String[] args) throws InterruptedException {
		  var nbThreads = 4;
		  var threads = new Thread[nbThreads];
		  var list = new ArrayList<Integer>(5000 * nbThreads);

		  IntStream.range(0, nbThreads).forEach(j -> {
		    Runnable runnable = () -> {
		      for (var i = 0; i < 5000; i++) {
		        //System.out.println("hello " + j + " " + i);
		        list.add(i);
		      }
		    };
		    threads[j] = Thread.ofPlatform().start(runnable);
		  });

		  for (var thread : threads) {
		    thread.join(); 
		  }

		  System.out.println("le programme est fini");
		  System.out.println("Taille :" + list.size());
		}
}

/* On remarque que l'affichage est différent entre chaque exécution, quelques exemples : 18070, 15807, 8410..
 * 
 * On se demande pourquoi la taille n'est pas égal à celle de la liste c'est à dire 20 000.
 * Le problème est le meme pour celui de l'exo 0, lorsque l'on observe de plus près la déclaration de la méthode Add,
 * on voit qu'après l'ajout, il y'a une incrémentation de la taille de la liste or juste avant cette etape, le scheduler peut très bien
 * passé la main à un autre thread et ne pas exécuter l'incrémentation, de ce fait on perd peu perdre l'information de l'ajout.
 * 
 */