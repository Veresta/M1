package MonModule;
use strict;
use warnings;
use parent qw(Exporter);
our @EXPORT = qw(TableMul1 TableMul2 TableMul3);

sub TableMul1 {
    my ($n) = @_;
    for(my $i = 1; $i < $n+1; $i++){
        for(my $j = 1; $j < $n+1; $j++){
            printf('%5d', $i*$j);
        }
        printf("\n");
    }
}


sub TableMul2 {
    my ($n) = @_;
    foreach my $i (1..$n) {
        foreach my $j (1..$n){
            printf('%5d', $i*$j);
        }
        printf("\n");
    }
}

sub TableMul3 {
    my ($n) = @_;
    my $chaine = '';
    foreach my $i (1..$n) {
        foreach my $j (1..$n){
           $chaine = sprintf("$chaine%5d", $i*$j);
        }
        $chaine= sprintf("$chaine\n");
    }
    return $chaine;
}

1;