#!/usr/bin/perl
#fichier exo4.pl
use strict;
use warnings;
use lib '.';
use Personne;
use Soiree;
use Data::Dumper;

my $p = new Personne(name => "Mathis", boisson => "Coca");

print Dumper($p);

my $s = new Soiree(capacity => 2);

print Dumper($s);

my $j = new Personne(name => "Johan", boisson => "Eau Evian");
$s->entrer($p);
$s->entrer($j);

print $s->fete();