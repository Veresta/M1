package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice<E> permits Slice.ArraySlice, Slice.SubArraySlice {

    static <E> Slice<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new ArraySlice<>(tab);
    }

    static <E> Slice<E> array(E[] tab, int from, int to) {
        Objects.requireNonNull(tab);
        return new SubArraySlice<>(tab,from,to);
    }

    int size();

    Object get(int index);

    @Override
    String toString();

    Slice<E> subSlice(int i, int i1);

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
            if (index > array.length || index < 0) throw new IndexOutOfBoundsException("Incorrect index");
            return array[index];
        }
        @Override
        public String toString(){
            return Arrays.stream(array).map(String::valueOf)
                    .collect(Collectors.joining(", ","[","]"));
        }

        @Override
        public Slice<E> subSlice(int i, int i1){
            if(i > i1 || i1 > array.length || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new SubArraySlice<>(array, i, i1);
        }
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
            if ((index >= this.size()) || (index < 0) || index >= to) throw new IndexOutOfBoundsException("Incorrect index");
            return array[index+from];
        }
        @Override
        public String toString() {
            return Arrays.stream(array, from, to).map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        @Override
        public Slice<E> subSlice(int i, int i2){
            if(i > i2 || i2 < 0  || (i2 - i) > this.size() || i < 0) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new SubArraySlice<>(array,this.from + i, this.from + i2);
        }
    }
 }
