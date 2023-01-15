#!/usr/bin/perl
use strict;
use warnings;


sub Fact {
    my ($n) = @_;
    if($n == 1){
        return 1;
    }
    return $n * Fact($n-1);
}


sub Fibo {
    my ($n) = @_;
    my @tab;
    foreach my $ind (0..$n){
        if($ind == 0 or $ind == 1){
            push(@tab, $ind);
        }else{
            push(@tab, $tab[$ind-1] + $tab[$ind-2]);
        }
    }
    return (@tab);
}


foreach my $i (1..10){
    printf("%d : %d\n",$i,Fact($i));
}

printf("------------------------------\n");

my @res = Fibo(10);

print("@res\n");
