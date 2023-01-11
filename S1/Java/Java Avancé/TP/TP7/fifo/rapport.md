# Compte rendu TP7 Java Avancé

------

## Exercice 2: Fifo

##### 1) Cette représentation peut poser un problème, car si la tête et la queue correspondent au même indice, il n'est pas facile de détecter si cela veux dire que la file est pleine ou vide.
##### Comment doit-on faire pour détecter si la file est pleine ou vide ? 

A size field is initialized and incremented or decremented each time something is added or deleted.

##### 2) Écrire une classe Fifo générique (avec une variable de type E) dans le package fr.uge.fifo prenant en paramètre le nombre maximal d’éléments que peut stocker la structure de données. Pensez à vérifier les préconditions.

````java
package fr.uge.fifo;

public class Fifo<E> {

    private int size;
    private final E[] fifo;
    private int tail;

    @SuppressWarnings("unchecked")
    public Fifo(int length){
        if(length <= 0) throw new IllegalArgumentException("Negative length");
        fifo = (E[] ) new Object[length];
    }
}
````

##### 3) Écrire la méthode offer qui ajoute un élément de type E dans la file. Pensez à vérifier les préconditions sachant que, notamment, on veut interdire le stockage de null.
````java
    public void offer(E value) {
        Objects.requireNonNull(value);
        if(size == fifo.length) throw new IllegalStateException("Fifo full");
        fifo[tail] = value;
        tail = (tail == fifo.length-1 ? 0 : tail + 1);
        size++;
    }
````
##### Comment détecter que la file est pleine ?

Look if the size field is equal to the size of the array.

##### Que faire si la file est pleine ?

Give up the add and throw an exception.

##### 4) Écrire une méthode poll qui retire un élément de type E de la file. Penser à vérifier les préconditions.

````java
    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = fifo[head];
        size--;
        head = (head == fifo.length-1 ? 0 : head + 1);
        return res;
    }
````


##### Que faire si la file est vide ?

Throw an exception.

##### 5) Ajouter une méthode d'affichage qui affiche les éléments dans l'ordre dans lequel ils seraient sortis en utilisant poll. L'ensemble des éléments devra être affiché entre crochets ('[' et ']') avec les éléments séparés par des virgules (suivies d'un espace).
```java
    @Override
    public String toString(){
        var joiner = new StringJoiner(", ","[","]");
        var tmp_head = head;
        for(var i = 0; i < size; i++){
            joiner.add(fifo[tmp_head].toString());
            tmp_head++;
            if(tmp_head >= fifo.length) tmp_head = 0;
        }
        return joiner.toString();
    }
```

##### 6) Rappelez ce qu'est un memory leak en Java et assurez-vous que votre implantation n'a pas ce comportement indésirable.

A "memory leak" is an uncontrolled drift in memory usage.
It's often due to an object that keeps references to a multitude of others,
which prevents the garbage collector from removing them from the memory.
The memory grows more and more and ends up saturated


To correct this in our implementation, put "null" in place of the removed objects.

````java
    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = fifo[head];
        fifo[head] = null;
        size--;
        head = (head == fifo.length-1 ? 0 : head + 1);
        return res;
    }
````

##### 7) Ajouter une méthode size et une méthode isEmpty.
````java
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
````

##### 8) Rappelez quel est le principe d'un itérateur.

Object that allows to browse a collection.
Object that has a method __next__ that allows to navigate to the next element of a set and
__has_next__ if there is a next element.

##### Quel doit être le type de retour de la méthode iterator() ?

Boolean

##### 9) Implanter la méthode iterator().

```java
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int currentIndex = head;
            private int numberIteration;

            @Override
            public boolean hasNext() {
                return numberIteration < size;
            }

            @Override
            public E next() {
                if(currentIndex >= size)  throw new NoSuchElementException("No element found");
                var res = fifo[currentIndex];
                numberIteration++;
                currentIndex = (currentIndex == fifo.length-1 ? 0 : currentIndex + 1);
                return res;
            }
        };
    }
```

##### 10) Rappeler à quoi sert l'interface Iterable. 

This interface allows an object to be the target of the "for-each loop" statement.

##### Faire en sorte que votre file soit Iterable.

We just need to implement the Iterable interface to our class.


## Exercice 3: ResizeableFifo

##### 1) Indiquer comment agrandir la file si celle-ci est pleine et que l'on veut doubler sa taille. Attention, il faut penser au cas où le début de la liste a un indice qui est supérieur à l'indice indiquant la fin de la file.

Create a new array twice bigger as the initial one and copy the values in the new array.
If the index of the head is bigger than the index of the tail, we loop from the head to the size of the array and from 0 to the tail.

##### Implanter la solution retenue dans une nouvelle classe ResizeableFifo.

