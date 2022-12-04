package fr.uge.graph;

import java.util.*;

final class NodeMapGraph<T> implements Graph<T> {
    private final HashMap<Integer, T>[] hashTable;
    private final int nodeCount;

    @SuppressWarnings("unchecked")
    public NodeMapGraph(int size) {
        if(size<0) throw new IllegalArgumentException("Negative size");
        this.hashTable = (HashMap<Integer, T>[]) new HashMap[size];
        this.nodeCount = size;
    }

    @Override
    public void addEdge(int src, int dst, T weight) {
        Objects.requireNonNull(weight);
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        if(hashTable[src] == null){
            hashTable[src] = new HashMap<>(){{put(dst, weight);}};
        }else hashTable[src].put(dst, weight);
        /*if(hashTable.containsKey(src)){
            var tmp = hashTable.get(src);
            tmp.put(dst,weight);
        }else hashTable.put(src, new HashMap<>(){{put(dst,weight);}});*/
    }

    @Override
    public Optional<T> getWeight(int src, int dst) {
        Objects.checkIndex(src, nodeCount);
        Objects.checkIndex(dst, nodeCount);
        if(hashTable[src] == null) return Optional.empty();
        return Optional.ofNullable(hashTable[src].get(dst));
    }
    @Override
    public void edges(int src, EdgeConsumer<? super T> edgeConsumer){
        Objects.requireNonNull(edgeConsumer);
        Objects.checkIndex(src, nodeCount);
        if(hashTable[src] == null) return;
        hashTable[src].forEach((key, value) -> {
            var weight = getWeight(src, key);
            weight.ifPresent(t -> edgeConsumer.edge(src, key, t));
        });
    }

    @Override
    public Iterator<Integer> neighborIterator(int src) {
        Objects.checkIndex(src, nodeCount);
        if(hashTable[src] == null) throw new NoSuchElementException();
        return hashTable[src].keySet().iterator();
    }
}
