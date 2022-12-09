# Compte rendu TP10 Java Avancé

------

## Exercice 2 - Seq

##### 1) Écrire le code de la classe Seq dans le package fr.uge.seq.

````java
package fr.uge.seq;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Seq<T> {

    private final List<T> internal;

    public Seq(List<? extends T> internal) {
        Objects.requireNonNull(internal);
        this.internal = List.copyOf(internal);
    }

    public static <E> Seq<E> from(List<? extends E> array){
        Objects.requireNonNull(array);
        return new Seq<>(array);
    }

    public T get(int index){
        Objects.checkIndex(index, size());
        return internal.get(index);
    }

    public int size(){
        return internal.size();
    }
}
````

##### 2) Écrire une méthode d'affichage permettant d'afficher les valeurs d'un Seq séparées par des virgules (suivies d'un espace),l'ensemble des valeurs étant encadré par des chevrons ('<' et '>'). 

````java
    @Override
    public String toString() {
        return internal.stream().map(Object::toString).collect(Collectors.joining(", ","<",">"));
    }
````


##### 3) Écrire une méthode of permettant d'initialiser un Seq à partir de valeurs séparées par des virgules.

```java
    @SafeVarargs
    public static <E> Seq<E> of(E... elem){
        Objects.requireNonNull(elem);
        return from(List.of(elem));
    }
```

##### 4) Écrire une méthode forEach qui prend en paramètre une fonction qui prend en paramètre chaque élément un par un et fait un effet de bord.

```java
    public void forEach(Consumer<? super T> func) {
        Objects.requireNonNull(func);
        internal.forEach(func);
    }
```

##### 5) Avant de se lancer dans l'implantation de map, quelle doit être sa signature ?

The signature of the map method should be of the following form: __public <F> Seq<F> map(Function<? super T, ? extends F> mapper)__

The type F means the new type assumed to be contained in my list.
The Function in parameter must take a __? super T__ which specifies the current type, and in output we want the new type wanted.

##### Quel doit être le type des éléments de la liste ? Et le type de la fonction stockée ?

The type of the elements of the list must be __?__, internally the type is not important, it is only externally
that we display the type.

The stored function must be of the form: __Function<Object, ? extends T>__

##### Faire les modifications correspondantes, puis changer le code des méthodes pour les prendre en compte. Enfin, écrire le code de map.
```java
public class Seq<T> {

    private final List<?> internal;
    private final Function<Object, ? extends T> mapper;

    @SuppressWarnings("unchecked")
    public Seq(List<?> internal) {
        this(internal, x -> (T)x);
    }

    private Seq(List<?> internal, Function<Object, ? extends T> mapper){
        Objects.requireNonNull(internal);
        Objects.requireNonNull(mapper);
        this.internal = List.copyOf(internal);
        this.mapper = mapper;
    }

    public static <E> Seq<E> from(List<? extends E> array){
        Objects.requireNonNull(array);
        return new Seq<>(array);
    }

    public T get(int index){
        Objects.checkIndex(index, internal.size());
        return mapper.apply(internal.get(index));
    }

    public int size(){
        return internal.size();
    }

    @SafeVarargs
    public static <E> Seq<E> of(E... elem){
        Objects.requireNonNull(elem);
        return from(List.of(elem));
    }

    @Override
    public String toString() {
        return internal.stream().map(mapper).map(Object::toString).collect(Collectors.joining(", ","<",">"));
    }

    public void forEach(Consumer<? super T> func) {
        Objects.requireNonNull(func);
        internal.stream().map(mapper).forEach(func);
        //internal.forEach(func);
    }

    public <F> Seq<F> map(Function<? super T, ? extends F> mapper) {
        Objects.requireNonNull(mapper);
        return new Seq<>(this.internal, this.mapper.andThen(mapper));
    }
}
```

##### 6) Écrire une méthode findFirst qui renvoie le premier élément du Seq si celui-ci existe.

```java
    public Optional<?> findFirst() {
        return Optional.of(internal.stream().map(mapper).findFirst()).get();
    }
```
##### 7) Faire en sorte que l'on puisse utiliser la boucle for-each-in sur un Seq

Need to implement the iterable interface to our class.

```java
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < internal.size();
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException("no next value");
                var res = get(currentIndex);
                currentIndex++;
                return res;
            }
        };
    }
```

##### 8) Enfin, on souhaite implanter la méthode stream() qui renvoie un Stream des éléments du Seq. Pour cela, on va commencer par implanter un Spliterator que l'on peut construire à partir du Spliterator déjà existant de la liste (que l'on obtient avec la méthode List.spliterator()).
##### Puis en utilisant la méthode StreamSupport.stream, créer un Stream à partir de ce Spliterator.
##### Écrire la méthode stream().

