#!/usr/bin/perl
use strict;
use warnings;

sub mygrep {
    my ($funRef, @values) = @_;
    my @output;
    foreach my $val (@values){
        if($funRef->($val)){
            @output = (@output, $val);
        }
    }
    return @output;
}

sub positif {
    my ($e) = @_;
    return $e > 0;
}

my @array = mygrep \&positif, (443,34,283,-1);

print join("\n", @array);

print "\n====================\n";

sub mymap {
    my ($funRef, @values) = @_;
    my @output;
    foreach my $val (@values){
        @output = (@output, $funRef->($val));
    }
    return @output;
}

sub double {
    my ($e) = @_;
    return 2*$e;
}

my @array2 = mymap \&double, @array;

print join("\n", @array2)."\n";

print "====================\n";

sub mysort {
    my ($funRef, @values) = @_;
    my @tries;
    for my $i (0 .. $#liste) {
    my $minimum = $i;
    for my $j ($i + 1 .. $#liste) {
      if (&$comparaison($liste[$j], $liste[$minimum]) < 0) {
        $minimum = $j;
      }
    }
    if ($minimum != $i) {
      @liste[$i, $minimum] = @liste[$minimum, $i];
    }
  }

  @triee = @liste;
  return @triee;

    return @output;
}