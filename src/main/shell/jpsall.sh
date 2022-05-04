#!/bin/bash
for host in hbase01 hbase02 hbase03
do
 echo =============== $host ===============
 ssh $host jps 
done
