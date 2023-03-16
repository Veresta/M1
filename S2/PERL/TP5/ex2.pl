#!/usr/bin/perl

use strict;
use warnings;
use POSIX qw(strftime);


my $folder = $ENV{HOME};

my ($lastMod) = ((stat($folder))[9]);

print "$lastMod\n";

my $formatedDate = strftime('%A, %d %B %Y', localtime($lastMod));

print "$formatedDate\n";