#!/bin/bash
while [ 1 ]
do
sleep 5
count=`cat ../chart.properties | grep chart.count | cut -d= -f2`
((count--));
for i in $(eval echo "{0..$count}")
do
#   echo "Processing $i"
   #Processing each chart now.
   rawFile=`cat ../chart.properties | grep chart.${i}.rawdata | cut -d= -f2`
   maxData=`cat ../chart.properties | grep chart.${i}.maxdata | cut -d= -f2`
   `tail -${maxData} ${rawFile} | grep -v T | grep -vi n | grep -v ,$  > ${rawFile}_xtrct`
done
done
