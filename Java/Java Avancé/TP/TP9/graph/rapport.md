# Compte rendu TP9 Java Avancé

------

## Exercice 2 - MatrixGraph

##### 1) Indiquer comment trouver la case (i, j) dans un tableau à une seule dimension de taille nodeCount * nodeCount.
Pour parcourir les lignes, on multiplie par nodecount et pour avancer en colonne on fait +1.

##### 2) Rappeler pourquoi, en Java, il n'est pas possible de créer des tableaux de variables de type puis implanter la classe MatrixGraph et son constructeur.
Car à l'exécution on ne peut pas savoir qu'elle type sera contenue dedans donc on crée un tableau d'object que l'on caste.

##### Pouvez-vous supprimer le warning à la construction ? Pourquoi? 
On peut utiliser l'annotation @SuppressWarnings("unchecked").

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
Put final with on MatrixGraph class

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

Cette classe permet de fournir une solution facultative à un résultat plutôt que de renvoyer null.

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

Un itérateur est une interface permettant de parcourir de manière safe une collection.
Elle dispose entre autres des méthodes next() lui permettant d'accéder à l'élément suivant de manière sécurisé et hasNext() vérifiant
qu'un élément existe après l'élément courant.

##### Que renvoie next si hasNext retourne false ?

hasNext() renvoie une exception NoSuchElementException.

##### Expliquer pourquoi il n'est pas nécessaire, dans un premier temps, d'implanter la méthode remove qui fait pourtant partie de l'interface.

La méthode remove est une méthode par default dans l'interface, il n'est donc pas nécessaire de l'implémenter immédiatement.

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

???

##### Est-ce qu'il y a d'autres champs qui ne doivent pas être déclarés private avant Java 11 ?

???

##### 8)On souhaite écrire la méthode neighborStream(src) qui renvoie un IntStream contenant tous les nœuds ayant un arc sortant par src.

##### Pour créer le Stream ,nous allons utiliser StreamSupport.intStream qui prend en paramètre un Spliterator.OfInt. Rappeler ce qu'est un Spliterator, à quoi sert le OfInt et quelles sont les méthodes qu'il va falloir redéfinir.

##### Écrire la méthode neighborStream sachant que l'on implantera le Spliterator en utilisant l'itérateur défini précédemment. 