# Compte rendu TP6 Java Avancé

------

## Exercice 2: IntHashSets

##### 1) Quels doivent être les champs de la classe Entry correspondant à une case de la table de hachage sachant que l'on veut stocker les collisions dans une liste chaînée.

The Entry class will have as field :
- The value it contains.
- And the next "Entry" since the colliding values are chained.

##### Quelle doit être la visibilité de cette classe ? Quels doivent être les modificateurs pour chaque champ ? En fait, ne pourrait-on pas utiliser un record plutôt qu'une classe, ici ? Pourquoi ?

The class must be in public, the modifiers of the fields must be __private final__ so that the values are not modified after initialization.
Yes, a record is more suitable.

##### Pour finir, il vaut mieux déclarer Entry en tant que classe interne de la classe IntHashSet plutôt qu'en tant que type dans le package fr.uge.set. Pourquoi ? Quelle doit être la visibilité de Entry ? 

````java
package fr.uge.set;

public final class IntHashSet {

    private final Entry[] table = new Entry[8];
    private int size = 0;

    private record Entry(int value, Entry next) {
    }
}
````

##### 2) Comment écrire la fonction de hachage dans le cas où la taille de la table est 2 ? Pourquoi est-ce que ça n'est pas une bonne idée d'utiliser l'opérateur % ? Écrire une version "rapide" de la fonction de hachage.

For a table of size 2 we can use the modulo to calculate the id of the value to store.

````java
private int hash(int value){
        return value % (1);
    }
````

This is a more expensive operation than a bit-by-bit operation.

##### En suivant la même idée, modifier votre fonction de hachage dans le cas où la taille de la table est une puissance de 2.

````java
private int hash(int value){
        return value % (table.length - 1);
    }

private int hash(int value){
        return value & (table.length - 1);
        }
````

##### 3) Dans la classe IntHashSet, implanter la méthode add. Écrire également la méthode size avec une implantation en O(1).

````java
 public void add(int val) {
        //Check if the value is in the table
        if(contains(val)) return;
        var hash = hash(val);
        table[hash] = new Entry(val, table[hash]);
        size++;
    }
    public int size() {
        return size;
    }
````

##### 4) On cherche maintenant à implanter la méthode forEach. Quelle doit être la signature de la functional interface prise en paramètre de la méthode forEach ?

The signature of the functional interface must be in the form of the Consumer functional interface.
It expects something as input and applies an __accept__ method on it.

##### Quel est le nom de la classe du package java.util.function qui a une méthode ayant la même signature ? 

The Consumer functional interface.

````java
public void forEach(Consumer<Integer> consumer) {
        Objects.requireNonNull(consumer);
        for(var pos : table){
            for(var pos2 = pos; pos2 != null; pos2 = pos2.next){
                consumer.accept(pos2.value);
            }
        }
    }

public void forEach(Consumer<Integer> consumer) {
        Objects.requireNonNull(consumer);
        Stream.of(table).flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next)).forEach(e -> consumer.accept(e.value));
        }
````

##### 5) Écrire la méthode contains.

````java
public boolean contains(int val) {
        for(var pos : table){
            for(var pos2 = pos; pos2 != null; pos2 = pos2.next){
                if(val == pos2.value) return true;
            }
        }
        return false;
    }

public boolean contains(int val) {
        return Stream.of(table).flatMap(entry -> Stream.iterate(entry,Objects::nonNull,Entry::next)).anyMatch(e -> e.value == val);
        }
````

## Exercice 3: DynamicHashSet

##### 1) Avant de générifier votre code, quelle est le problème avec la création de tableau ayant pour type un type paramétré ?

It is not possible to create an array of the form: __new Entry<T>[];__

##### Comment fait-on pour résoudre le problème, même si cela lève un warning. Rappeler pourquoi on a ce warning.

To solve the problem, try to declare something as close as possible to the Entry object, here: __new Entry<?>[];__
The "?" indicates that Entry takes any object as type.
We have to cast this initialization with a __(Entry<T>[])__ so that it is of the same type as our variable.
The problem is that this raises a warning.

