package fr.uge.series;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries<T> {
    private final ArrayList<Data<T>> tab = new ArrayList<>();
    private int size = 0;
    public record Data<T>(long timestamp,T element){
        public Data{
            Objects.requireNonNull(element);
        }

        @Override
        public String toString(){
            return timestamp + " | " + element;
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
            throw new IllegalStateException("timestamp is inferior comparing to the timestamp of the last value inserted");
        }
        tab.add(new Data<>(timestamp, value));
        size++;
    }

    public Data<T> get(int index){
        Objects.checkIndex(index, size);
        return tab.get(index);
    }

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

        public Index or(Index indexBis){
            if(!this.belongsTo().equals(indexBis.belongsTo())) throw new IllegalArgumentException("Both doesn't belongs to the same instance");
            var concat = IntStream.concat(Arrays.stream(this.index), Arrays.stream(indexBis.index));
            return new Index(concat.sorted().distinct().toArray());
        }

        public TimeSeries<T> belongsTo(){
            return TimeSeries.this;
        }

        public Index and(Index indexBis) {
            if(!this.belongsTo().equals(indexBis.belongsTo())) throw new IllegalArgumentException("Both doesn't belongs to the same instance");
            Supplier<IntStream> tmp = () -> Arrays.stream(this.index).distinct();
            var res = new ArrayList<Integer>();
            for(var i : indexBis.index){
                if(tmp.get().anyMatch(indice -> indice == i)) res.add(i);
            }
            return new Index(res.stream().mapToInt(Integer::intValue).sorted().toArray());
        }
    }
    public Index index(){
        return new Index(IntStream.range(0,size).toArray());
    }
    public Index index(Predicate<? super T> fun){
        return new Index(IntStream.range(0, size).filter(i -> fun.test(this.get(i).element)).toArray());
   }
}


