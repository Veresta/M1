#!/usr/bin/perl
use strict;
use warnings;


sub TableMul1 {
    my ($n) = @_;
    for(my $i = 1; $i < $n+1; $i++){
        for(my $j = 1; $j < $n+1; $j++){
            printf('%5d', $i*$j);
        }
        printf("\n");
    }
}

printf(TableMul1($ARGV[0] // 4)."\n"); #Variable par défault 4, si $ARGV[0] == undef alors 4


sub TableMul2 {
    my ($n) = @_;
    foreach my $i (1..$n) {
        foreach my $j (1..$n){
            printf('%5d', $i*$j);
        }
        printf("\n");
    }
}

printf(TableMul2($ARGV[0] // 4)."\n"); #Variable par défault 4, si $ARGV[0] == undef alors 4

sub TableMul3 {
    my ($n) = @_;
    my $chaine = '';
    foreach my $i (1..$n) {
        foreach my $j (1..$n){
           $chaine = sprintf("$chaine%5d", $i*$j);
        }
        $chaine= sprintf("$chaine\n");
    }
    return $chaine;
}

printf(TableMul3($ARGV[0] // 4)); #Variable par défault 4, si $ARGV[0] == undef alors 4