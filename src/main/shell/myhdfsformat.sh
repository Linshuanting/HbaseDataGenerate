#!/bin/bash


 echo ================== 清除開始 =================
 ssh hbase01 "rm -rf /opt/module/hadoop-3.2.1/data" 
 ssh hbase01 "rm -rf /opt/module/hadoop-3.2.1/logs"
 ssh hbase02 "rm -rf /opt/module/hadoop-3.2.1/data" 
 ssh hbase03 "rm -rf /opt/module/hadoop-3.2.1/data" 
 ssh hbase02 "rm -rf /opt/module/hadoop-3.2.1/logs"
 ssh hbase03 "rm -rf /opt/module/hadoop-3.2.1/logs"
 ssh hbase01 "hdfs namenode -format"
 ssh hbase02 "hdfs namenode -format"
 ssh hbase03 "hdfs namenode -format"
 echo 清除完成

