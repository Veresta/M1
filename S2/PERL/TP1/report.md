# Rapport TP1 - PERL

### Mathis MENAA

## Exercice 1 :

Ma version de PERL : 5.30

Les options de cette interpreteur sont : -W


## Exercice 2 :

(voir mystery.pl)

## Exercice 3 : 

Ecrire une fonction renvoyant vrai si la concaténation de x et y égal z.

#!/usr/bin/perl
use strict;
use warnings;

sub SommeTest {
    my ($x, $y, $z) = @_;
    if("$x"."$y" eq "$z"){
        return (1);
    }
    return (0);
}

if(SommeTest(1,2,12)){
    printf("MARCHE\n");
}

## Exercice 4 : 