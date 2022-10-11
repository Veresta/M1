package fr.uge.slice;

import java.util.Arrays;
import java.util.Objects;

public interface Slice<E> {

    Object[] array = new Object[0];
    int size = 0;

    static Slice<E> array(Object[] tab){
        Objects.requireNonNull(tab);
        array = Arrays.copyOf(tab,tab.length);
        size = tab.length;
        return null;
    }
    int size();
    E get(int index);

    class ArraySlice implements Slice {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Object get(int index) {
            return null;
        }
    }
}
