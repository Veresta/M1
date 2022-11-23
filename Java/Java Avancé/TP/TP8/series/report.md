# Compte rendu TP8 Java Avancé

------

## Exercice 2 - TimeSeries


##### 1) Dans un premier temps, on va créer une classe TimeSeries ainsi qu'un record Data à l'intérieur de la classe TimeSeries qui représente une paire contenant une valeur de temps (timestamp) et un élément (element).

 ```java
public class TimeSeries<T> {

    public record Data<T>(long timestamp,T element){
        public Data{
            Objects.requireNonNull(element);
        }
    }
}
```

##### 2) On souhaite maintenant écrire les méthodes dans TimeSeries : add(timestamp, element)
##### La valeur de timestamp doit toujours être supérieure ou égale à la valeur du timestamp précédemment inséré (s'il existe).
##### size qui renvoie le nombre d'éléments ajoutés.
##### get(index) qui renvoie l'objet Data se trouvant à la position indiquée par l'index (de 0 à size - 1).

##### En interne, la classe TimeSeries stocke des instances de Data dans une liste qui s'agrandit dynamiquement. 
````java
public class TimeSeries<T> {
    private final ArrayList<Data<T>> tab = new ArrayList<>();
    private int size = 0;
    public record Data<T>(long timestamp,T element){
        public Data{
            Objects.requireNonNull(element);
        }
    }

    public int size(){
        return size;
    }

    public void add(long timestamp, T value){
        if(size == 0){
            tab.add(new Data<>(timestamp, value));
            size++;
            return;
        }

        if(tab.get(size-1).timestamp > timestamp){
            throw new IllegalStateException();
        }
        tab.add(new Data<>(timestamp, value));
        size++;
    }

    public Data<T> get(int index){
        Objects.checkIndex(index, size);
        return tab.get(index);
    }
}
````

##### 3)On souhaite maintenant créer une classe interne publique Index ainsi qu'une méthode index permettant de créer un Index stockant les indices des données de la TimeSeries sur laquelle la méthode index est appelée. L'objectif est de pouvoir ensuite accéder aux Data correspondantes dans le TimeSeries. Un Index possède une méthode size indiquant combien d'indices il contient.
##### Seuls les indices des éléments ajoutés avant l'appel à la méthode index() doivent être présents dans l'Index.
##### En interne, un Index stocke un tableau d'entiers correspondants à chaque indice. 

````java
    public class Index implements {
        private final int[] index;
        private final int size;
        private Index(int[] list){
            this.index = list;
            this.size = list.length;
        }
        public int size() {
            return size;
        }
    }
    public Index index(){
        return new Index(IntStream.range(0,size).toArray());
    }
````

##### 4) On souhaite pouvoir afficher un Index, c'est à dire afficher les éléments (avec le timestamp) référencés par un Index, un par ligne avec un pipe (|) entre le timestamp et l'élément. 
```java
    @Override
    public String toString(){
        return Arrays.stream(index)
        .mapToObj(elem -> TimeSeries.this.get(elem).toString())
        .collect(Collectors.joining("\n"));
    }
```

##### 5) On souhaite ajouter une autre méthode index(lambda) qui prend en paramètre une fonction/lambda qui est appelée sur chaque élément de la TimeSeries et indique si l'élément doit ou non faire partie de l'index.
La lambda doit être de type Predicate.

````java
    public Index index(Predicate<? super T> fun){
        return new Index(IntStream.range(0, size).filter(i -> fun.test(this.get(i).element)).toArray());
    }
````

##### 6) Dans la classe Index, écrire une méthode forEach(lambda) qui prend en paramètre une fonction/lambda qui est appelée avec chaque Data référencée par les indices de l'Index.
##### Par exemple, avec la TimeSeries contenant les Data (24 | "hello"), (34 | "time") et (70 | "series") et un Index [0, 2], la fonction sera appelée avec les Data (24 | "hello") et (70 | "series").
##### Quel doit être le type du paramètre de la méthode forEach(lambda) ? 

La lambda doit être de type Consumer.

##### Écrire la méthode forEach(lambda) dans la classe Index.

````java
    public void forEach(Consumer<? super Data<T>> fun){
        IntStream.range(0, size).forEach(index -> fun.accept(TimeSeries.this.tab.get(index)));
    }
````

##### 7) Quelle interface doit implanter la classe Index pour pouvoir être utilisée dans une telle boucle ?

On doit implémenter l'interface Iterable.

##### Quelle méthode de l'interface doit-on implanter ? Et quel est le type de retour de cette méthode ? Faites les modifications qui s'imposent dans la classe Index et vérifiez que les tests marqués "Q7" passent.

Il faut implementer la méthode Iterator qui aura comme valeur de retour une classe anonyme Iterator<Data<T>>.

````java
public class Index implements Iterable<Data<T>> {
    private final int[] index;
    private final int size;
    private Index(int[] list){
        this.index = list;
        this.size = list.length;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString(){
        return Arrays.stream(index)
                .mapToObj(elem -> TimeSeries.this.get(elem).toString())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void forEach(Consumer<? super Data<T>> fun){
        IntStream.range(0, size).forEach(index -> fun.accept(TimeSeries.this.get(index)));
    }

    @Override
    public Iterator<Data<T>> iterator() {
        return new Iterator<>() {
            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public Data<T> next() {
                if(currentIndex >= size) throw new NoSuchElementException("No element found");
                var res = TimeSeries.this.get(index[currentIndex]);
                currentIndex++;
                return res;
            }
        };
    }
}
````

##### 8) On veut ajouter une méthode or sur un Index qui prend en paramètre un Index et renvoie un nouvel Index qui contient à la fois les indices de l'Index courant et les indices de l'Index passé en paramètre.
##### Il ne doit pas être possible de faire un or avec deux Index issus de TimeSeries différentes.

Ajout de la methode belongsTo pour indiquer à quelle instance appartient quel index.
````java
    public TimeSeries<T> belongsTo(){
            return TimeSeries.this;
    }
````

##### En termes d'implantation, on peut faire une implantation en O(n) mais elle est un peu compliquée à écrire. On se propose d'écrire une version en O(n.log(n)) en concaténant les Stream de chaque index puis en triant les indices et en retirant les doublons.

````java
        public Index or(Index indexBis){
            if(!this.belongsTo().equals(indexBis.belongsTo())) throw new IllegalArgumentException("Both doesn't belongs to the same instance");
            var concat = IntStream.concat(Arrays.stream(this.index), Arrays.stream(indexBis.index));
            return new Index(concat.sorted().distinct().toArray());
        }
````

##### 9) Même question que précédemment, mais au lieu de vouloir faire un or, on souhaite faire un and entre deux Index.
