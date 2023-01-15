#!/usr/bin/perl
use strict;
use warnings;

sub SommeTest {
    my ($x, $y, $z) = @_;
    if("$x"."$y" eq "$z"){
        return (1);
    }
    return (0);
}

if(SommeTest(1,2,12)){
    printf("MARCHE\n");
}

