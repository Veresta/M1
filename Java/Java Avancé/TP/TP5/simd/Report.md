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

Let vectors of integers v1 and v2, we obtain their addiction thanks to v3 = v1.add(v2);

- Comment faire la somme de toutes les lanes d'un vecteur d'entiers ?

Given an integer vector v1, we use v1.reduceLanes(VectorOperators.ADD) to sum it.

- Si la longueur du tableau n'est pas un multiple du nombre de lanes, on va utiliser une post-loop, quel doit être le code de la post-loop ?
Take the value of the iterator of the loop that we make iterate until the size of the array that we handle.

```java
    var res = 0;
    for(;i < size; i++){
        res+= array[i];
    }
```


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

First init the mask then create an IntVector with the mask.
Add it to the vector to add.

- Comment faire pour créer un mask qui allume les bits entre i la variable de boucle et length la longueur du tableau ?

use SPECIES.indexInRange(i, length);

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

Initialize the values to the constant Integer.MAX_VALUE.

```java
 public static int min(int[] array) {
        Objects.requireNonNull(array);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        var min = IntVector.broadcast(SPECIES, Integer.MAX_VALUE);
        var i = 0;
        for(;i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES,array,i);
            min = min.min(view);
        }
        var res = min.reduceLanes(VectorOperators.MIN);
        for(;i < size; i++){
            if(array[i] < res){
                res = array[i];
            }
        }
        return res;
    }
```

##### 4) On souhaite enfin écrire une méthode minMask qui au lieu d'utiliser une post-loop comme dans le code précédent, utilise un mask à la place. Attention, le minimum n'a pas d’élément nul (non, toujours pas !), donc on ne peut pas laisser des zéros "traîner" dans les lanes lorsque l'on fait un minimum sur deux vecteurs.Écrire le code de la méthode minMask et vérifier que le test nommé "testMinMask" passe. 

(One case of test didn't pass)

## Exercice 3: FizzBuzz

##### 1)