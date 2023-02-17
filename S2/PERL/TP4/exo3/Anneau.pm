package Anneau;
use strict;
use warnings;
use parent qw(Disque);
use Math::Trig ':pi';

sub new {
    my ($class, $X, $Y, $R, $RI) = @_;
    my $this = $class->SUPER::new($X,$Y,$R);
    $this->{RI} = $RI // 0;
    return bless($this, $class);
}

sub surface {
    my ($this) = @_;
    my $di = $this->{RI} * $this->{RI} * pi;
    return $this->SUPER::surface() - $di;
}

sub dump{
    my ($this) = @_;
    return $this->SUPER::dump();
}
1;