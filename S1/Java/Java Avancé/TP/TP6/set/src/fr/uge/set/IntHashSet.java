package fr.uge.set;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class IntHashSet {

    private final Entry[] table = new Entry[8];
    private int size = 0;
    private record Entry(int value, Entry next){}

    private int hash(int value){
        return value & (table.length - 1);
    }

    public void add(int val) {
        if(contains(val)){
            return;
        }
        var hash = hash(val);
        table[hash] = new Entry(val, table[hash]);
        size++;
    }
    public int size() {
        return size;
    }

    public void forEach(Consumer<Integer> consumer) {
        Objects.requireNonNull(consumer);
        Stream.of(table).flatMap(entry -> Stream.iterate(entry, Objects::nonNull, Entry::next)).forEach(e -> consumer.accept(e.value));
    }

    public boolean contains(int val) {
        return Stream.of(table).flatMap(entry -> Stream.iterate(entry,Objects::nonNull,Entry::next)).anyMatch(e -> e.value == val);
    }
}
