#!/usr/bin/perl
use IO::Socket::INET; 
$receiver = $ARGV[0];
$maxCount = $ARGV[1];
$sleep = $ARGV[2];
open FILE, ">", "sendLog.log" or die $!;
print FILE "sending message to ".$receiver."\n";
my $sock = new IO::Socket::INET(PeerAddr => $receiver,
		                 PeerPort => 6666,
		                 Proto => 'udp', Timeout => 1) or die('Error opening socket.'); 
for (my $i=0; $i < $maxCount; $i++) {
   my $data = "10:12:12,12";
   print $sock $data;
   print FILE "sent ".$data."\n";
   select(undef, undef, undef, $sleep);
}

