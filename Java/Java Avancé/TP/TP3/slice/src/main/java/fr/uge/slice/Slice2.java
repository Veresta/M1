package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice2<E> {
    static <E> Slice2<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new Slice2.ArraySlice<>(tab);
    }
    int size();

    E get(int index);

    @Override
    String toString();

    static <E> Slice2<E> array(E[] tab, int from, int to){
      Objects.requireNonNull(tab);
      return new ArraySlice<>(tab).subSlice(from, to);
    }

    Slice2<E> subSlice(int i, int i1);

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
        public String toString() {
            return Arrays.stream(array).map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        @Override
        public Slice2<E> subSlice(int i, int i1){
            if(i > i1 || i1 > this.size() || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
            return new subArraySlice(i, i1);
        }

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

            @Override
            public Slice2<E> subSlice(int i, int i1) {
                if(i > i1 || i1 < 0  || (i1 - i) > this.size() || i < 0) throw new IndexOutOfBoundsException("Incorrect parameters");
                return ArraySlice.this.new subArraySlice(this.from + i, this.from + i1);
            }
        }
    }
}

