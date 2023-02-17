#!/usr/bin/perl
#fichier exo3.pl
use strict;
use warnings;
use lib '.';
use Disque;
use Data::Dumper;
use Anneau;

my $d = Disque->new(1, 10, 5);

print Dumper $d;

print "$d->surface()\n";

print "$d";

my $a = Anneau->new(10);
print "$a";