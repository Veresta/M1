#!/usr/bin/perl
use strict;
use warnings;
use Data::Dumper;


my $tab = {"Mathis" => {
            "Tel" => "092932930290",
            "Adr" => "rue des boulangers",
            "Enfants" => ["Bernard","Arnault"],
            },
            "Yohann" => {
                "Tel" => "3737370",
                "Adr" => "rue des devs java",
                "Enfants" => [],
            },
            "Johan" => {
                "Tel" => "08767730290",
                "Adr" => "a coté de chez moi",
                "Enfants" => ["Remi","Carine"],
            }
        };

print Dumper($tab);

foreach my $person (keys %$tab){
    print "===================="."\n".$person."\n";
    print "Tel :".$tab->{$person}->{"Tel"}."\n";
    print "Adr :".$tab->{$person}->{"Adr"}."\n";
    #foreach my $enfant (@{$tab->{$person}->{Enfants}}){
    #    print $enfant."\n";
    #}
    print join(", ", @{$tab->{$person}->{Enfants}})."\n";
    print "Nb enfant : ". @{$tab->{$person}->{Enfants}}."\n";
    print "===================="."\n";
}