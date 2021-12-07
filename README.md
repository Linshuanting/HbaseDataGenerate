# HbaseDataGenerate
專題，用來產生COVID-19的亂數資料，來做資料庫的使用

------
The class **BlockData in datamap.java** is moved into **DataGenerator.java**

地圖生成的程式碼基本上都被移到 **MapGenerator.java** 裡，MapGenerator 會將地圖資料生成至某路徑下(預設 "./test/map/map.xlsx)，可以到test/map/下載map.xlsx。而DataGenerator就只是到map.xlsx裡讀取地圖，隨機生成一些人隨機拜訪某些場所生成資料。

~~TODO: 接下來可能可以寫個~~ **PeopleGenerator.java** 生成固定的居民到excel裡面同時把class **Person**移進去，**TODO:** 不同的人可以有不同的移動、生活模式，除此之外還得考慮時間像是白天活動或是晚上活動等等。這樣地圖和人都確定下來，生成資料應該會比較容易。

----