````java
package fr.uge.fifo;

import java.util.*;

public class ResizeableFifo<E> {
    private int size;
    private E[] resizeableFifo;
    private int tail;
    private int head;

    @SuppressWarnings("unchecked")
    public ResizeableFifo(int length){
        if(length <= 0) throw new IllegalArgumentException("Negative length");
        resizeableFifo = (E[] ) new Object[length];
    }
    
    public void offer(E value) {
        Objects.requireNonNull(value);
        if(size == resizeableFifo.length) resize();
        resizeableFifo[tail] = value;
        tail = (tail == resizeableFifo.length-1 ? 0 : tail + 1);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        var resizedFifo = (E[] ) new Object[resizeableFifo.length*2];
        System.arraycopy(resizeableFifo,head,resizedFifo,0, resizeableFifo.length - head);
        System.arraycopy(resizeableFifo,0,resizedFifo, resizeableFifo.length - head, tail);
        head = 0;
        tail = size;
        resizeableFifo = resizedFifo;
    }
    
    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = resizeableFifo[head];
        resizeableFifo[head] = null;
        size--;
        head = (head == resizeableFifo.length-1 ? 0 : head + 1);
        return res;
    }
    
    @Override
    public String toString(){
        var joiner = new StringJoiner(", ","[","]");
        var tmp_head = head;
        for(var i = 0; i < size; i++){
            joiner.add(resizeableFifo[tmp_head].toString());
            tmp_head++;
            if(tmp_head >= resizeableFifo.length) tmp_head = 0;
        }
        return joiner.toString();
    }
    
    public int size() {
        return size;
    }
    
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int currentIndex = head;
            private int numberIteration;

            @Override
            public boolean hasNext() {
                return numberIteration < size;
            }

            @Override
            public E next() {
                if(currentIndex >= size)  throw new NoSuchElementException("No element found");
                var res = resizeableFifo[currentIndex];
                numberIteration++;
                currentIndex = (currentIndex == resizeableFifo.length-1 ? 0 : currentIndex + 1);
                return res;
            }
        };
    }
}
````

##### 2) En fait, il existe déjà une interface pour les files dans le JDK appelée java.util.Queue.
##### Sachant qu'il existe une classe AbstractQueue qui fournit déjà des implantations par défaut de l'interface Queue indiquer :
##### Quelles sont les méthodes supplémentaires à implanter :

Additional method to implement: peek.

##### Quelles sont les méthodes dont l'implantation doit être modifiée :

Modify the offer method so that it returns a boolean.

##### Quelles sont les méthodes que l'on peut supprimer.

Delete the method "isEmpty".

##### Faire en sorte que la classe ResizableFifo implante l'interface Queue.

```java
public class ResizeableFifo<E> extends AbstractQueue<E> implements Queue<E> {
    private int size;
    private E[] resizeableFifo;
    private int tail;
    private int head;

    @SuppressWarnings("unchecked")
    public ResizeableFifo(int length){
        if(length <= 0) throw new IllegalArgumentException("Negative length");
        resizeableFifo = (E[] ) new Object[length];
    }

    @Override
    public boolean offer(E value) {
        Objects.requireNonNull(value);
        if(size == resizeableFifo.length) resize();
        resizeableFifo[tail] = value;
        tail = (tail == resizeableFifo.length-1 ? 0 : tail + 1);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        var resizedFifo = (E[] ) new Object[resizeableFifo.length*2];
        System.arraycopy(resizeableFifo,head,resizedFifo,0, resizeableFifo.length - head);
        System.arraycopy(resizeableFifo,0,resizedFifo, resizeableFifo.length - head, tail);
        head = 0;
        tail = size;
        resizeableFifo = resizedFifo;
    }

    @Override
    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = resizeableFifo[head];
        resizeableFifo[head] = null;
        size--;
        head = (head == resizeableFifo.length-1 ? 0 : head + 1);
        return res;
    }

    @Override
    public E peek() {
        return resizeableFifo[head];
    }

    @Override
    public String toString(){
        var joiner = new StringJoiner(", ","[","]");
        var tmp_head = head;
        for(var i = 0; i < size; i++){
            joiner.add(resizeableFifo[tmp_head].toString());
            tmp_head++;
            if(tmp_head >= resizeableFifo.length) tmp_head = 0;
        }
        return joiner.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int currentIndex = head;
            private int numberIteration;

            @Override
            public boolean hasNext() {
                return numberIteration < size;
            }

            @Override
            public E next() {
                if(currentIndex >= size)  throw new NoSuchElementException("No element found");
                var res = resizeableFifo[currentIndex];
                numberIteration++;
                currentIndex = (currentIndex == resizeableFifo.length-1 ? 0 : currentIndex + 1);
                return res;
            }
        };
    }
}
```





