#!/usr/bin/perl

use strict;
use warnings;
use MIME::Lite;

my $mail = MIME::Lite->new(
    From=>'mathis.menaa@edu.univ-eiffel.fr',
    To=>'mathis.menaa@edu.univ-eiffel.fr',
    Subject=>'Test',
    Type=>'TEXT',
    Data=>'envoie.',
    
);

$mail->attach(
    Type=>'application/pdf',
    Encoding=>'base64',
    Path=>'/home/2inf1/mathis.menaa/Documents/M1/PERL/tp3.pdf',
    Filename=>'tp3.pdf'
);

$mail->send();