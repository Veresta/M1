# Compte rendu TP3 Java Avancé

### Mathis MENAA
------

## Exercice 2: The Slice and Furious

##### 1) Implanter la classe Slice et les méthodes array, size et get(index). 

```java
public sealed interface Slice<E> permits Slice.ArraySlice {

    static <E> Slice<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new ArraySlice<E>(tab);
    }

    int size();

    Object get(int index);

    final class ArraySlice<E> implements Slice<E> {

        private final E[] array;

        private ArraySlice(E[] tab) {
            Objects.requireNonNull(tab);
            array = tab;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public E get(int index) {
            if (index > len || index < 0) throw new IndexOutOfBoundsException("Incorrect index");
            return array[index];
        }
    }
}
```

##### 2) On souhaite que l'affichage d'un slice affiche les valeurs séparées par des virgules avec un '[' et un ']' comme préfixe et suffixe. 

```java
@Override
        public String toString(){
            return Arrays.stream(array).map(x -> (String::valueOf)
                    .collect(Collectors.joining(", ","[","]"));
        }
```

##### 3) On souhaite enfin ajouter une méthode subSlice(from, to) à l'interface Slice qui renvoie un sous-slice restreint aux valeurs entre from inclus et to exclu.

```java
 final class SubArraySlice<E> implements Slice<E> {

        private final E[] array;
        private final int from;
        private final int to;

        private SubArraySlice(E[] tab, int from, int to){
            Objects.requireNonNull(tab);
            if(from > to || to > tab.length || from < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            array = tab;
            this.from = from;
            this.to = to;
        }

        @Override
        public int size() {
            return to - from;
        }
        @Override
        public E get(int index) {
            if (index > this.size() || index < 0) throw new IndexOutOfBoundsException("Incorrect index");
            return array[index+from];
        }
        @Override
        public String toString() {
            return Arrays.stream(array, from, to).map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }
```

##### 4) On souhaite enfin ajouter une méthode subSlice(from, to) à l'interface Slice qui renvoie un sous-slice restreint aux valeurs entre from inclus et to exclu.

```java
@Override
    public Slice<E> subSlice(int i, int i2){
        if(i > i2 || i2 < 0  || (i2 - i) > this.size() || i < 0) throw new IndexOutOfBoundsExcepti("Incorrect parameters");
            return new SubArraySlice<>(array,this.from + i, this.from + i2);
        }
```

## Exercice 3: The Slice 2 Furious

##### 1) Recopier l'interface Slice de l'exercice précédent dans une interface Slice2. Vous pouvez faire un copier-coller de Slice dans même package, votre IDE devrait vous proposer de renommer la copie. Puis supprimer la classe interne SubArraySlice ainsi que la méthode array(array, from, to) car nous allons les réimplanter et commenter la méthode subSlice(from, to) de l'interface, car nous allons la ré-implanter aussi, mais plus tard. 

```java
public sealed interface Slice2<E> permits Slice2.ArraySlice {
    static <E> Slice2<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new Slice2.ArraySlice<E>(tab);
    }
    int size();

    Object get(int index);

    @Override
    String toString();

    //Slice<E> subSlice(int i, int i1);

    final class ArraySlice<E> implements Slice2<E> {

        private final E[] array;

        private ArraySlice(E[] tab) {
            Objects.requireNonNull(tab);
            array = tab;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public E get(int index) {
            if (index > array.length || index < 0) throw new IndexOutOfBoundsException("Incorrect index");
            return array[index];
        }
        @Override
        public String toString(){
            return Arrays.stream(array).map(String::valueOf)
                    .collect(Collectors.joining(", ","[","]"));
        }

        /*@Override
        public Slice<E> subSlice(int i, int i1){
            if(i > i1 || i1 > array.length || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new SubArraySlice(array, i, i1);
        }*/
    }
}
```

##### 2) Déclarer une classe SubArraySlice à l'intérieur de la classe ArraySlice comme une inner class donc pas comme une classe statique et implanter cette classe et la méthode array(array, from, to). 


```java
final class subArraySlice implements Slice2<E> {
            private final int from;
            private final int to;

            private subArraySlice(int from, int to) {
                if(from > to || to > ArraySlice.this.size() || from < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
                this.from = from;
                this.to = to;
            }

            @Override
            public int size() {
                return to - from;
            }

            @Override
            public E get(int index) {
                if ((index >= this.size()) || (index < 0) || index >= to) throw new IndexOutOfBoundsException("Incorrect index");
                return ArraySlice.this.get(index+from);
            }

            @Override
            public String toString() {
                return Arrays.stream(array, from, to).map(String::valueOf)
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        }
```

##### 3) Dé-commenter la méthode subSlice(from, to) de l'interface et fournissez une implantation de cette méthode dans les classes ArraySlice et SubArraySlice.



```java
static <E> Slice2<E> array(E[] tab, int from, int to){
      Objects.requireNonNull(tab);
      return new ArraySlice<>(tab).subSlice(from, to);
    }

@Override
        public Slice2<E> subSlice(int i, int i1){
            if(i > i1 || i1 > this.size() || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new subArraySlice(i, i1);
        }

@Override
            public Slice2<E> subSlice(int i, int i1) {
                if(i > i1 || i1 < 0  || (i1 - i) > this.size() || i < 0) throw new IndexOutOfBoundsException("Incorrect parameters");
                return ArraySlice.this.new subArraySlice(this.from + i, this.from + i1);
            }
```

##### 4) Dans quel cas va-t-on utiliser une inner class plutôt qu'une classe interne ?

On va utiliser une Inner Class dans le cas ou on veut eviter de la duplication de code.

Par exemple dans l'exo 2 de ce TP, il y'a de la duplication de code car on remet un champ array dans la subArraySlice.

## Exercice 4 : The Slice and The Furious: Tokyo Drift

##### 1) Recopier l'interface Slice du premier exercice dans une interface Slice3. Supprimer la classe interne SubArraySlice ainsi que la méthode array(array, from, to) car nous allons les réimplanter et commenter la méthode subSlice(from, to) de l'interface, car nous allons la réimplanter plus tard. Puis déplacer la classe ArraySlice à l'intérieur de la méthode array(array) et transformer celle-ci en classe anonyme. 

```java
public interface Slice3<E> {

    int size();

    E get(int index);

    @Override
    String toString();
    static <E> Slice3<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new Slice3<>(){
            private final E[] array = tab;
            public int size(){
                return array.length;
            }

            public E get(int index){
                return array[index];
            }

            public String toString() {
                return Arrays.stream(array).map(String::valueOf)
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        };
    }
    //Slice3<E> subSlice(int i, int i1);
}
```

##### 2) Écrire la méthode subSlice(from, to) en utilisant là encore une classe anonyme.