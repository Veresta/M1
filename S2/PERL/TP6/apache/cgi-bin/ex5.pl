#!/usr/bin/perl

use strict;
use warnings;
use CGI qw/:standard/;

#print header,
#    start_html('Titre'),
#    start_form(-method=>'GET', -action=>'http://localhost:8080/formulaire.html'),
#    'Pr&eacute; Champs : ', textfield('champs'), br,
#    br, submit('Envoyer'), br, reset('Annuler'),
#    end_form, end_html;

my $query = CGI->new();
my $entry = $query->param('Field');

print "Content-Type: text/html\n\n";
print "THE FIELD IS : $entry\n";
