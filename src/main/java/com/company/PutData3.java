package com.company;

import com.tools.HbaseTools;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class PutData3 {
    private static final int second2Milli = 1000;
    private static String ColumnFamily = "All_position_time";

    public static void putData(HbaseTools hf, String tableName, ArrayList<ArrayList<Object>> objectLists) throws IOException {

        for (ArrayList<Object> list : objectLists){

            // 取得需要存入的時間 (cq: time)
            String time = (String)list.get(1);

            // 取得rowKey (rk: xxx_placecode)
            String placecode = Integer.toString((int)list.get(2));
            String rowKey = getRowKey(placecode, time);

            long stamp_time = getStampTime(time);

            // 取得 person (value: phonenum)
            String phonenum = (String) list.get(0);

            hf.putData(tableName, rowKey, ColumnFamily, time, phonenum);

        }

        System.out.println("======= Finish Put Table 03 ========");

    }

    // xxx_placeCode
    public static String getRowKey(String pos, String time){

        String row_key_arr [] = new String[]{"200", "201", "202", "203", "204", "205", "206", "207", "208"};

        // 取得 int 版本 placecode 之後需要做運算
        int pcode = Integer.parseInt(pos);

        // 取得 int 版本 year-month 之後做運算
        String year_month = time.substring(0, 4) + time.substring(5, 7);
        int t = Integer.parseInt(year_month);

        // 分配 rowkey_xxx
        int remainder = (pcode ^ t) % 9;

        String ans = row_key_arr[remainder] + "_" + pos;

        return ans;
    }

    private static long getStampTime(String time){

        LocalDateTime dateTime = LocalDateTime.parse(time);
        long ts = dateTime.toEpochSecond(ZoneOffset.of("+08:00")) * second2Milli;
        return  ts;

    }


}
