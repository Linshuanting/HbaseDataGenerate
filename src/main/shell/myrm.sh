#!/bin/bash

if [ $# -lt 1 ]
then
 echo "No Args Input..."
 exit ;
fi


pdir=$(cd -P $(dirname $1); pwd)
if [ -e $pdir/$1 ]
 then
   echo "do nothing"  
 else 
   echo $pdir/$1 does not exist!
   exit
fi

for host in hbase01 hbase02 hbase03
do
 echo ================== $host =================
 for file in $@
 do
    #5. 獲取父目錄
    pdir=$(cd -P $(dirname $file); pwd)
    #6. 獲取當前文件的名稱
    fname=$(basename $file)
    ssh $host "rm -rf $pdir/$fname"
 done 
 
 echo 刪除完成
done
