# HbaseDataGenerate
專題，用來產生COVID-19的亂數資料

------
The class BlockData in datamap.java is moved into DataGenerator.java

地圖生成的程式碼基本上都被移到 DataGenerator.java 裡，DataGenerator 會將地圖資料生成至某路徑下(預設 "./test/map/map.xlsx)，可以到test/map/下載map.xlsx。

接下來可能可以寫個 PeopleGenerator.java 生成固定的居民到excel裡面，不同的人可以有不同的移動、生活模式。
