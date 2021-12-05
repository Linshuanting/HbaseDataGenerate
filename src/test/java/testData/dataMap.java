package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Random;

public class dataMap {

    private static int BlockSize;
    private static int BlockLength;
    private static Blockdata[] blockdatas;
    private static int timeRange;
    private static int runStep;

    static {
        BlockSize = 100;
        BlockLength = (int)Math.sqrt(BlockSize);
        timeRange = 500;
        runStep = 20;
        blockdatas = new Blockdata[BlockSize];
    }

    public static void main(String[] args) throws IOException {

        generateBlockData();

        Person apple = new Person("Apple");
        generateDataToPerson(apple);

        writeToXlsx(apple, "D:\\test\\mytest01\\test.xlsx");

        return;

    }

    public static void test(Person p){
        p.setName("Banana");
    }

    public static void writeToXlsx(Person p, String filePath) throws IOException {

        File file = new File(filePath);
        XSSFWorkbook xssf = null;
        SXSSFWorkbook wb = null;
        Sheet sheet = null;

        // 確認檔案存在，如果存在，則向後新增
        if (file.exists()){
            System.out.println("File Exist");
            FileInputStream fileIn = new FileInputStream(filePath);
            xssf = new XSSFWorkbook (fileIn);
            wb = new SXSSFWorkbook(xssf);
            sheet = wb.getSheetAt(0);
        }
        else {
            wb = new SXSSFWorkbook(runStep);
            sheet      = wb.createSheet();
        }

        // name, timestamp, placecode
        Object[][] objects = new Object[runStep][3];

        // 將資料存入 object
        for (int i = 0; i < runStep; i++){
            objects[i][0] = p.getName();
            objects[i][1] = p.getTime()[i];
            objects[i][2] = p.getPlaceCode()[i];
        }

        System.out.println("Write start");

        int rowNum = 0;
        if (file.exists())
            rowNum = xssf.getSheetAt(0).getLastRowNum()+1;
        int columnNum = 0;
        for (Object[] object : objects) {

            Row row = sheet.createRow(rowNum++);
            columnNum = 0;
            for (Object obj : object) {
                Cell cell = row.createCell(columnNum++);
                if (obj instanceof String)
                    cell.setCellValue((String)obj);
                if (obj instanceof Integer)
                    cell.setCellValue((int)obj);
            }
        }

        // 將object存入xlsx
        FileOutputStream fileOut = new FileOutputStream(filePath);
        try {
            wb.write(fileOut);
        }catch (Exception e){
            System.out.println("Write Error");
        }
        System.out.println("Writing on XLSX file Finished ...");


    }

    // 以2維陣列產生區域
    // 場所代碼從 10000開始
    // EX. x = 2, y = 0, placeCode = 10002
    public static void generateBlockData(){

        int x = 0, y = 0;
        int placeCode = 10000;

        for (int i = 0; i < BlockSize; i++){
            if (x >= BlockLength){
                x = 0;
                y += 1;
            }
            blockdatas[i] = new Blockdata(x++, y, placeCode++);
        }
    }

    // 將人物隨機位置開始，並亂數走動
    public static void generateDataToPerson(Person p){

        Random random = new Random();

        // time 0 ~ 500，走20步
        int start_time = random.nextInt(timeRange);
        int startX = random.nextInt(BlockLength);
        int startY = random.nextInt(BlockLength);
        int[] currentPos = new int[runStep];
        int[] currentTime = new int[runStep];

        for (int i = 0; i < dataMap.runStep; i++){

            // 將時間與場所代碼放入person
            // 每次走一步，X +-01, Y +- 0,10
            int pos = startX + 10 * startY;
            currentPos[i] = blockdatas[pos].getPlaceCode();
            currentTime[i] = start_time + i;

            startX = runOneStep(startX, random);
            startY = runOneStep(startY, random);
        }

        p.setPlaceCode(currentPos);
        p.setTime(currentTime);

    }

    public static int runOneStep(int n, Random random){

        int[] s = new int[]{0,1,1,1,0,-1,-1,-1};
        int r = random.nextInt(8);
        while ((n + s[r]) <= 0 || (n + s[r]) >= BlockLength)
            r = random.nextInt(8);
        return n + s[r];
    }


}

class Blockdata{
    private int xPos;
    private int yPos;
    private int placeCode;

    public Blockdata (int xPos, int yPos, int placeCode){
        setxPos(xPos);
        setyPos(yPos);
        setPlaceCode(placeCode);
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(int placeCode) {
        this.placeCode = placeCode;
    }
}

class Person {
    private String name;
    private int [] time;
    private int [] placeCode;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public int[] getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(int[] placeCode) {
        this.placeCode = placeCode;
    }
}


