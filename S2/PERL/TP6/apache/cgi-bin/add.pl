#!/usr/bin/perl

use strict;
use warnings;
use CGI qw/:standard/;
use DBI;

print "Content-Type: text/html\n\n";

my $query = CGI->new();
my $field1 = $query->param('PrenomNom');
my $field2 = $query->param('Telephone');

if(!defined($field1) && !defined($field2)) {
    print header,
    start_html('Titre'),
    start_form(-method=>'GET', -action=>'http://localhost:8080/cgi-bin/add.pl'),
    'PrenomNom : ', textmy $user = "mathis.menaa";
my $passwd = "123456";
my $base = DBI->connect($source, $user, $passwd) or die($DBI::errstr);

my $req = $base->prepare('INSERT INTO annuaire(prenom_nom, numero_tel) VALUES(?,?);') or die($base->errstr());
$req->execute($field1, $field2) or die($base->errstr());

print "<h1>Ajout de $field1 , $field2\n</h1>";

my $showSQL = 'SELECT * FROM annuaire;';

$req = $base->prepare($showSQL) or die($base->errstr());

$req->execute() or die($base->errstr());

while( my $reft = $req->fetchrow_arrayref()) {
    print "<p> @$reft\n </p>";
}

$base->disconnect();
field('PrenomNom'), br,
    'Telephone : ', textfield('Telephone'), br,
    br, submit('Envoyer'), br, reset('Annuler'),
    end_form, end_html;
}else{
    my $source = 'dbi:Pg:host=sqletud.u-pem.fr;dbname=mathis.menaa_db';
    my $user = "mathis.menaa";
    my $passwd = "123456";
    my $base = DBI->connect($source, $user, $passwd) or die($DBI::errstr);

    my $req = $base->prepare('INSERT INTO annuaire(prenom_nom, numero_tel) VALUES(?,?);') or die($base->errstr());
    $req->execute($field1, $field2) or die($base->errstr());

    print "<h1>Ajout de $field1 , $field2\n</h1>";

    my $showSQL = 'SELECT * FROM annuaire;';

    $req = $base->prepare($showSQL) or die($base->errstr());

    $req->execute() or die($base->errstr());

    while( my $reft = $req->fetchrow_arrayref()) {
        print "<p> @$reft\n </p>";
    }

    $base->disconnect();
}