````java
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Spliterator<T> spliterator() {
        return new Spliterator<>() {
            private final Spliterator<?> currentSplit = internal.spliterator();

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                return currentSplit.tryAdvance(e -> action.accept(mapper.apply(e)));
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return currentSplit.estimateSize();
            }

            @Override
            public int characteristics() {
                return NONNULL | ORDERED | IMMUTABLE;
            }
        };
    }
````

##### 9) Si vous ne l'avez pas déjà fait, on souhaite que le Stream renvoyé par la méthode stream permette d’effectuer les calculs en parallèle sur les éléments du Seq.
##### Modifier votre implantation (commentez l'ancienne) et vérifier que les tests marqués "Q9" passent

```java
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Spliterator<T> spliterator(){
        return getSpliterator(internal.spliterator());
    }
    private Spliterator<T> getSpliterator(Spliterator<?> selfIterator) {
        if(selfIterator == null) return null;
        return new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                return selfIterator.tryAdvance(e -> action.accept(mapper.apply(e)));
            }

            @Override
            public Spliterator<T> trySplit() {
                return getSpliterator(selfIterator.trySplit());
            }

            @Override
            public long estimateSize() {
                return selfIterator.estimateSize();
            }
            @Override
            public int characteristics() {
                return selfIterator.characteristics() | NONNULL | ORDERED | IMMUTABLE;
            }
        };
    }
```

## Exercice 3 - Seq2

````java
public final class Seq2<T> implements Iterable<T> {

    private final Object[] internal;
    private final Function<Object, ? extends T> mapper;
    private final int size;


    @SuppressWarnings("unchecked")
    public Seq2(List<? extends T> liste) {
        this(liste, x -> (T) x);
        Objects.requireNonNull(liste);
    }

    private Seq2(List<?> liste, Function<Object, ? extends T> mapper){
        Objects.requireNonNull(liste);
        Objects.requireNonNull(mapper);
        this.size = liste.size();
        this.internal = new Object[size];
        this.mapper = mapper;
        IntStream.range(0, size).forEach(index ->{
            var res = liste.get(index);
            if(res == null) throw new NullPointerException("null present inside list");
            internal[index] = res;
        });
    }

    public int size(){
        return size;
    }

    public T get(int index){
        Objects.checkIndex(index, size);
        return mapper.apply(internal[index]);
    }

    public static <E> Seq2<E> from(List<? extends E> liste){
        Objects.requireNonNull(liste);
        return new Seq2<>(liste);
    }

    @Override
    public String toString() {
        return Arrays.stream(internal).map(mapper).map(Objects::toString).collect(Collectors.joining(", ","<",">"));
    }

    @SafeVarargs
    public static <T> Seq2<T> of(T... elem){
        Objects.requireNonNull(elem);
        return Seq2.from(List.of(elem));
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException("No next value");
                var res = get(currentIndex);
                currentIndex++;
                return res;
            }
        };
    }

    public void forEach(Consumer<? super T> action){
        Objects.requireNonNull(action);
        Arrays.stream(internal).map(mapper).forEach(action);
    }

    public <F> Seq2<F> map(Function<? super T, ? extends F> newMapper) {
        Objects.requireNonNull(newMapper);
        return new Seq2<>(Arrays.stream(this.internal).toList(), this.mapper.andThen(newMapper));
    }

    public Optional<?> findFirst() {
        return Optional.of(Arrays.stream(internal).map(mapper).findFirst()).get();
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Spliterator<T> spliterator(){
        return getSpliterator(Arrays.asList(internal).spliterator());
    }
    private Spliterator<T> getSpliterator(Spliterator<?> selfIterator) {
        if(selfIterator == null) return null;
        return new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                return selfIterator.tryAdvance(e -> action.accept(mapper.apply(e)));
            }
            @Override
            public Spliterator<T> trySplit() {
                return getSpliterator(selfIterator.trySplit());
            }

            @Override
            public long estimateSize() {
                return selfIterator.estimateSize();
            }
            @Override
            public int characteristics() {
                return selfIterator.characteristics() | NONNULL | ORDERED | IMMUTABLE;
            }
        };
    }
    /*@Override
    public Spliterator<T> spliterator(){
        var tmp = Arrays.stream(internal).spliterator();
        return new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                return tmp.tryAdvance(e -> action.accept(mapper.apply(e)));
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return tmp.estimateSize();
            }

            @Override
            public int characteristics() {
                return tmp.characteristics() | NONNULL | ORDERED | IMMUTABLE;
            }
        };
    }*/
}
````