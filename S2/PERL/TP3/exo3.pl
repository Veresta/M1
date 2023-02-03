#!/usr/bin/perl
use strict;
use warnings;
use Data::Dumper;

sub parse {
    my ($file) = @_;
    my $ref = {};
    open (my $fd, '<', $file) or die ("open: $!");
    while( defined( my $ligne = <$fd> ) ) {
        chomp $ligne;
        my @split_line = split(/:/, $ligne);
        $ref->{$split_line[0]} = {"passwd" => $split_line[1],
                                "uid" =>$split_line[2] + 0,
                                "grid" => $split_line[3],
                                "info" => $split_line[4],
                                "home" => $split_line[5],
                                "shell" => $split_line[6],
        };
    }
    close($fd);
    return ($ref);
}

my $ref = parse("passwd.txt");

print Dumper($ref);

sub display1 {
    my ($ref) = @_;
    foreach my $login (keys %$ref){
        print "====================\nUtilisateur : ".$login."\n";
        print "passwd : ". $ref->{$login}->{"passwd"}."\n";
        print "uid : ". $ref->{$login}->{"uid"}."\n";
        print "grid : ". $ref->{$login}->{"grid"}."\n";
        print "info : ". $ref->{$login}->{"info"}."\n";
        print "home : ". $ref->{$login}->{"home"}."\n";
        print "shell : ". $ref->{$login}->{"shell"}."\n";
    }
}

display1($ref);

print "DISPLAY 2***************************************************\n";

sub display2 {
    my ($ref) = @_;
    foreach my $login (sort {$a cmp $b} keys %$ref){
        print "====================\nUtilisateur : ".$login."\n";
        print "passwd : ". $ref->{$login}->{"passwd"}."\n";
        print "uid : ". $ref->{$login}->{"uid"}."\n";
        print "grid : ". $ref->{$login}->{"grid"}."\n";
        print "info : ". $ref->{$login}->{"info"}."\n";
        print "home : ". $ref->{$login}->{"home"}."\n";
        print "shell : ". $ref->{$login}->{"shell"}."\n";
    }
}

display2($ref);

print "DISPLAY 3***************************************************\n";

sub display3{
    my ($ref) = @_;
    foreach my $login (sort {$ref->{$b}->{'uid'} <=> $ref->{$a}->{'uid'}} keys %$ref){
        print "====================\nUtilisateur : ".$login."\n";
        print "passwd : ". $ref->{$login}->{"passwd"}."\n";
        print "uid : ". $ref->{$login}->{"uid"}."\n";
        print "grid : ". $ref->{$login}->{"grid"}."\n";
        print "info : ". $ref->{$login}->{"info"}."\n";
        print "home : ". $ref->{$login}->{"home"}."\n";
        print "shell : ". $ref->{$login}->{"shell"}."\n";
    }
}

display3($ref);