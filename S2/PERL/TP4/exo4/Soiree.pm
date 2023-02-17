package Soiree;
use Moose;
use strict;
use warnings;

has capacity => (is=>'ro' , isa=>'Int', required=>1);
#has potes => (is=>'rw', isa=>'ArrayRef[Personne]', default=>sub{[]}, auto_deref=>1, traits=>['Array'], handles => {
#                entrer => 'push',
#                expulser => 'pop',
#                nbPotes => 'count',
#});

has potes => (is=>'rw', isa=>'ArrayRef[Fetard]', default=>sub{[]}, auto_deref=>1, traits=>['Array'], handles => {
                entrer => 'push',
                expulser => 'pop',
                nbPotes => 'count',
});


sub fete {
    my ($this) = @_;
    foreach my $potes ($this->potes()){
        print $potes->name()."\n";
        $potes->boire();
        $potes->delirer();
    }
}

before entrer => sub {
    my ($this, @args) = @_;
    print $args[0]->{name} ." entre dans la fiesta.\n";
};

after entrer => sub {
    my ($this, @args) = @_;
    if($this->capacity < $this->nbPotes){
        my $p = $this->expulser();
        print $p->name." à été TEJ de la fiesta.\n";
    }else{
        print "BIENVENUE ".$args[0]->{name} ."\n";
    }
};

1;