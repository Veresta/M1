#!/usr/bin/perl
use strict;
use warnings;

sub Intervalle {
    my ($n, $x) = @_;
    return (1..$x-1, $x+1..$n);
}

my @res = Intervalle(10,4);

print "@res" ."\n";

sub NonMult {
    my ($n, $x) = @_;
    my @res;
    foreach my $i (1..$n){
        push(@res, $i) if $i % $x != 0;
    }
    return @res;
}

my @res2 = NonMult(10,2);

print "@res2" ."\n";