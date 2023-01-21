#!/usr/bin/perl

use strict;
use warnings;

#Lit le fichier en argument

my @text;

open (my $fd, '<', $ARGV[0]) or die ("open: $!");
while( defined( my $ligne = <$fd> ) ) {
    chomp $ligne;
    #if(my ($val) = $ligne =~ m/^jc:.+/){
    #    print "$ligne\n";
    #}
     
    #if(my ($val) = $ligne !~ m@/bash$@){
    #    print "$ligne\n";
    #}

    #$ligne =~s@/home@/mnt/home/@g;
    #print "$ligne\n";

    #$ligne =~ s/:.*?:/:/;
    #print "$ligne\n";

    #$ligne =~ s/^(.*?):(.*?):/$2:$1:/;
    #print "$ligne\n";
}
close($fd);

