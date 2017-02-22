while [ 1 ]
do
   x=`echo $RANDOM`
   message=`date | cut -d" " -f4`","$x 
   sleep 2
   cd ../bin
   java UDPSend localhost 7002 $message
done
