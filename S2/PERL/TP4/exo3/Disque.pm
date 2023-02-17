package Disque;
use strict;
use warnings;
use Math::Trig ':pi';
use overload '""' => \&dump;

sub new {
    my ($class, $X, $Y, $R) = @_;
    my $this = {};
    $this->{X} = $X // 0;
    $this->{Y} = $Y // 0;
    $this->{R} = $R // 1;
    bless($this, $class);
    return $this;
}

sub surface {
    my ($this) = @_;
    return $this->{R} * $this->{R} * pi;
}

sub dump{
    my ($this) = @_;
    return ref($this).": $this->{X},$this->{Y},$this->{R}\n"
}
1;