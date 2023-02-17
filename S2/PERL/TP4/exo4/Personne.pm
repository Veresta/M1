package Personne;
use Moose;
use strict;
use warnings;

with 'Fetard';

has name => (is=>'ro', isa=>'Str');

sub delirer {
    my ($this) = @_;
    print $this->name." DELIRE DE FOU ZINZIN!!!\n";
}

1;