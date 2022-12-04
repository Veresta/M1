# Compte rendu TP9 Java Avancé

------

## Exercice 2 - MatrixGraph

##### 1) Indiquer comment trouver la case (i, j) dans un tableau à une seule dimension de taille nodeCount * nodeCount.
To go through the rows, we multiply by nodecount and to go through the columns, we do +1.
i * nodecount + j

##### 2) Rappeler pourquoi, en Java, il n'est pas possible de créer des tableaux de variables de type puis implanter la classe MatrixGraph et son constructeur.
Because at runtime we can't know what type will be contained in it so we create an array of objects that we cast.

##### Pouvez-vous supprimer le warning à la construction ? Pourquoi? 
We can use the @SuppressWarnings("unchecked") annotation.

````java
public class MatrixGraph<T> implements Graph<T> {
    private final T[] array;

    @SuppressWarnings("unchecked")
    private MatrixGraph(int size) {
        if(size<0) throw new IllegalArgumentException("Negative size");
        this.array = (T[]) new Object[size*size];
    }
}
````

##### 3) On peut remarquer que la classe MatrixGraph n'apporte pas de nouvelles méthodes par rapport aux méthodes de l'interface Graph donc il n'est pas nécessaire que la classe MatrixGraph soit publique.
##### Ajouter une méthode factory nommée createMatrixGraph dans l'interface Graph et déclarer la classe MatrixGraph non publique. 
Put final on MatrixGraph class

```java
/**
 * Create a graph implementation based on a matrix.
 * @param <T> type of the edge weight
 * @param nodeCount the number of nodes.
 * @return a new implementation of Graph.
 */
static Graph createMatrixGraph(int nodeCount) {
        return new MatrixGraph<>(nodeCount);
}
```

##### 4) Afin d'implanter correctement la méthode getWeight, rappeler à quoi sert la classe java.util.Optional en Java.

This class allows to provide an optional solution to a result instead of returning null.

##### Implanter la méthode addEdge sachant que l'on ne peut pas créer un arc sans valeur.

````java
    @Override
    public void addEdge(int src, int dst, T weight) {
        Objects.requireNonNull(weight);
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        this.array[src * this.nodeCount + dst] = weight;
    }
````

##### Implanter la méthode getWeight.

````java
    @Override
    public Optional<T> getWeight(int src, int dst) {
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        return Optional.ofNullable(array[src * this.nodeCount + dst]);
    }
````

##### 5) Implanter la méthode edges puis vérifier que les tests marqués "Q5" passent.

````java
@Override
    public void edges(int src, EdgeConsumer<? super T> edgeConsumer) {
        Objects.requireNonNull(edgeConsumer);
        Objects.checkIndex(src, nodeCount);
        IntStream.range(0, nodeCount).forEach(index ->{
            var weight = getWeight(src, index);
            weight.ifPresent(t -> edgeConsumer.edge(src, index, t));
        });
    }
````

##### 6) Rappeler le fonctionnement d'un itérateur et de ses méthodes hasNext et next.

An iterator is an interface allowing to browse a collection in a safe way.
It has, among others, the methods next() allowing it to access the next element in a safe way and hasNext() checking that an element exists after the current element.
that an element exists after the current element.

##### Que renvoie next si hasNext retourne false ?

hasNext() returns a NoSuchElementException.

##### Expliquer pourquoi il n'est pas nécessaire, dans un premier temps, d'implanter la méthode remove qui fait pourtant partie de l'interface.

The remove method is a default method in the interface, so it is not necessary to implement it immediately.

##### Implanter la méthode neighborsIterator(src) qui renvoie un itérateur sur tous les nœuds ayant un arc dont la source est src. 

