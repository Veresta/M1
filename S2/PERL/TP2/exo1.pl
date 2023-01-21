#!/usr/bin/perl

use strict;
use warnings;

my %day = (
    "janvier"=>"31",
    "fevrier"=>"28",
    "mars"=> "31",
    "avril"=> "28",
    "mai"=>"31"
    );

delete($day{"fevrier"});

foreach my $month (@ARGV) {
    if(exists ($day{$month})){
        print "$day{$month}\n";
    }else{
        print "inconnue\n";
    }
}