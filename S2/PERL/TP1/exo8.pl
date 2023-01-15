#!/usr/bin/perl
use strict;
use warnings;

sub Eratosthene {
    my ($n) = @_;
    my @tab = (2..$n);
    my @res;
    while(@tab != 0){
        my $tmp = shift(@tab);
        @tab = grep{$_ % $tmp != 0} @tab;
        push(@res, $tmp);
    }
    return @res;
}

my @res = Eratosthene(10);

print "@res"."\n";