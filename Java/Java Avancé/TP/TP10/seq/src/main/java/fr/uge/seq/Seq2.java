package fr.uge.seq;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
