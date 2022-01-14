/* Another data generator which generate different pattern of moving and visiting.
People always move in the same direction at X and Y-axis during the whole process.
+- 1 or 0 dot in single iteration */
package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
//import java.lang.Thread;
public class DataGenerator {
    final private static int runStep = 10;

    public static void main(String[] args) throws Exception, IOException {

        final int numOfGeneration = 1;
        Random random = new Random();
        
        BlockData[] blockDatas = new BlockData[MapGenerator.getBlockSize()];
        blockDatas = (BlockData[]) readFromXlsx("./test/map/map.xlsx", blockDatas);
        Person[] people = new Person[PeopleGenerator.getNumOfPeople()];
        people = (Person[]) readFromXlsx("./test/people/people.xlsx", people);

        for(int i=0; i<numOfGeneration; i++) {
            int j = 0;
            for (Person p : people) {
                System.out.println("Starting generation of the "+ j + "-th person's data.");
                generateDataToPerson(p, random, blockDatas);
                j++;
            }
            String filePath = new String("./test/data/data_" + Integer.toString(i) + ".xlsx");
            writeToXlsx(people, filePath);

        }
        
        return;
    }
    
    public static Object[] readFromXlsx(String filePath, Object[] objects) throws IOException {
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
            exception.printStackTrace();
            throw exception;
        }

        if(objects instanceof BlockData[]) {
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
        } else if(objects instanceof Person[]) {
            int numOfPeople = PeopleGenerator.getNumOfPeople();
            Person[] people = new Person[numOfPeople];

            int i = 0;
            for(Row row : sheet) {
                people[i] = new Person();
                people[i].setName(row.getCell(0).getStringCellValue());
                people[i].setePhoneNum(row.getCell(1).getStringCellValue());
                i++;
            }
            return people;
        }
        System.out.println("Error: type of " + objects + "(" + objects.getClass() + ") is not allowed.");
        return null;
    }

    public static void writeToXlsx(Person[] people, String filePath) throws IOException {

        File file = new File(filePath);
        XSSFWorkbook xssf = null;
        SXSSFWorkbook wb = null;
        Sheet sheet = null;
        final int numOfPeople = PeopleGenerator.getNumOfPeople();

        // 確認檔案存在，如果存在，則向後新增
        if (file.exists()) {
            System.out.println("File Exists");
            FileInputStream fileIn = new FileInputStream(filePath);
            xssf = new XSSFWorkbook (fileIn);
            wb = new SXSSFWorkbook(xssf);
            sheet = wb.getSheetAt(0);
        }
        else {
            wb = new SXSSFWorkbook(runStep * numOfPeople);
            sheet      = wb.createSheet();
        }

        // name, timestamp, placeCode, positionCode
        
        ArrayList <ArrayList <Object>> objectLists = new ArrayList<ArrayList<Object>> (runStep * numOfPeople);
        for (Person p : people) {
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
                    cell.setCellValue((Long) object);
                if (object instanceof Boolean)
                    cell.setCellValue((boolean) object);
            }
        }

        // 將object存入xlsx
        FileOutputStream fileOut = new FileOutputStream(filePath);
        try {
            wb.write(fileOut);
        } catch (IOException e) {
            System.out.println("Write Error");
            e.printStackTrace();
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
        int displacementX = 0;  // X位移
        int displacementY = 0;  // y位移
        int temp;
        int[] currentPos = new int[runStep];
        long[] positionCodes = new long[runStep];
        String[] currentTime = new String[runStep];
        boolean isStay = false;
        boolean[] isPositionCodeExist = new boolean[runStep];
        
        int numOfStep = 5;
        int timeInterval = 0;
        for (int i = 0; i < DataGenerator.runStep; i++) {
            // 將時間與場所代碼放入person
            // 每走一步，X +-01, Y +- 0,10
            // 時間間隔亂數產生
            int pos = startX + 10 * startY;
            currentPos[i] = blockDatas[pos].getPlaceCode();
            positionCodes[i] = blockDatas[pos].getPositionCode();
            currentTime[i] = dateTime.plusSeconds(timeInterval).toString();
            timeInterval += random.nextInt(3000) + 600;
            
            if(isStay)  // Which means the person dose not move during this iteration
                isPositionCodeExist[i] = false;
            else
                isPositionCodeExist[i] = blockDatas[pos].getPositionBoolean();

            temp = startX;
            startX = runSteps(startX, random, random.nextInt(numOfStep));    // 每次走隨機步數
            displacementX = startX - temp;

            temp = startY;
            startY = runSteps(startY, random, random.nextInt(numOfStep));
            displacementY = startY - temp;
            // if the person didn't move
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

    // Run multiple steps in 1 iteration. Direcion is fixed if location + s[r] is still in the block.
    public static int runSteps(int location, Random random, int stepNum) {
        int blockLength = MapGenerator.getBlockLength();
        int direction;
        int[][] s = new int[][]{ {0 , 1, 1, 1, 1, 1, 1, 0},
                                 {0, -1, -1, -1, 0, -1, -1, -1} };
        int r = random.nextInt(8);
        direction = random.nextInt(2);
        for(int i = 0; i < stepNum; i++) {
            if((location + s[direction][r]) < 0 || (location + s[direction][r]) >= blockLength)  // boundary check
                r = 0;
            location += s[direction][r];
        }
        return location;
    }
}
