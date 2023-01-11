package fr.uge.seq;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Seq<T> implements Iterable<T> {

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
        Objects.checkIndex(index, size());
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

    @Override
    public Iterator<T> iterator() {
        var currentSize = size();
        return new Iterator<>() {

            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize;
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

    public void forEach(Consumer<? super T> func) {
        Objects.requireNonNull(func);
        internal.stream().map(mapper).forEach(func);
    }

    public <F> Seq<F> map(Function<? super T, ? extends F> newMapper) {
        Objects.requireNonNull(mapper);
        return new Seq<>(this.internal, this.mapper.andThen(newMapper));
    }

    public Optional<?> findFirst() {
        return Optional.of(internal.stream().map(mapper).findFirst()).get();
    }

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
    /*
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
     */
}
