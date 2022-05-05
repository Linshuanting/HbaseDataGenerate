#!/bin/bash
if [ $# -lt 1 ]
then
 echo "No Args Input..."
 exit ;
fi
case $1 in
"start")
 echo " =================== 啟動 hadoop 集群 ==================="
 echo " --------------- 啟動 hdfs ---------------"
 ssh hbase01 "/opt/module/hadoop-3.2.1/sbin/start-dfs.sh"
 echo " --------------- 啟動 yarn ---------------"
 ssh hbase02 "/opt/module/hadoop-3.2.1/sbin/start-yarn.sh"
 echo " --------------- 啟動 historyserver ---------------"
 ssh hbase01 "/opt/module/hadoop-3.2.1/bin/mapred --daemon start historyserver"
;;
"stop")
 echo " =================== 關閉 hadoop 集群 ==================="
 echo " --------------- 關閉 historyserver ---------------"
 ssh hbase01 "/opt/module/hadoop-3.2.1/bin/mapred --daemon stop historyserver"
 echo " --------------- 關閉 yarn ---------------"
 ssh hbase02 "/opt/module/hadoop-3.2.1/sbin/stop-yarn.sh"
 echo " --------------- 關閉 hdfs ---------------"
 ssh hbase01 "/opt/module/hadoop-3.2.1/sbin/stop-dfs.sh"
;;
*)
 echo "Input Args Error..."
;;
esac
