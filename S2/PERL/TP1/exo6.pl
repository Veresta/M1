#!/usr/bin/perl
use strict;
use warnings;


my @tab = (4, -5, 7);
@tab = (@tab, -2, 3);

print join(',', @tab)."\n";

@tab = ((0, -1),@tab);

$tab[3] = 9;

@tab = map{$_ * 2} @tab;

@tab = grep{$_ > 0} @tab;

@tab = sort{$a < $b} @tab;

print "@tab\n";