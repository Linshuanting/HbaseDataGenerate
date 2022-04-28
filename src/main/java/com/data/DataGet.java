package com.data;

import com.data.getData1;
import com.data.getData2;
import com.data.getData3;
import com.tools.FileOperator;
import com.tools.HbaseTools;
import com.tools.cq_value;
import com.tools.timer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import java.io.IOException;

 /**
 * 此程式為運行 hbase table3, table4, table5 的程式
 * table3 格式為row_key: xxx_phonenum
 *   columnFamily: All_of_the_time
 *   columnQualifier: 2020-04-30T00:00:00(time)
 *   value: placeCode
 * table4 格式為
 *   row_key: rk: xxx_placecode_time
 *   columnFamily: People
 *   columnQualifier: phonenum
 *   value: x
 * table5 格式為
 *   row_key: rk: xxx_placecode_time
 *   columnFamily: All_position_time
 *   columnQualifier: 2020-04-30T00:00:00(time)
 *   value: phonenum
 * 1. 透過 table3, table4，可以得到一筆潛在確診者清單
 * 2. 透過 table3, table5，也可以得到一筆潛在確診者清單
 * 而此程式最終會輸出：運行此兩者所需要的時間，來比較資料庫設計運行差異
 * 運行此程式最後得到的潛在確診者清單，會存入txt中，而檔名需要自己設定*
  *
  * @author Lin Hsuan-Ting
 */

public class DataGet {

    // 新增需要連接到Hbase的設定
    private static Configuration configuration = null;
    private static HbaseTools HF;

    static {

        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "140.115.52.28");
        configuration.set("zookeeper.znode.parent", "/hbase");
        HF = new HbaseTools();
        HF.setDataConfig(configuration);

    }

    public static void main(String[] args) throws IOException {
        
        String[][] data01, data02;
        // 創建計時器
        timer timer = new timer();
        // 創建寫檔 object
        FileOperator fileOperator = new FileOperator();

        // 開始 table34 的讀取資料，並啟動計時器
        // 需要選擇一個確診的人，並匡列出此人行動的時間
        // 得到的 data 即為可能確診接觸人
        // 時間需要依照該格式：2020-04-30T00:00:00
        // minTime, maxTime 為開始的時間以及結束的時間
        timer.startTimer();
        data01 = getDataByTable34("0912545411",
                            "2020-04-1T00:00:00",
                            "2020-04-30T00:00:00");
        timer.stopTimer();
        System.out.println("data01 run time: " + timer.getRunTime());

        fileOperator.writeTotxt("data01", data01);

        // 開始 table35 的讀取資料，並啟動計時器
        timer.startTimer();
        data02 = getDataByTable35("0912545411",
                "2020-04-1T00:00:00",
                "2020-04-30T00:00:00");
        timer.stopTimer();
        System.out.println("data01 run time: " + timer.getRunTime());

        fileOperator.writeTotxt("data02", data02);

    }


    // 取得 table3 & table4 合作的資料
    // phonenum 為電話號碼 09xxxxxxxx
    // minTime、maxTime 為需要資料的範圍時間，格式為: 2022-04-30T00:00:00
    public static String[][] getDataByTable34(String phonenum, String minTime, String maxTime) throws IOException {

        getData1 getData1 = new getData1();
        getData2 getData2 = new getData2();

        cq_value datas = getData1.getData(HF, phonenum, minTime, maxTime);

        // time
        String [] columnQualifiers = datas.getCq();
        // placeCode
        String [] values = datas.getValue();

        String[][] ans = new String[datas.getLength()][];

        for (int i = 0; i < datas.getLength(); i++) {

             ans[i] = getData2.getData(HF, columnQualifiers[i], values[i]);

        }

        //printArr(ans);
        return ans;
    }

    // 取得 table3 & table5 合作的資料
    // phonenum 為電話號碼 09xxxxxxxx
    // minTime、maxTime 為需要資料的範圍時間，格式為: 2022-04-30T00:00:00
    public static String[][] getDataByTable35(String phonenum, String minTime, String maxTime) throws IOException{

        getData1 getData1 = new getData1();
        getData3 getData3 = new getData3();

        cq_value datas = getData1.getData(HF, phonenum, minTime, maxTime);

        // time
        String [] columnQualifiers = datas.getCq();
        // placeCode
        String [] values = datas.getValue();

        String[][] ans = new String[datas.getLength()][];

        for (int i = 0; i < datas.getLength(); i++) {

            ans[i] = getData3.getData(HF, columnQualifiers[i], values[i]);

        }

        //printArr(ans);
        return ans;

    }




    public static void printArr(String [][] arrs){

        for (String[] arr: arrs) {
            for (String item: arr) {
                System.out.println(item);
            }
        }
    }
    
}
