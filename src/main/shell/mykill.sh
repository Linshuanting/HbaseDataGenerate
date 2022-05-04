#!/bin/bash

if [ $# -lt 3 ]
then
 echo "No Args Input..."
 exit ;
fi

 echo ================== 清除開始 =================
 ssh hbase01 "kill -9 $1"
 ssh hbase02 "kill -9 $2"
 ssh hbase03 "kill -9 $3"
 echo 清除完成

