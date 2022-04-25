package com.company;

// Schema of 'table1'
// | 1st | phone number | position(VERSIONS => 100) | position code | location |
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

// import org.apache.hadoop.hbase.TableName;
// import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.xssf.streaming.SXSSFWorkbook;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PutData1 {
    private static final int second2Milli = 1000;

    // public static void main(String[] args) throws MasterNotRunningException,
    // IOException {
    // // Instantiating a Connection class object and table object
    // Connection connection = ConnectionFactory.createConnection();
    // Table table = connection.getTable(TableName.valueOf("table1"));
    // sizeOfList = 0;
    // // final int numOfInFiles = 4;
    // // for (int i = 0; i < numOfInFiles; i++) {
    // //
    // readFromXlsx("/home/shang/repo/myHBaseProject/HbaseDataGenerate/test/data/data_from_1-"
    // // + Integer.toString(1 + (i * 7)) + "_sorted.xlsx");
    // // }

    // ArrayList<Put> puts = new ArrayList<>(sizeOfList);

    // table.put(puts);
    // System.out.println("Data was inserted Successfully");

    // // Close table and connection
    // table.close();
    // connection.close();
    // }

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
