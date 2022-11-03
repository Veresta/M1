package fr.uge.simd;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.Objects;

public class VectorComputation {

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public static int sum(int[] array) {
        Objects.requireNonNull(array);
        var sum = IntVector.zero(SPECIES);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        var i = 0;
        for(; i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES, array, i);
            sum = sum.add(view);
        }
        var res = 0;
        for(;i < size; i++){
            res+= array[i];
        }
        return sum.reduceLanes(VectorOperators.ADD) + res;
    }
    public static int sumMask(int[] array) {
        Objects.requireNonNull(array);
        var sum = IntVector.zero(SPECIES);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        for(var i = 0; i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES, array, i);
            sum = sum.add(view);
        }
        var mask = SPECIES.indexInRange(loopBound, size);
        var view = IntVector.fromArray(SPECIES, array, loopBound, mask);
        sum = sum.add(view);
        return sum.reduceLanes(VectorOperators.ADD);
    }

    public static int min(int[] array) {
        Objects.requireNonNull(array);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        var min = IntVector.broadcast(SPECIES, Integer.MAX_VALUE);
        var i = 0;
        for(;i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES,array,i);
            min = min.min(view);
        }
        var res = min.reduceLanes(VectorOperators.MIN);
        for(;i < size; i++){
            if(array[i] < res){
                res = array[i];
            }
        }
        return res;
    }

    public static int minMask(int[] array) {
        Objects.requireNonNull(array);
        var size = array.length;
        var loopBound = SPECIES.loopBound(size);
        var min = IntVector.broadcast(SPECIES, Integer.MAX_VALUE);
        for(var i = 0; i < loopBound; i+= SPECIES.length()){
            var view = IntVector.fromArray(SPECIES,array,i);
            var mask = SPECIES.indexInRange(i, loopBound);
            view = IntVector.fromArray(SPECIES, array, i, mask);
            min = min.min(view);
        }
        //mask treatment <=> 1 test doesn't work
        //var mask = SPECIES.indexInRange(loopBound, size);
        //var view = IntVector.fromArray(SPECIES, array, loopBound, mask);
        //min = min.min(view);
        return min.reduceLanes(VectorOperators.MIN);
    }
}