##### Peut-on supprimer le warning ici, ou est-ce une bêtise ? Comment fait-on pour supprimer le warning ?

Remove the warning with the __@SuppressWarnings("unchecked")__ annotation, we specify that the warning is not checked,
the developer commits himself when he writes this.
You should use this annotation only when you have to and not every time you have a warning you have an unresolved warning.

````java
public class DynamicHashSet<T> {

    private int size;
    private int length;
    private final Entry<T>[] table;
    private record Entry<T>(T value, Entry<T> next){}

    @SuppressWarnings("unchecked")
    public DynamicHashSet(){
        size = 0;
        table = (Entry<T>[]) new Entry<?>[8];
        length = table.length;
    }

    private int hash(T value){
        return value.hashCode() & (length - 1);
    }

    public void add(T value){
        if(contains(value)){
            return;
        }
        var h = hash(value);
        table[h] = new Entry<>(value, table[h]);
        size++;
    }

    public int size(){
        return size;
    }

    public boolean contains(T value){
        Objects.requireNonNull(value);
        return Stream.of(table).flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next)).anyMatch(e -> e.value.equals(value));
    }

    public void forEach(Consumer<T> consumer){
        Objects.requireNonNull(consumer);
        Stream.of(table).flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next)).forEach(e -> consumer.accept(e.value));
    }
}
````

##### 2) Vérifier la signature de la méthode contains de HashSet et expliquer pourquoi on utilise un type plus général que E.

````java
public boolean contains(Object o) {
        return map.containsKey(o);
    }
````

Use a more general type than E to be able to use __Object.equals__ to test if an object is already present.

##### 3) Modifier le code de la méthode add pour implanter l'algorithme d'agrandissement de la table.
##### L'idée est que si la longueur du tableau est inférieure à la moitié du nombre d’éléments, il faut doubler la taille du tableau et re-stocker tous les éléments (pas besoin de tester si un élément existe déjà dans le nouveau tableau).

````java
    public void add(T value){
        if(contains(value)){
            return;
        }
        if(length / 2 <= size + 1){
            resize();
        }
        var h = hash(value);
        table[h] = new Entry<>(value, table[h]);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        length = length * 2;
        var auxTable = (Entry<T>[]) new Entry<?>[length];
        Stream.of(table)
            .flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next))
            .forEach(e -> {
                var h = hash(e.value);
                auxTable[h] = new Entry<>(e.value, auxTable[h]);
            });
        table = auxTable;
    }

    public boolean contains(Object value){
        Objects.requireNonNull(value);
        int hashValue = hash(value);
        for(var entry = table[hashValue]; entry != null; entry = entry.next){
            if(entry.value.equals(value)) return true;
        }
        return false;
    }
````

## Exercice 4: Wild cards

##### 1) Écrire une méthode addAll, qui permet de recopier une collection d’éléments dans le DynamicHashSet courant.

````java
    public void addAll(Collection<? extends T> collection){
        Objects.requireNonNull(collection);
        collection.stream().forEach(this::add);
    }
````

At first, I used here a Collection<T> collection as argument to my method. At first, I didn't understand why the "shouldTakeTheRightTypeOfCollectionAsArgumentOfAddAll()" test doesn't compil. 
The problem was that my DynamicHashSet object was initialized on <Object> except that as argument to the addAll method there was a String collection while it was expecting an object collection.
To solve the problem, I used a wild card to make my addAll method accept all objects that inherit from T.
So now, because String inherits from Object, the test works.

##### 2) Regarder la signature de la méthode addAll dans java.util.Collection. Avez-vous la même signature ? Modifier votre code si nécessaire.

(cf Q1)

##### Expliquer ce que veut dire le '?' dans la signature de addAll

? is a way symbol to point an unnamed object.

##### 3) Ne devrait-on pas aussi utiliser un '?' dans la signature de la méthode forEach ?

Yes, we can use an __Consumer<? super T> consumer__ now in argument.