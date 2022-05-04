#!/bin/bash

#1. 判斷參數個數
if [ $# -lt 1 ]
then
 echo Not Enough Arguement!
 exit;
fi

#2. 遍歷集群所有機器
for host in hbase01 hbase02 hbase03
do
 echo ==================== $host ====================
 #3. 遍歷所有目錄，挨个發送
 for file in $@
 do
  #4. 判段文件是否存在
  if [ -e $file ]
   then
    #5. 獲取父目錄
    pdir=$(cd -P $(dirname $file); pwd)
    #6. 獲取當前文件的名稱
    fname=$(basename $file)
    ssh $host "mkdir -p $pdir"
    rsync -av $pdir/$fname $host:$pdir
 else
    echo $file does not exists!
 fi
 done
done
