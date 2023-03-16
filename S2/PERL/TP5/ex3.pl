#!/usr/bin/perl

use strict;
use warnings;
use IO::Socket;
use threads;
use threads::shared;

my $cpt : shared = 1;

my $listen_socket = IO::Socket::INET->new(
    Proto=>'tcp', LocalPort=>2000, Listen=>5, Reuse=>1
    ) or die("$@");

sub sendingFun {
    my ($socketAdress) = @_;
    $socketAdress->send($cpt++."\n");
    sleep(5);
    $socketAdress->send($cpt++."\n");
    close ($socketAdress);
}

while(my $accept_socket = $ listen_socket->accept()) {
    print "New client\n";
    threads->new(\&sendingFun, $accept_socket)->detach();
}
