#!/bin/bash
if [ $# -lt 1 ]
then
 echo "No Args Input..."
 exit ;
fi
case $1 in
"start")
 # start Hbase
 echo " =================== 啟動 Hbase 集群 ==================="
 ssh hbase01 "/opt/module/hbase-2.3.7/bin/start-hbase.sh"
;;
"stop")
 # stop Hbase
 echo " =================== 關閉 Hbase 集群 ==================="
 ssh hbase01 "/opt/module/hbase-2.3.7/bin/stop-hbase.sh"
;;
*)
 echo "Input Args Error..."
;;
esac
