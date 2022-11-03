package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DynamicHashSet<T> {

    private int size;
    private int length;
    private Entry<T>[] table;
    private record Entry<T>(T value, Entry<T> next){}

    @SuppressWarnings("unchecked")
    public DynamicHashSet(){
        size = 0;
        length = 2;
        table = (Entry<T>[]) new Entry<?>[length];
    }

    private int hash(Object value){
        return value.hashCode() & (length - 1);
    }

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

    public int size(){
        return size;
    }

    public boolean contains(Object value){
        Objects.requireNonNull(value);
        int hashValue = hash(value);
        for(var entry = table[hashValue]; entry != null; entry = entry.next){
            if(entry.value.equals(value)) return true;
        }
        return false;
    }

    public void forEach(Consumer<? super T> consumer){
        Objects.requireNonNull(consumer);
        Stream.of(table).flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next)).forEach(e -> consumer.accept(e.value));
    }
}
