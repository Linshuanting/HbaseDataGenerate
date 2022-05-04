#!/bin/bash

echo "disable 'scale1'" | hbase shell -n
echo "disable 'scale2'" | hbase shell -n
echo "drop 'scale1'" | hbase shell -n
echo "drop 'scale2'" | hbase shell -n

echo "create 'scale1', {NAME => 'pos', BLOOMFILTER => 'ROW', IN_MEMORY => 'false', VERSIONS => '100'}" | hbase shell -n
echo "create 'scale2', {NAME => 'pho', BLOOMFILTER => 'ROW', IN_MEMORY => 'false', VERSIONS => '100'}" | hbase shell -n

status=$?
echo "The status was " $status
