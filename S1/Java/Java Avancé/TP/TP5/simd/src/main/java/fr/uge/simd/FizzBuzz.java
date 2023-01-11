package fr.uge.simd;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.sql.Array;

public class FizzBuzz {

    private final static int[] v = {-3,  1,  2, -1,  4, -2, -1,  7,  8, -1, -2, 11, -1, 13, 14};
    private final static int[] delta = {0, 15, 15,  0, 15,  0,  0, 15, 15,  0,  0, 15,  0, 15, 15};

    private static int fizzbuzzAt(int index) {
        if (index % 15 == 0) {
            return -3;
        }
        if (index % 5 == 0) {
            return -2;
        }
        if (index % 3 == 0) {
            return -1;
        }
        return index;
    }

    public static int[] fizzBuzzVector128(int length) {
        var species = IntVector.SPECIES_128;
        var resTab = new int[length];
        var values = IntVector.fromArray(species, v, 0);
        var deltas = IntVector.fromArray(species, delta, 0);
        var loopbound = species.loopBound(length);
        var i = 0;
        for(;i < loopbound; i+= species.length()){
            values.intoArray(resTab,i);
            values.add(deltas);
        }
        return resTab;
    }

    public static int[] fizzBuzzVector256(int i) {
        return null;
    }

    public static int[] fizzBuzzVector128AddMask(int i) {
        return  null;
    }
}
