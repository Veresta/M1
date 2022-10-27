# Compte rendu TP5 Java Avancé

------

## Exercice 2: Vectorized Add/ Vectorized Min

##### 1) On cherche à écrire une fonction sum qui calcule la somme des entiers d'un tableau passé en paramètre. Pour cela, nous allons utiliser l'API de vectorisation pour calculer la somme sur des vecteurs.

- Quelle est la classe qui représente des vecteurs d'entiers ?
The class representing an int vector is : __IntVector__ 

- Qu'est ce qu'un VectorSpecies et quelle est la valeur de VectorSpecies que nous allons utiliser dans notre cas ?
VectorSpecies<*> represent a type (byte|short|long|int|float|double) and a specific shape (64|128|256|512).
The shape represent the size of operation did by the CPU.

- Comment créer un vecteur contenant des zéros et ayant un nombre préféré de lanes ?
First, define the size with a VectorSpecies<Integer> species.
Then, use the method IntVector.zero(species).


- Comment calculer la taille de la boucle sur les vecteurs (loopBound) ?
To calculate, loopBound use the following thing : size of the array - size of the array % SPECIES.length()

- Comment faire la somme de deux vecteurs d'entiers ?

- Comment faire la somme de toutes les lanes d'un vecteur d'entiers ?
- Si la longueur du tableau n'est pas un multiple du nombre de lanes, on va utiliser une post-loop, quel doit être le code de la post-loop ?

```java
public static int sum(int[] array) {
        Objects.requireNonNull(array);
        var sum = IntVector.zero(SPECIES);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        var i = 0;
        for(; i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES, array, i);
            sum = sum.add(view);
        }
        var res = 0;
        for(;i < size; i++){
            res+= array[i];
        }
        return sum.reduceLanes(VectorOperators.ADD) + res;
    }
```




##### 2) On souhaite écrire une méthode sumMask qui évite d'utiliser une post-loop et utilise un mask à la place.

- Comment peut-on faire une addition de deux vecteurs avec un mask ?
- Comment faire pour créer un mask qui allume les bits entre i la variable de boucle et length la longueur du tableau ?

```java
public static int sumMask(int[] array) {
        Objects.requireNonNull(array);
        var sum = IntVector.zero(SPECIES);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        for(var i = 0; i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES, array, i);
            sum = sum.add(view);
        }
        var mask = SPECIES.indexInRange(loopBound, size);
        var view = IntVector.fromArray(SPECIES, array, loopBound, mask);
        sum = sum.add(view);
        return sum.reduceLanes(VectorOperators.ADD);
    }
```

##### 3) On souhaite maintenant écrire une méthode min qui calcule le minimum des valeurs d'un tableau en utilisant des vecteurs et une post-loop. Contrairement à la somme qui a 0 comme élément nul, le minimum n'a pas d'élément nul... Quelle doit être la valeur utilisée pour initialiser toutes les lanes du vecteur avant la boucle principale ? Écrire le code de la méthode min, vérifier que le test nommé "testMin" passe et vérifier avec les tests JMH que votre code est plus efficace qu'une simple boucle sur les valeurs du tableau.


