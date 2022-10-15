# Compte rendu TP3 Java Avancé

### Mathis MENAA
------

## Exercice 2: The Slice and Furious

##### 1) Implanter la classe Slice et les méthodes array, size et get(index). 

Creation of the sealed Slice interface, with an inner class ArraySlice.
Add abstract method and the static method for define an initialize an ArraySlice.

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

Use a stream to print our array.

```java
@Override
        public String toString(){
            return Arrays.stream(array).map(x -> (String::valueOf)
                    .collect(Collectors.joining(", ","[","]"));
        }
```

##### 3) On souhaite ajouter une surcharge à la méthode array qui, en plus de prendre le tableau en paramètre, prend deux indices from et to et montre les éléments du tableau entre from inclus et to exclus. 

Creation of a new class (SubArraySlice) which allow to manage Slice with limit.
Add overloading of Array method which return a new SubArraySlice.

```java
static <E> Slice<E> array(E[] tab, int from, int to) {
        Objects.requireNonNull(tab);
        return new SubArraySlice<>(tab,from,to);
    }
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

Add the subslice method in both class. The method return a new SubArraySlice with the limit given.


```java

@Override
        public Slice<E> subSlice(int i, int i1){
            if(i > i1 || i1 > array.length || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new SubArraySlice<>(array, i, i1);
        }
@Override
    public Slice<E> subSlice(int i, int i2){
        if(i > i2 || i2 < 0  || (i2 - i) > this.size() || i < 0) throw new IndexOutOfBoundsExcepti("Incorrect parameters");
            return new SubArraySlice<>(array,this.from + i, this.from + i2);
        }
```

## Exercice 3: The Slice 2 Furious

##### 1) Recopier l'interface Slice de l'exercice précédent dans une interface Slice2. Vous pouvez faire un copier-coller de Slice dans même package, votre IDE devrait vous proposer de renommer la copie. Puis supprimer la classe interne SubArraySlice ainsi que la méthode array(array, from, to) car nous allons les réimplanter et commenter la méthode subSlice(from, to) de l'interface, car nous allons la ré-implanter aussi, mais plus tard. 

Initialize the interface with the last ArraySlice implementation.

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

Create the Inner class SubArraySlice with two fields : from and to.
Add and adapt all abstract method of the interface Slice2.


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

Create the array(E[] tab, int from, int to) method whitch after checking the parameters, return an ArraySlice of the tab with the application of the method subslice.

The subSlice method for the ArraySlice class just return an object subArraySlice with the two parameter from and to.

The subSlice method for the subArraySlice class return a new subArraySlice based on the first ArraySlice at whitch we add the new interval 'from' and 'to' to the current.


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

We are going to use an Inner Class in the case where we want to avoid code duplication.

For example in the exercice 2, there is some duplication of code because we put an other array field in the subArraySlice class.

## Exercice 4 : The Slice and The Furious: Tokyo Drift

##### 1) Recopier l'interface Slice du premier exercice dans une interface Slice3. Supprimer la classe interne SubArraySlice ainsi que la méthode array(array, from, to) car nous allons les réimplanter et commenter la méthode subSlice(from, to) de l'interface, car nous allons la réimplanter plus tard. Puis déplacer la classe ArraySlice à l'intérieur de la méthode array(array) et transformer celle-ci en classe anonyme. 

Create the method array(E[] tab) whitch returning an annonym class for manipulate the array.

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

##### 2) Écrire la méthode subSlice(from, to) en utilisant là encore une classe anonyme. Puis fournissez une implantation à la méthode array(array, from, to).

Add an overload of the array method with two others parameters : from and to. This method just return the application of the method array(tab) plus a call on the method subslice with from and to.

For the subSlice method, we need to get the acces of the array. That's why I get the in a variable the value of "this" to use it in the annonym class returned.

```java
static <E> Slice3<E> array(E[] tab, int from, int to){
        Objects.requireNonNull(tab);
        return array(tab).subSlice(from, to);
    }

 default Slice3<E> subSlice(int i, int i2){
        if(i > i2 || i2-i > this.size() || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
        Slice3<E> tmp = this;
        return new Slice3<>() {
            private final int from = i;
            private final int to = i2;
            @Override
            public int size() {
                return to - from;
            }
            @Override
            public E get(int index) {
                if ((index >= this.size()) || (index < 0) || index >= to)
                    throw new IndexOutOfBoundsException("Incorrect index");
                return tmp.get(index + from);
            }
            @Override
            public String toString() {
                var res = new StringBuilder();
                if(this.size() == 0) return "[]";
                res.append("[");
                for(var i = from; i < to; i++){
                    if(i ==(to - 1))res.append(tmp.get(i));
                    else res.append(tmp.get(i)).append(", ");
                }
                res.append("]");
                return res.toString();
            }
        };
```

##### 3) Dans quel cas va-t-on utiliser une classe anonyme plutôt qu'une classe interne ?

We prefer to use annonym class when we wants to simplify the syntax used for class inheritance or interface implementation for simple codes.