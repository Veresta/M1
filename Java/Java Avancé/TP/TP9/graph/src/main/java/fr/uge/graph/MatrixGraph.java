package fr.uge.graph;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

final class MatrixGraph<T> implements Graph<T> {
    private final T[] array;
    private final int nodeCount;

    @SuppressWarnings("unchecked")
    public MatrixGraph(int size) {
        if(size<0) throw new IllegalArgumentException("Negative size");
        this.array = (T[]) new Object[size*size];
        this.nodeCount = size;
    }

    @Override
    public void addEdge(int src, int dst, T weight) {
        Objects.requireNonNull(weight);
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        this.array[src * this.nodeCount + dst] = weight;
    }

    @Override
    public Optional<T> getWeight(int src, int dst) {
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        return Optional.ofNullable(array[src * this.nodeCount + dst]);
    }

    @Override
    public void edges(int src, EdgeConsumer<? super T> edgeConsumer) {
        Objects.requireNonNull(edgeConsumer);
        Objects.checkIndex(src, nodeCount);
        IntStream.range(0, nodeCount).forEach(index ->{
            var weight = getWeight(src, index);
            weight.ifPresent(t -> edgeConsumer.edge(src, index, t));
        });
    }

    @Override
    public Iterator<Integer> neighborIterator(int src) {
        Objects.checkIndex(src, nodeCount);
        return new Iterator<>() {
            private int currentIndex;


            private boolean existAnotherEdge(){
                return IntStream.range(currentIndex, nodeCount).anyMatch(index -> getWeight(src, index).isPresent());
            }
            @Override
            public boolean hasNext() {
                return currentIndex <= nodeCount - 1 && existAnotherEdge();
            }
            @Override
            public Integer next() {
                if(!hasNext()) throw new NoSuchElementException("No element found");
                var res = getWeight(src, currentIndex);
                var tmp =  currentIndex;
                currentIndex++;
                while(res.isEmpty()){
                    res = getWeight(src, currentIndex);
                    tmp = currentIndex;
                    if(!hasNext()) throw new NoSuchElementException("No element found");
                    currentIndex++;
                }
                return tmp;
            }
        };
    }

    public IntStream neighborStream(int src){
        Objects.checkIndex(src, nodeCount);
        return null;
    }
}
