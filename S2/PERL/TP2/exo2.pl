#!/usr/bin/perl

use strict;
use warnings;

my %uid;

open (my $fd, '<', $ARGV[0]) or die ("open: $!");
while( defined( my $ligne = <$fd> ) ) {
    chomp $ligne;
    my @split_line = split(/:/, $ligne);
    $uid {$split_line[0]} = $split_line[2];
}

foreach my $login (keys %uid) {
    print "login : $login | uid : $uid{$login}\n";
}
close($fd);

#Trie avec sort

print "===TRIE Login===\n";

my %sorted_login_uid;

foreach my $login (sort keys %uid){
    $sorted_login_uid {$login} = $uid{$login};
    print "login : $login | uid : $sorted_login_uid{$login}\n";
}

print "===TRIE value===\n";


foreach my $login (sort {$uid{$a} <=> $uid{$b}} keys %uid){
    print "login : $login | uid : $uid{$login}\n";
}

#Affinage du tri
#