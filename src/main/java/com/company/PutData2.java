package com.company;

// Schema of 'table2'
// | 2nd | location | phone_numbers(VERSIONS => 100) | phone number | position code |
import java.io.*;
import java.time.LocalDate;
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

public class PutData2 {
    private static final int second2Milli = 1000;
    private static String ColumnFamily = "People";

    public static void putData(HbaseTools hf, String tableName, ArrayList<ArrayList<Object>> objectLists) throws IOException {

        for (ArrayList<Object> list : objectLists){

            // 取得rowKey (rk: xxx_placecode_time)
            String time = (String)list.get(1);
            String placecode = Integer.toString((int)list.get(2));
            String rowKey = getRowKey(placecode, time);


            // 取得 phonenum 當作 column qualifier (cq: phonenum)
            String phonenum = (String) list.get(0);


            long stamp_time = getStampTime(time);

            // 取得 person (value: null)
            String value = "";

            hf.putData(tableName, rowKey, ColumnFamily, phonenum, stamp_time,value);

        }

        System.out.println("======= Finish Put Table 02 ========");

    }

    // 測試用
    public static void main(String[] args) {
        LocalDate d = LocalDate.now();

        String time = DataGenerator.getTime(d,10,5);
        String time2 = "2022-04-30T00:00:00";
        long ts = getStampTime(time);
        long ts2 = getStampTime(time2);

        System.out.println(time);
        System.out.println(ts);
        System.out.println(time2);
        System.out.println(ts2);

    }

    // 取得要求rowkey: xxx_placeCode_time
    public static String getRowKey(String pos, String time){

        String row_key_arr [] = new String[]{"100", "101", "102", "103", "104", "105", "106", "107", "108"};

        // 取得 int 版本 placecode 之後需要做運算
        int pcode = Integer.parseInt(pos);

        // 取得 int 版本 year-month 之後做運算
        String year_month = time.substring(0, 4) + time.substring(5, 7);
        int t = Integer.parseInt(year_month);

        // 分配 rowkey_xxx
        int remainder = (pcode ^ t) % 9;

        // 取時間長度時，因為要放在rowkey中，需要方便的拿出來，故設計到 2022-04-30T00，來方便統一
        String ans = row_key_arr[remainder] + "_" + pos + "_" + time.substring(0, 13);

        return ans;
    }

    private static long getStampTime(String time){

        LocalDateTime dateTime = LocalDateTime.parse(time);
        long ts = dateTime.toEpochSecond(ZoneOffset.of("+08:00")) * second2Milli;
        return  ts;

    }

    /*
    row_key: (int)placeCode
    columnFamily: pho
    columnQualifier: (String)phonenum
    value: (long)positionCode
     */
    public static void putData(Connection connection, Table table, ArrayList<ArrayList<Object>> objectLists)
            throws IOException {
        ArrayList<Put> puts = new ArrayList<>(objectLists.size());
        LocalDateTime dateTime;
        String time;
        for (ArrayList<Object> list : objectLists) {
            time = (String) list.get(1);
            dateTime = LocalDateTime.parse(time);
            long ts = dateTime.toEpochSecond(ZoneOffset.of("+08:00")) * second2Milli;

            Put put = new Put(Bytes.toBytes(Integer.toString((int) list.get(2))));
            put.addColumn(Bytes.toBytes("pho"), Bytes.toBytes((String) list.get(0)), ts,
                    Bytes.toBytes((long) list.get(3)));
            puts.add(put);

            put = null;
        }

        table.put(puts);
        System.out.println("Data was inserted Successfully");
    }

}
