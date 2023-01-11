# Compte rendu TP11 Java Avancé

------

## Exercice 2 : NumericVec

##### 1) Dans la classe fr.uge.numeric.NumericVec, on souhaite écrire une méthode longs sans paramètre qui permet de créer un NumericVec vide ayant pour l'instant une capacité fixe de 16 valeurs. Cela doit être la seule façon de pouvoir créer un NumericVec.
##### Écrire la méthode longs puis ajouter les méthodes add(value), get(index) et size. 

```java
package fr.uge.numeric;

import java.util.Objects;

public class NumericVec<T> {
    private final long[] internal;
    private int size;

    private NumericVec(){
        internal = new long[16];
    }

    public static <E> NumericVec<E> longs(){
        return new NumericVec<>();
    }

    public void add(Long value){
        Objects.requireNonNull(value);
        internal[size] = value;
        size++;
    }

    public long get(int index){
        Objects.checkIndex(index, size);
        return internal[index];
    }

    public int size(){
        return size;
    }
}
```

##### 2) On veut maintenant que le tableau utilisé par NumericVec puisse s'agrandir pour permettre d'ajouter un nombre arbitraire de valeurs.
##### On veut, de plus, que NumericVec soit économe en mémoire, donc la capacité du tableau d'un NumericVec vide doit être 0 (si vous n'y arrivez pas, faites la suite).

```java
public class NumericVec<T> {
    private long[] internal;
    private int size;

    private NumericVec(){
        internal = new long[0];

    }

    public static <E> NumericVec<E> longs(){
        return new NumericVec<>();
    }

    public void add(Long value){
        Objects.requireNonNull(value);
        if(size + 1 > internal.length){
            resizeInternal();
        }
        internal[size] = value;
        size++;
    }

    public long get(int index){
        Objects.checkIndex(index, size);
        return internal[index];
    }

    public int size(){
        return size;
    }

    private void resizeInternal(){
        internal = Arrays.copyOf(internal, 2*size+1);
    }
}
```

##### 3) Faire en sorte d'utiliser un Stream pour que l'on puisse afficher un NumericVec avec le format attendu. 

```java
    public String toString(){
        return Arrays.stream(internal).mapToObj(Long::toString).limit(size).collect(Collectors.joining(", ","[","]"));
    }
```

##### 4) On veut maintenant ajouter les 3 méthodes ints, longs et doubles qui permettent respectivement de créer des NumericVec d'int, de long ou de double en prenant en paramètre des valeurs séparées par des virgules.
##### En termes d'implantation, l'idée est de convertir les int ou les double en long avant de les insérer dans le tableau. Et dans l'autre sens, lorsque l'on veut lire une valeur,
##### C'est-à-dire quand on prend un long dans le tableau, on le convertit en le type numérique attendu. Pour cela, l'idée est de stocker dans chaque NumericVec une fonction into qui sait convertir une valeur en long, et une fonction from qui sait convertir un long vers la valeur attendue. 

````java
public class NumericVec<T> {
    private long[] internal;

    private int length;
    private int size;
    private static final int RESIZEVALUE = 10;
    private int nbOfResize = 1;

    private final Function<T, Long> into;

    private final Function<Long, T> from;

    private NumericVec(Function<T, Long> into, Function<Long, T> from){
        Objects.requireNonNull(into);
        Objects.requireNonNull(from);
        internal = new long[0];
        this.into = into;
        this.from = from;
    }

    public static NumericVec<Integer> ints(int... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(Integer::longValue, Long::intValue);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public static NumericVec<Long> longs(long... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(x -> x, x -> x);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public static NumericVec<Double> doubles(double... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(Double::doubleToRawLongBits, Double::longBitsToDouble);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public void add(T value){
        Objects.requireNonNull(value);
        if(size + 1 > length){
            resizeInternal();
        }
        internal[size] = into.apply(value);
        size++;
    }

    public T get(int index){
        Objects.checkIndex(index, size);
        return from.apply(internal[index]);
    }

    public int size(){
        return size;
    }

    private void resizeInternal(){
        length = size+nbOfResize*RESIZEVALUE;
        var tmp = Arrays.copyOf(internal, length);
        nbOfResize++;
        internal = tmp;
    }

    public String toString(){
        return Arrays.stream(internal)
                .mapToObj(x -> from.apply(x).toString())
                .limit(size)
                .collect(Collectors.joining(", ","[","]"));
    }
}
````

##### 5) On souhaite maintenant pouvoir parcourir un NumericVec avec une boucle for(:). Dans le cas où l'on modifie un NumericVec avec la méthode add lors de l'itération, les valeurs ajoutées ne sont pas visibles pour la boucle.
##### Modifier la classe NumericVec pour implanter le support de la boucle for(:).

```java
    @Override
    public Iterator<T> iterator() {
        var tmp = Arrays.copyOf(internal, internal.length);
        var tmp_size = size;
        return new Iterator<>() {
            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < tmp_size;
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException("No next");
                var res = from.apply(tmp[currentIndex]);
                currentIndex++;
                return res;
            }
        };
    }
```

##### 6) On souhaite ajouter une méthode addAll qui permet d'ajouter un NumericVec à un NumericVec déjà existant.
##### Écrire le code de la méthode addAll.

```java
    public void addAll(NumericVec<T> seq2) {
        Objects.requireNonNull(seq2);
        IntStream.range(0, seq2.size).forEach(index -> add(seq2.get(index)));
    }
```

##### 7) On souhaite maintenant écrire une méthode map(function, factory) qui prend en paramètre une fonction qui peut prendre en paramètre un élément du NumericVec et renvoie une nouvelle valeur ainsi qu' une référence de méthode permettant de créer un nouveau NumericVec qui contiendra les valeurs renvoyées par la fonction.
##### Écrire la méthode map.

```java
    public <E> NumericVec<E> map(Function<? super T, ? extends E> fun, Supplier<NumericVec<E>> vec) {
        Objects.requireNonNull(fun);
        Objects.requireNonNull(vec);
        var tmp = vec.get();
        for(var e : this){
            tmp.add(fun.apply(e));
        }
        return tmp;
    }
```

##### 8) On souhaite écrire une méthode toNumericVec(factory) qui prend en paramètre une référence de méthode permettant de créer un NumericVec et renvoie un Collector qui peut être utilisé pour collecter des valeurs numériques dans un/des NumericVec créés par la référence de méthode.
##### Écrire la méthode toNumericVec. 

