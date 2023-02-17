package Fetard;
use Moose::Role;
use strict;
use warnings;

has boisson => (is=>'ro', isa=>'Str', required=>1);

sub boire {
    my ($this) = @_;
    print "Bois du ".$this->boisson."\n";
}

requires 'delirer';

1;