````java
@Override
    public Iterator<Integer> neighborIterator(int src) {
        Objects.checkIndex(src, nodeCount);
        return new Iterator<>() {
            private int currentIndex;


            private boolean existAnotherEdge(){
                return IntStream.range(currentIndex, nodeCount).anyMatch(index -> getWeight(src, index).isPresent());
            }
            @Override
            public boolean hasNext() {
                return currentIndex <= nodeCount - 1 && existAnotherEdge();
            }
            @Override
            public Integer next() {
                if(!hasNext()) throw new NoSuchElementException("No element found");
                var res = getWeight(src, currentIndex);
                var tmp =  currentIndex;
                currentIndex++;
                while(res.isEmpty()){
                    res = getWeight(src, currentIndex);
                    tmp = currentIndex;
                    if(!hasNext()) throw new NoSuchElementException("No element found");
                    currentIndex++;
                }
                return tmp;
            }
        };
    }
````

##### 7) Pourquoi le champ nodeCount ne doit pas être déclaré private avant Java 11 ?

Because otherwise, as it is used in an inner class, the VM generates an accessor.

##### Est-ce qu'il y a d'autres champs qui ne doivent pas être déclarés private avant Java 11 ?


##### 8) On souhaite écrire la méthode neighborStream(src) qui renvoie un IntStream contenant tous les nœuds ayant un arc sortant par src.
##### Pour créer le Stream ,nous allons utiliser StreamSupport.intStream qui prend en paramètre un Spliterator.OfInt. 
##### Rappeler ce qu'est un Spliterator, à quoi sert le OfInt et quelles sont les méthodes qu'il va falloir redéfinir.

A spliterator is what iterator is to collection, but on streams.
The spliterator ofInt is the implementation of Spliterator on an Intstreams.
We will have to redefine the :
- tryAdvance
- trySplit
- characteristics
- estimateSize

##### Écrire la méthode neighborStream sachant que l'on implantera le Spliterator en utilisant l'itérateur défini précédemment.

```java
    public IntStream neighborStream(int src){
        Objects.checkIndex(src, nodeCount);
        return StreamSupport.intStream(new Spliterator.OfInt(){

            private final Iterator<Integer> it = neighborIterator(src);
            @Override
            public OfInt trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return 0;
            }

            @Override
            public boolean tryAdvance(IntConsumer action) {
                var res = 0;
                if(it.hasNext()){
                    res = it.next();
                    action.accept(res);
                    return true;
                }
                return false;
            }
        },false);
    }
```

##### 9) Rappeler comment on fait pour avoir une méthode 'instance avec du code dans une interface.

Define the method as default method.

##### Déplacer neighborStream dans Graph et vérifier que les tests unitaires passent toujours.

##### 10) Expliquer le fonctionnement précis de la méthode remove de l'interface Iterator.

Removes from the underlying collection the last element returned by this iterator (optional operation).
This method can only be called once per call to next.
The behavior of an iterator is not specified if the underlying collection is modified while the iteration is in progress other than by calling this method,
unless an overriding class has specified a concurrent modification policy.
The behavior of an iterator is not specified if this method is called after a call to the forEachRemaining method.

##### Implanter la méthode remove de l'itérateur.

```java
@Override
    public void remove(){
        if(!canDelete) throw new IllegalStateException("Can't delete");
            array[src * nodeCount + currentIndex-1] = null;
            canDelete = false;
    }
```

##### 11)On peut remarquer que l'on peut ré-écrire edges en utilisant neighborsStream, en une ligne :) et donc déplacer edges dans Graph.
##### Déplacer le code de la méthode edges dans Graph.
````java
    default void edges(int src, EdgeConsumer<? super T> edgeConsumer){
        Objects.requireNonNull(edgeConsumer);
        neighborStream(src).forEach(index -> edgeConsumer.edge(src, index, getWeight(src, index).get()));
    }
````

## Exercice 3 - MatrixGraph

I tried to do the implementation in two ways, first with a HashMap containing a HashMap in value,
then with a HashMap Array, in both cases, there were some test questions that didn't work
because of the neighborIterator method.
The problem is that the test wants to create an iterator on a "null" node, except that it wants to see it as if the list for that node
was not null but empty. I couldn't solve the problem.