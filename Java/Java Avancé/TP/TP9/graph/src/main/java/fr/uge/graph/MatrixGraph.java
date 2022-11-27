package fr.uge.graph;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

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
    public void edges(int src, EdgeConsumer<T> edgeConsumer) {
        Objects.requireNonNull(edgeConsumer);
        Objects.checkIndex(src, nodeCount);
        IntStream.range(0, nodeCount).forEach(index -> edgeConsumer.edge(index*nodeCount,nodeCount,));

    }
}
