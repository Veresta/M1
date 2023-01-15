#!/usr/bin/perl
use strict;
use warnings;

sub Modif1 {
    my ($texte, $ancien, $nouveau) = @_;
    $texte =~ s/$ancien/$nouveau/g;
    return ($texte);
}

sub Modif2 {
    my ($texte, $ancien, $nouveau) = @_;
    my $res = "";
    my $tmp = -1;
    while(index($texte,$ancien) != -1){
        my $ind = index($texte,$ancien);
        substr($texte, $ind, length($ancien), $nouveau)
    }
    return ($texte);
}

my $res = Modif2('bonjour vous, bonjour', 'bonjour', 'bonjour bonjour');

print $res . "\n";