#!/bin/bash

targetFile=$HBASE_HOME/HBaseDMSource/

javac -d $targetFile/target/ -cp $targetFile:$CLASSPATH $targetFile/src/com/*/*.java

java -cp $CLASSPATH:$targetFile:$targetFile/target/DataGet.jar com/data/DataGet 
