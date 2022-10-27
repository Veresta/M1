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
        return 0;
    }

    public static int minMask(int[] array) {
        return 0;
    }
}
