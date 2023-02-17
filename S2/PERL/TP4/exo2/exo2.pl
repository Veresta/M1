#!/usr/bin/perl
#fichier exo2.pl
use strict;
use warnings;
use lib '.';
use MonModule;


my $res = TableMul1($ARGV[0] // 4); # // valeur par défault

print "$res";