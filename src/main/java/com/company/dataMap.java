package com.company;
/*
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
*/
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class dataMap {/*
    //private static int timeRange;
    private static int runStep;

    static {
        //timeRange = 500;
        runStep = 20;
    }*/

    public static void main(String[] args) /*throws Exception, IOException*/ {
        /*
        int numOfGeneration = 1;
        Random random = new Random();
        BlockData[] blockDatas = readFromXlsx("./test/map/map.xlsx");
        for(int i=0; i<numOfGeneration; i++) {
            Person apple = new Person("Apple");
            generateDataToPerson(apple, random, blockDatas);
            writeToXlsx(apple, "./test/mytest01/test1.xlsx");
            apple = null;
        }*/


        return;
    }
}
    /*public static void test(Person p) {
        p.setName("Banana");
    }*/
    /*
    public static BlockData[] readFromXlsx(String filePath) throws IOException {
        File file = new File(filePath);
        XSSFWorkbook xssf;
        Sheet sheet;
        if(file.exists()) {
            FileInputStream fileIn = new FileInputStream(filePath);
            xssf = new XSSFWorkbook (fileIn);
            sheet = xssf.getSheetAt(0);
        }
        else {
            IOException exception = new IOException("File Not Found!");
            throw exception;
        }

        int blockSize = MapGenerator.getBlockSize();
        BlockData[] blockDatas = new BlockData[blockSize];
        
        int i = 0;
        for (Row row : sheet) {
            blockDatas[i] = new BlockData();
            blockDatas[i].setPlaceCode((int) row.getCell(0).getNumericCellValue());
            blockDatas[i].setPositionCode((long) row.getCell(1).getNumericCellValue());
            blockDatas[i].setPositionBoolean(row.getCell(2).getBooleanCellValue());
            i++;
        }

        return blockDatas;
    }
    public static void writeToXlsx(Person p, String filePath) throws IOException {

        File file = new File(filePath);
        XSSFWorkbook xssf = null;
        SXSSFWorkbook wb = null;
        Sheet sheet = null;

        // 確認檔案存在，如果存在，則向後新增
        if (file.exists()) {
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

        // name, timestamp, placeCode, positionCode
        ArrayList <ArrayList <Object>> objectLists = new ArrayList<ArrayList<Object>> (runStep);

        for (int i = 0; i < runStep; i++) {
            ArrayList <Object> objectList = new ArrayList<Object> (4);
            if(p.getPositionBooleans()[i]) {
                objectList.add(0, p.getPhoneNum());
                objectList.add(1, p.getTime()[i]);
                objectList.add(2, p.getPlaceCodes()[i]);
                objectList.add(3, p.getPositionCodes()[i]);
                objectLists.add(objectList);
            }
            else {  // p.getPositioncodes()[i] == false. The program does not generate data to excel.xlsx
            }
            objectList = null;
        }
        
        System.out.println("Writing to xlsx file starts.");

        int rowNum = 0;
        if (file.exists())
            rowNum = xssf.getSheetAt(0).getLastRowNum() + 1;
        int columnNum = 0;

        for (ArrayList <Object> list: objectLists) {

            Row row = sheet.createRow(rowNum++);
            columnNum = 0;
            for (Object object : list) {
                Cell cell = row.createCell(columnNum++);
                if (object instanceof String)
                    cell.setCellValue((String) object);
                if (object instanceof Integer)
                    cell.setCellValue((int) object);
                if (object instanceof Long)
                    cell.setCellValue((long) object);
                if (object instanceof Boolean)
                    cell.setCellValue((boolean) object);
            }
        }

        // 將object存入xlsx
        FileOutputStream fileOut = new FileOutputStream(filePath);
        try {
            wb.write(fileOut);
        } catch (Exception e) {
            System.out.println("Write Error");
        }
        System.out.println("Writing to XLSX file Finished ...");
    }

    // 將人物隨機位置開始，並亂數走動
    public static void generateDataToPerson(Person p, Random random, BlockData[] blockDatas) throws IOException {
        
        // 移動runStep次
        LocalDateTime dateTime = LocalDateTime.now();

        int blockLength = MapGenerator.getBlockLength();
        int startX = random.nextInt(blockLength);
        int startY = random.nextInt(blockLength);
        int displacementX = 0;
        int displacementY = 0;
        int temp;
        int[] currentPos = new int[runStep];
        long[] positionCodes = new long[runStep];
        String[] currentTime = new String[runStep];
        boolean isStay = false;
        boolean[] isPositionCodeExist = new boolean[runStep];

        int numOfStep = 5;
        int timeInterval = 0;
        for (int i = 0; i < dataMap.runStep; i++) {
            // 將時間與場所代碼放入person
            // 每次走一步，X +-01, Y +- 0,10
            // 時間間隔亂數產生
            int pos = startX + 10 * startY;
            currentPos[i] = blockDatas[pos].getPlaceCode();
            positionCodes[i] = blockDatas[pos].getPositionCode();
            currentTime[i] = dateTime.plusSeconds(timeInterval).toString();
            timeInterval += random.nextInt(3000) + 600;
            
            if(isStay)
                isPositionCodeExist[i] = false;
            else
                isPositionCodeExist[i] = blockDatas[pos].getPositionBoolean();

            temp = startX;
            startX = runSteps(startX, random, random.nextInt(numOfStep));    // 每次走隨機步數
            displacementX = startX - temp;

            temp = startY;
            startY = runSteps(startY, random, random.nextInt(numOfStep));
            displacementY = startY - temp;

            if(displacementX == 0 && displacementY == 0)
                isStay = true;
            else
                isStay = false;
        }

        p.setPlaceCodes(currentPos);
        p.setPositionBooleans(isPositionCodeExist);
        p.setPositionCodes(positionCodes);
        p.setTime(currentTime);
    }
    /*public static int runOneStep(int n, Random random) {

        int[] s = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
        int r = random.nextInt(8);
        while ((n + s[r]) <= 0 || (n + s[r]) >= blockLength)
            r = random.nextInt(8);
        return n + s[r];
    }*/
/*
    // Run multiple steps in 1 iteration. Direcion is fixed if n + s[r] is still in the block.
    public static int runSteps(int n, Random random, int stepNum) {
        int blockLength = MapGenerator.getBlockLength();
        int direction;
        int[][] s = new int[][]{ {0 , 1, 1, 1, 0, 1, 1, 1},
                                 {-1, -1, -1, -1, 0, -1, 0, -1} };
        int r = random.nextInt(8);
        direction = random.nextInt(2);
        for(int i = 0; i < stepNum; i++) {
            while ((n + s[direction][r]) <= 0 || (n + s[direction][r]) >= blockLength)
                r = random.nextInt(8);
            n += s[direction][r];
        }
        return n;
    }
}
*/