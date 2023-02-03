#!/usr/bin/perl
use strict;
use warnings;

my @entries = glob('~/.*');

print join("\n",@entries)."\n"."========================"."\n";

@entries = grep {!(-x $_)} @entries;

print join("\n",@entries)."\n"."========================"."\n";


@entries = sort{(-s $a) <=> (-s $b)} @entries;

print join("\n",@entries)."\n"."========================"."\n";

my @sizes = map{-s $_} @entries;

print join("\n",@sizes)."\n"."========================"."\n";


my %hash;

foreach my $value (@entries){
    $hash{$value} = -s $value;
}

print join("\n",%hash)."\n"."========================"."\n";
