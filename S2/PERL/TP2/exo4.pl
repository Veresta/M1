#!/usr/bin/perl

use strict;
use warnings;

my $cpt_line = 0;
my $nb_err = 0;
my $nb_octet = 0;

my %acces_url;
my %acces_ip;
my %acces_volume;

open (my $fd, '<', $ARGV[0]) or die ("open: $!");
while( defined( my $ligne = <$fd> ) ) {
    chomp $ligne;
    $cpt_line++;

    if(my ($a, $b, $c, $d) = $ligne =~m/^(.*?) .*?".*? (.*?) .*?" (.*?) (.*?) /){
        print "$a\n $b\n $c\n $d\n";
        if($c != 200){$nb_err++;}
        $nb_octet+= $d;
        $acces_url{$b}++;
        $acces_ip{$a}++;
        $acces_volume{$a} += $d;
    }
    
}

close($fd);

print "Nombres de lignes : $cpt_line\n";
print "Nombres d'erreurs : $nb_err\n";
print "Nombres d'octers transférés : $nb_octet\n";

print "Url dans l'ordre décroissant du nombre d'accès :";
foreach my $key (sort {$acces_url{$b} <=> $acces_url{$a}}  keys %acces_url){
    print "$key => $acces_url{$key}\n";
}

print "10 IP avec le plus d'accès au serveur + volume correspondant :";

foreach my $key (sort {$acces_ip{$b} <=> $acces_ip{$a}} keys %acces_ip){
    print "$key => $acces_ip{$key} => $acces_volume{$key}\n";
}

#Limite le résultat à 10