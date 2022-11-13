package fr.uge.fifo;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class Fifo<E> implements Iterable<E> {

    private int size;
    private final E[] fifo;
    private int tail;
    private int head;

    @SuppressWarnings("unchecked")
    public Fifo(int length){
        if(length <= 0) throw new IllegalArgumentException("Negative length");
        fifo = (E[] ) new Object[length];
    }

    public void offer(E value) {
        Objects.requireNonNull(value);
        if(size == fifo.length) throw new IllegalStateException("Fifo full");
        fifo[tail] = value;
        tail = (tail == fifo.length-1 ? 0 : tail + 1);
        size++;
    }

    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = fifo[head];
        fifo[head] = null;
        size--;
        head = (head == fifo.length-1 ? 0 : head + 1);
        return res;
    }

    @Override
    public String toString(){
        var joiner = new StringJoiner(", ","[","]");
        var tmp_head = head;
        for(var i = 0; i < size; i++){
            joiner.add(fifo[tmp_head].toString());
            tmp_head++;
            if(tmp_head >= fifo.length) tmp_head = 0;
        }
        return joiner.toString();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int currentIndex = head;
            private int numberIteration;

            @Override
            public boolean hasNext() {
                return numberIteration < size;
            }

            @Override
            public E next() {
                if(currentIndex >= size)  throw new NoSuchElementException("No element found");
                var res = fifo[currentIndex];
                numberIteration++;
                currentIndex = (currentIndex == fifo.length-1 ? 0 : currentIndex + 1);
                return res;
            }
        };
    }
}
