package fr.uge.simd;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class test {

    private static final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;

    public static void main(String[] args) {
        var v1 = IntVector.fromArray(species, new int[] {7, 3, 3, 5}, 0);
        var v2 = IntVector.broadcast(species,34);
        var v3 = v1.lanewise(VectorOperators.ADD, v2);

        System.out.println(v3);

    }
}
