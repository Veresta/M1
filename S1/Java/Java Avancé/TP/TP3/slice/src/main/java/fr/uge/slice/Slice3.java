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

            @Override
            public String toString() {
                return Arrays.stream(array).map(String::valueOf)
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        };
    }

    static <E> Slice3<E> array(E[] tab, int from, int to){
        Objects.requireNonNull(tab);
        return array(tab).subSlice(from, to);
    }
    default Slice3<E> subSlice(int i, int i2){
        if(i > i2 || i2-i > this.size() || i < 0 ) throw new IndexOutOfBoundsException("Incorrect parameters");
        Slice3<E> tmp = this;
        return new Slice3<>() {
            private final int from = i;
            private final int to = i2;
            @Override
            public int size() {
                return to - from;
            }
            @Override
            public E get(int index) {
                if ((index >= this.size()) || (index < 0) || index >= to)
                    throw new IndexOutOfBoundsException("Incorrect index");
                return tmp.get(index + from);
            }
            @Override
            public String toString() {
                var res = new StringBuilder();
                if(this.size() == 0) return "[]";
                res.append("[");
                for(var i = from; i < to; i++){
                    if(i ==(to - 1))res.append(tmp.get(i));
                    else res.append(tmp.get(i)).append(", ");
                }
                res.append("]");
                return res.toString();
            }
        };
    }
}
