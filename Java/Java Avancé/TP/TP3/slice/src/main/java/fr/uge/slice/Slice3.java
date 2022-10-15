package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Slice3<E> {

    int size();

    E get(int index);

    @Override
    String toString();
    static <E> Slice3<E> array(E[] tab) {
        Objects.requireNonNull(tab);
        return new Slice3<>(){
            private final E[] array = tab;
            public int size(){
                return array.length;
            }

            public E get(int index){
                return array[index];
            }

            public String toString() {
                return Arrays.stream(array).map(String::valueOf)
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        };
    }
    default Slice3<E> subSlice(int i, int i2){
        if(i > i2 || i > this.size() || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
        //Récup la val ici puis ré injecter dans la class anno.
        return new Slice3<>() {
            private final int from = i;
            private final int to = i2;
            @Override
            public int size() {
                return from - to;
            }
            @Override
            public E get(int index) {
                if ((index >= this.size()) || (index < 0) || index >= to) throw new IndexOutOfBoundsException("Incorrect index");
                return this.array.get(index);
            }
        };
    }
}
