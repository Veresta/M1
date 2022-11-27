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

