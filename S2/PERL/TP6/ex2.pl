#!/usr/bin/perl

use strict;
use warnings;
use DBI;

my $source = 'dbi:Pg:host=sqletud.u-pem.fr;dbname=mathis.menaa_db';
my $user = "mathis.menaa";
my $passwd = "34Xreuceyb@";
my $base = DBI->connect($source, $user, $passwd) or die($DBI::errstr);

my $createTableSQL = 
    'CREATE TABLE annuaire (
        prenom_nom VARCHAR(40),
        numero_tel VARCHAR(20)
    );';

my $req = $base->prepare($createTableSQL) or die($base->errstr());

$req->execute() or die($base->errstr());

my $insertSQL = 'INSERT INTO annuaire(prenom_nom, numero_tel) VALUES(?,?);';

$req = $base->prepare($insertSQL) or die($base->errstr());

$req->execute("Mathis MENAA", "065145574768") or die($base->errstr());
$req->execute("Johan RAMAROSON", "0669454368") or die($base->errstr());
$req->execute("Yohann REGUEME", "065142564") or die($base->errstr());
$req->execute("REMI JR", "07454553468") or die($base->errstr());

my $showSQL = 'SELECT * FROM annuaire;';

$req = $base->prepare($showSQL) or die($base->errstr());

$req->execute() or die($base->errstr());

while( my $reft = $req->fetchrow_arrayref()) {
    print "@$reft\n";
}

$base->disconnect();