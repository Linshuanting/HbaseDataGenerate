# HbaseDataGenerate

專題，用來產生COVID-19的亂數資料，來做資料庫的使用

------
The class **BlockData in datamap.java** is moved into **DataGenerator.java** and The class **Person in datamap.java** is moved into **People.java**.

地圖生成的程式碼基本上都被移到 **MapGenerator.java** 裡，MapGenerator 會將地圖資料生成至某路徑下(預設 "./test/map/map.xlsx)，可以到test/map/下載map.xlsx。而DataGenerator就只是到map.xlsx裡讀取地圖，隨機生成一些人隨機拜訪某些場所生成資料。

接下來我想的到的地圖作法是手動標記，標記一個2維陣列中有場所代碼的點。再來就是將人依據生活模式分類以生成合理資料。

~~TODO: 接下來可能可以寫個~~ 現在 **PeopleGenerator.java** 生成固定的居民到excel裡面同時把class **Person**移進去，**TODO:** 不同的人可以有不同的移動、生活模式，除此之外還得考慮時間像是白天活動或是晚上活動等等。這樣地圖和人都確定下來，生成資料應該會比較容易。

---
**test/data/data_0.xlsx is the sample data generated from DataGenerator.java**

地圖改為100*100，有1000個人走訪地圖。人與地圖資訊都是從test資料夾下的excel讀取而來。

---
![](/../patch-1/assets/DefinitionOfLivingPattern.jpg)

這只是我初步的想法，**有甚麼其他想法就寫在下面吧**，數量和內容都能改
```
/* Definiton of living pattern
    1 : 白天活動，活動路線固定
    3 : 白天活動，路線不固定
    2 : 晚上活動，路線固定
    3 : 晚上活動，路線不固定
    4 : 白天夜晚都會活動，路線固定
    5 : 白天夜晚都會活動，路線不固定
    6 : 活動時間與路徑相對隨機
*/
```
----
<<<<<<< HEAD
[Google Map API](https://developers.google.com/maps/gmp-get-started?hl=zh-tw)
90天免費之後疑似要收費?

https://developers.google.com/maps/documentation/geocoding/overview?hl=zh-tw

