while [ 1 ]
do
  message=`date | cut -d" " -f2,3,4`,`top -b -n1 | grep "Cpu(s)" | awk '{print $2 + $4}'` 
  message=`echo $message | sed 's/ /_/g'`
  echo $message
  cd ../bin
  java UDPSend localhost 7001 $message
  sleep 15
done
