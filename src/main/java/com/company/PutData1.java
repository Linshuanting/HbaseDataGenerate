package com.company;

// Schema of 'table1'
// | 1st | phone number | position(VERSIONS => 100) | position code | location |
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

// import org.apache.hadoop.hbase.TableName;
// import org.apache.hadoop.hbase.MasterNotRunningException;
import com.tools.HbaseTools;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.xssf.streaming.SXSSFWorkbook;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PutData1 {
    private static final int second2Milli = 1000;
    private static String ColumnFamily = "All_of_the_time";



    // 將資料放入 hbaseTable01
    public static void putData(HbaseTools hf, String tableName, ArrayList<ArrayList<Object>> objectLists) throws IOException {

        for (ArrayList<Object> list : objectLists){

            // 取得rowKey (rk: xxx_phonenum)
            String rowKey = getRowKey((String)list.get(0));

            // 取得需要存入的時間 (cq: time)
            String time = (String) list.get(1);
            long stamp_time = getStampTime(time);

            // 取得placeCode (value: placeCode)
            String pc = Integer.toString((int) list.get(2));

            hf.putData(tableName, rowKey, ColumnFamily, time, stamp_time, pc);

        }

        System.out.println("======= Finish Put Table 01 ========");

    }

    // 設置 xxx_phone_num 的 row_key
    private static String getRowKey(String phoneNum){

        String row_key_arr [] = new String[]{"000", "001", "002", "003", "004", "005", "006", "007", "008"};

        // 計算需要的xxx
        int remainder = Integer.parseInt(phoneNum) % 9;

        return row_key_arr[remainder] + "_" + phoneNum;

    }

    // 設定需要放入timestamp中的時間
    private static long getStampTime(String time){

        LocalDateTime dateTime = LocalDateTime.parse(time);
        long ts = dateTime.toEpochSecond(ZoneOffset.of("+08:00")) * second2Milli;
        return  ts;

    }


    public static void putData(Connection connection, Table table, ArrayList<ArrayList<Object>> objectLists)
            throws IOException {
        ArrayList<Put> puts = new ArrayList<>(objectLists.size());
        LocalDateTime dateTime;
        String time;
        for (ArrayList<Object> list : objectLists) {
            time = (String) list.get(1);
            dateTime = LocalDateTime.parse(time);
            long ts = dateTime.toEpochSecond(ZoneOffset.of("+08:00")) * second2Milli;

            Put put = new Put(Bytes.toBytes((String) list.get(0)));
            put.addColumn(Bytes.toBytes("pos"), Bytes.toBytes((long) list.get(3)), ts,
                    Bytes.toBytes((int) list.get(2)));
            puts.add(put);

            put = null;
        }

        // for (int i = 0; i < objectLists.size(); i++) {
        // Put put = new Put(Bytes.toBytes(phoneNums.get(i)));
        // put.addColumn(Bytes.toBytes("pos"), Bytes.toBytes(positionCodes.get(i)),
        // ts.get(i),
        // Bytes.toBytes(placeCodes.get(i)));
        // puts.add(put);
        // put = null;
        // }

        table.put(puts);
        System.out.println("Data was inserted Successfully");
    }

}
