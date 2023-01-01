package fr.uge.numeric;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

public final class NumericVec<T> implements Iterable<T> {
    private long[] internal;
    private int size; //Current size
    private final Function<T, Long> into;
    private final Function<Long, T> from;

    private NumericVec(Function<T, Long> into, Function<Long, T> from){
        Objects.requireNonNull(into);
        Objects.requireNonNull(from);
        internal = new long[0];
        this.into = into;
        this.from = from;
    }

    public static NumericVec<Integer> ints(int... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(Integer::longValue, Long::intValue);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public static NumericVec<Long> longs(long... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(x -> x, x -> x);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public static NumericVec<Double> doubles(double... elem) {
        Objects.requireNonNull(elem);
        var tmp = new NumericVec<>(Double::doubleToRawLongBits, Double::longBitsToDouble);
        for(var e : elem){
            tmp.add(e);
        }
        return tmp;
    }

    public void add(T value){
        Objects.requireNonNull(value);
        if(size + 1 > internal.length){
            resizeInternal();
        }
        internal[size] = into.apply(value);
        size++;
    }

    public T get(int index){
        Objects.checkIndex(index, size);
        return from.apply(internal[index]);
    }

    public int size(){
        return size;
    }

    private void resizeInternal(){
        internal = Arrays.copyOf(internal, 2 * size + 1);
    }

    public String toString(){
        return Arrays.stream(internal)
                .mapToObj(x -> from.apply(x).toString())
                .limit(size)
                .collect(Collectors.joining(", ","[","]"));
    }

    @Override
    public Iterator<T> iterator() {
        var tmp_size = size;
        return new Iterator<>() {
            private int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < tmp_size;
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException("No next");
                var res = get(currentIndex);
                currentIndex++;
                return res;
            }
        };
    }

    public void addAll(NumericVec<T> seq2) {
        Objects.requireNonNull(seq2);
        IntStream.range(0, seq2.size).forEach(index -> add(seq2.get(index)));
    }

    public <E> NumericVec<E> map(Function<? super T, ? extends E> fun, Supplier<NumericVec<E>> vec) {
        Objects.requireNonNull(fun);
        Objects.requireNonNull(vec);
        var tmp = vec.get();
        for(var e : this){
            tmp.add(fun.apply(e));
        }
        return tmp;
    }


    public static <E> Collector<E, ?, NumericVec<E>> toNumericVec(Supplier<NumericVec<E>> fun) {
        Objects.requireNonNull(fun);
        return Collector.of(fun, NumericVec::add, (nvec, other) -> {
            nvec.addAll(other);
            return nvec;
        });
    }

    private Spliterator<T> spliterator(int start, int end) {
        return new Spliterator<>() {

            private int currentPos = start;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (currentPos == end) {
                    return false;
                }
                action.accept(get(currentPos++));
                return true;
            }

            @Override
            public Spliterator<T> trySplit() {
                var middle = (end + currentPos) >>> 1;
                if (middle == currentPos || end - start < 1024) {
                    return null;
                }
                var split = spliterator(currentPos, middle);
                currentPos = middle;
                return split;
            }

            @Override
            public long estimateSize() {
                return end - start;
            }

            @Override
            public int characteristics() {
                return IMMUTABLE | NONNULL | ORDERED | SIZED | SUBSIZED;
            }
        };
    }
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(0, size), size >= 1024);
    }
}
