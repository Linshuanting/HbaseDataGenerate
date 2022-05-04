# HbaseDataGenerate

專題，用來產生COVID-19的亂數資料，來做資料庫的使用

Contributors:   
https://github.com/sShaAanGg   
https://github.com/Linshuanting

---

**Please run the commands below at the root directory (HbaseDataGenerate)**
## Environment
1. **Centos7.9**
2. **Java-1.8.0_202**
3. **Hadoop-3.2.1**
4. **Zookeeper-3.6.3**
5. **Hbase-2.3.7**
6. **maven-3.8.5**

## Compile
```mvn package (or mvn verify)```

## CLASSPATH environment variable
```export CLASSPATH=$HBASE_HOME/lib/*```

## Execute
```java -cp $CLASSPATH:target/HbaseDataGenerate-1.0-SNAPSHOT.jar com.company.DataGenerator```

```java -cp $CLASSPATH:target/HbaseDataGenerate-1.0-SNAPSHOT.jar com.company.MapGenerator```

```java -cp $CLASSPATH:target/HbaseDataGenerate-1.0-SNAPSHOT.jar com.company.PeopleGenerator```

------
## Introduction

地圖大小(size)為1000*1000，有100000個人走訪地圖。     
將100000個人依照下方 living pattern 來走訪，並將生成的資料放置於Hbase中

## Result of our experiment
![](./assets/Result.png)
---
```
/* Definiton of living pattern
    1 : 白天活動，活動路線固定
    3 : 白天活動，路線不固定
    2 : 晚上活動，路線固定
    3 : 晚上活動，路線不固定
    4 : 白天夜晚都會活動，路線固定
    5 : 白天夜晚都會活動，路線不固定
    6 : 活動時間與路徑相對隨機
    
    更改成
    1 : 早八晚五型，目標相同
    2 : 早八晚五型，目標不同
    3 : 早八晚五型，無活動
    4 : 早八午十二，單目標
    5 : 午十二晚五，單目標
    6 : 活動時間與路徑相對隨機 
*/
```
---

### Pattern 01 Table Design

#### Table01
**row_key**: (String) phonenum  
**columnFamily**: pos   
**columnQualifier**: (long)positionCode 
**value**: (int)placeCode   

#### Table02
**row_key**: (int)placeCode 
**columnFamily**: pho   
**columnQualifier**: (String)phonenum   
**value**: (long)positionCode   

### Pattern 02 Table Design

#### Table03
**row_key**: (String)xxx_phonenum   
**columnFamily**: All_of_the_time   
**columnQualifier**: (String)time   
**value**: (String)placeCode  

#### Table04
**row_key**: (String)xxx_placecode_time   
**columnFamily**: People   
**columnQualifier**: (String)phonenum   
**value**: null

### Pattern 03 Table Design

#### Table03
**row_key**: (String)xxx_phonenum   
**columnFamily**: All_of_the_time   
**columnQualifier**: (String)time   
**value**: (String)placeCode

#### Table05
**row_key**: (String)xxx_placecode   
**columnFamily**: All_position_time   
**columnQualifier**: (String)time   
**value**: phonenum