#!/usr/bin/perl

use strict;
use warnings;
use MIME::Parser;
use MIME::Base64;

my $parser = MIME::Parser->new();

my $mime = $parser->parse_open("/home/2inf1/mathis.menaa/Documents/M1/PERL/TP5/courriel");

print $mime->get('From')."\n";
print $mime->get('Date')."\n";

my $subject = $mime->get('Subject')."\n";

$subject =~ s/=\?utf-8\?b\?(.*?)\?=/decode_base64($1)/ieg;

print "$subject\n";