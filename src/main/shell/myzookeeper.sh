#!/bin/bash
if [ $# -lt 1 ]
then
 echo "No Args Input..."
 exit ;
fi
case $1 in
"start")
 echo " =================== 啟動 zookeeper 集群 ==================="
 echo " --------------- 啟動 hbase01 zookeeper ---------------"
 ssh hbase01 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh start"
 echo " --------------- 啟動 hbase02 zookeeper ---------------"
 ssh hbase02 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh start"
 echo " --------------- 啟動 hbase03 zookeeper ---------------"
 ssh hbase03 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh start"
;;
"stop")
 echo " =================== 關閉 zookeeper 集群 ==================="
 echo " --------------- 關閉 hbase01 zookeeper ---------------"
 ssh hbase01 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh stop"
 echo " --------------- 關閉 hbase02 zookeeper ---------------"
 ssh hbase02 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh stop"
 echo " --------------- 關閉 hbase03 zookeeper ---------------"
 ssh hbase03 "/opt/module/zookeeper-3.6.3/bin/zkServer.sh stop"
;;
*)
 echo "Input Args Error..."
;;
esac
