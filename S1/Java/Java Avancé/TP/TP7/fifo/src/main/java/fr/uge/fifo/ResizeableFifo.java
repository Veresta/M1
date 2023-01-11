package fr.uge.fifo;

import java.util.*;

public class ResizeableFifo<E> extends AbstractQueue<E> implements Queue<E> {
    private int size;
    private E[] resizeableFifo;
    private int tail;
    private int head;

    @SuppressWarnings("unchecked")
    public ResizeableFifo(int length){
        if(length <= 0) throw new IllegalArgumentException("Negative length");
        resizeableFifo = (E[] ) new Object[length];
    }

    @Override
    public boolean offer(E value) {
        Objects.requireNonNull(value);
        if(size == resizeableFifo.length) resize();
        resizeableFifo[tail] = value;
        tail = (tail == resizeableFifo.length-1 ? 0 : tail + 1);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        var resizedFifo = (E[] ) new Object[resizeableFifo.length*2];
        System.arraycopy(resizeableFifo,head,resizedFifo,0, resizeableFifo.length - head);
        System.arraycopy(resizeableFifo,0,resizedFifo, resizeableFifo.length - head, tail);
        head = 0;
        tail = size;
        resizeableFifo = resizedFifo;
    }

    @Override
    public E poll() {
        if(size == 0) throw new IllegalStateException("Fifo empty");
        var res = resizeableFifo[head];
        resizeableFifo[head] = null;
        size--;
        head = (head == resizeableFifo.length-1 ? 0 : head + 1);
        return res;
    }

    @Override
    public E peek() {
        return resizeableFifo[head];
    }

    @Override
    public String toString(){
        var joiner = new StringJoiner(", ","[","]");
        var tmp_head = head;
        for(var i = 0; i < size; i++){
            joiner.add(resizeableFifo[tmp_head].toString());
            tmp_head++;
            if(tmp_head >= resizeableFifo.length) tmp_head = 0;
        }
        return joiner.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
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
                var res = resizeableFifo[currentIndex];
                numberIteration++;
                currentIndex = (currentIndex == resizeableFifo.length-1 ? 0 : currentIndex + 1);
                return res;
            }
        };
    }
}
