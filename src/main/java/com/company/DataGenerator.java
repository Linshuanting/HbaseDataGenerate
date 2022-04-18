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
import java.time.LocalDate;
// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

//import java.lang.Thread;
public class DataGenerator {
    final private static int runStep = 10;
    private static BlockData Cluster[] = new BlockData[MapGenerator.getClusterNum()];
    private static BlockData blockDatas[];
    // private static int hourLimits = 12 - 8 + 18 - 13, minInterval = 10;
    // private static int minLimits = (60 - 0) / minInterval;
    private static Random random = new Random();

    public static void main(String[] args) throws Exception, IOException {

        final int numOfGeneration = 1000;
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        String filePath = new String("./test/data/data_from_" + localDate.toString() + ".xlsx");

        blockDatas = new BlockData[MapGenerator.getBlockSize()];
        blockDatas = (BlockData[]) readFromXlsx("./test/map/map.xlsx", blockDatas);
        Person[] people = new Person[PeopleGenerator.getNumOfPeople()];
        people = (Person[]) readFromXlsx("./test/people/people.xlsx", people);

        ArrayList<ArrayList<Object>> objectLists = new ArrayList<ArrayList<Object>>(
                runStep * PeopleGenerator.getNumOfPeople());

        for (int i = 0; i < numOfGeneration; i++) {
            System.out.println("Starting generation of the " + (Integer.toString(i + 1)) + "-th day's data.");
            for (Person p : people) {
                // generateDataToPerson(p, random, blockDatas);
                dailyForOneDay(p, random, blockDatas, localDate);
                for (int j = 0; j < p.getArrayLength(); j++) {
                    ArrayList<Object> objectList = new ArrayList<Object>(4);
                    if (p.getPositionBooleans()[j]) {
                        objectList.add(0, p.getPhoneNum());
                        objectList.add(1, p.getTime()[j]);
                        objectList.add(2, p.getPlaceCodes()[j]);
                        objectList.add(3, p.getPositionCodes()[j]);
                        objectLists.add(objectList);
                    } else { // p.getPositioncodes()[i] == false. The program does not generate data to
                             // excel.xlsx
                    }
                    objectList = null;
                }
                p = null;
            }

            System.gc();
            localDate = localDate.plusDays(1);
        }
        writeToXlsx(objectLists, filePath);
        return;
    }

    public static final int getRunStep() {
        return runStep;
    }

    public static Object[] readFromXlsx(String filePath, Object[] objects) throws IOException {
        File file = new File(filePath);
        XSSFWorkbook xssf;
        Sheet sheet;
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(filePath);
            xssf = new XSSFWorkbook(fileIn);
            sheet = xssf.getSheetAt(0);
        } else {
            IOException exception = new IOException("File Not Found!");
            exception.printStackTrace();
            throw exception;
        }

        if (objects instanceof BlockData[]) {
            int blockSize = MapGenerator.getBlockSize();
            BlockData[] blockDatas = new BlockData[blockSize];

            int i = 0, j = 0;
            for (Row row : sheet) {
                blockDatas[i] = new BlockData();
                blockDatas[i].setPlaceCode((int) row.getCell(0).getNumericCellValue());
                blockDatas[i].setPositionCode((long) row.getCell(1).getNumericCellValue());
                blockDatas[i].setPositionBoolean(row.getCell(2).getBooleanCellValue());
                blockDatas[i].setClusterBoolean(row.getCell(3).getBooleanCellValue());

                // 確定 Map 中哪些是集中點
                if (blockDatas[i].getClusterBoolean() == true)
                    Cluster[j++] = blockDatas[i];

                i++;
            }
            xssf.close();
            return blockDatas;
        } else if (objects instanceof Person[]) {
            int numOfPeople = PeopleGenerator.getNumOfPeople();
            Person[] people = new Person[numOfPeople];

            int i = 0;
            for (Row row : sheet) {
                people[i] = new Person();
                people[i].setName(row.getCell(0).getStringCellValue());
                people[i].setePhoneNum(row.getCell(1).getStringCellValue());
                people[i].setLivingPattern((int) row.getCell(2).getNumericCellValue());
                i++;
            }
            xssf.close();
            return people;
        }
        xssf.close();
        System.out.println("Error: type of " + objects + "(" + objects.getClass() + ") is not allowed.");
        return null;
    }

    public static void writeToXlsx(ArrayList<ArrayList<Object>> objectLists, String filePath) throws IOException {

        File file = new File(filePath);
        XSSFWorkbook xssf = null;
        SXSSFWorkbook wb = null;
        Sheet sheet = null;
        final int numOfPeople = PeopleGenerator.getNumOfPeople();

        // 確認檔案存在，如果存在，則向後新增
        if (file.exists()) {
            System.out.println("File Exists");
            FileInputStream fileIn = new FileInputStream(filePath);
            xssf = new XSSFWorkbook(fileIn);
            wb = new SXSSFWorkbook(xssf);
            sheet = wb.getSheetAt(0);
        } else {
            wb = new SXSSFWorkbook(runStep * numOfPeople);
            sheet = wb.createSheet();
        }

        // name, timestamp, placeCode, positionCode

        System.out.println("Writing to xlsx file starts.");

        int rowNum = 0;
        if (file.exists())
            rowNum = xssf.getSheetAt(0).getLastRowNum() + 1;
        int columnNum = 0;

        for (ArrayList<Object> list : objectLists) {

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
            list = null;
        }
        objectLists = null;
        // 將object存入xlsx
        FileOutputStream fileOut = new FileOutputStream(filePath);
        try {
            wb.write(fileOut);
            wb.close();
            wb = null;
            sheet = null;
            xssf = null;
            file = null;
        } catch (IOException e) {
            System.out.println("Write Error");
            e.printStackTrace();
        }
        System.out.println("Writing to XLSX file Finished ...");
    }

    // public static void generateDataToOnePerson(Person p, Random random,
    // BlockData[] blockDatas, int runStep) throws IOException{
    // int blockLength = MapGenerator.getBlockLength();
    // runMode runMode = new runMode(blockLength);
    // pair<Integer, Integer> startPos = new pair<Integer,
    // Integer>(random.nextInt(blockLength), random.nextInt(blockLength));
    // }

    public static void dailyForOneDay(Person p, Random random, BlockData[] blockDatas, LocalDate localDate) {
        int blockLength = MapGenerator.getBlockLength();
        // 從Cluster中產生目標點
        int firstPlaceCode = Cluster[random.nextInt(blockLength)].getPlaceCode();
        int secondPlaceCode = Cluster[random.nextInt(blockLength)].getPlaceCode();

        // 判斷此人類型，並決定目標位置
        switch (p.getLivingPattern()) {
            /*
             * 1 : 早八晚五型，目標相同
             * 2 : 早八晚五型，目標不同
             * 3 : 早八晚五型，無活動
             * 4 : 早八午十二，單目標
             * 5 : 午十二晚五，單目標
             * 6 : 活動時間與路徑相對隨機
             */
            case 1:
                secondPlaceCode = firstPlaceCode;
                break;
            case 2:
                break;
            case 3:
                firstPlaceCode = 0;
                secondPlaceCode = 0;
                break;
            case 4:
                secondPlaceCode = 0;
                break;
            case 5:
                firstPlaceCode = 0;
                break;
            case 6:
                firstPlaceCode = -1;
                secondPlaceCode = -1;
                break;
            default:
                System.out.println("-------Not correct Living Pattern-------");
        }

        // 決定好起始點以及終點
        pair<Integer, Integer> nowXY = new pair<Integer, Integer>(random.nextInt(blockLength),
                random.nextInt(blockLength));
        pair<Integer, Integer> firstTargetXY = getPairXY(firstPlaceCode);
        pair<Integer, Integer> secondTargetXY = getPairXY(secondPlaceCode);

        boolean targetArrival = false;
        int hourLimits = 12 - 8 + 18 - 13, minInterval = 10;
        int minLimits = (60 - 0) / minInterval;
        int counter = 0;
        int[] currentPos = new int[hourLimits * minLimits];
        long[] positionCodes = new long[hourLimits * minLimits];
        String[] currentTime = new String[hourLimits * minLimits];
        boolean[] isPositionCodeExist = new boolean[hourLimits * minLimits];

        // 早上
        for (int hours = 8; hours < 12 && (targetArrival == false); hours++) {
            // 每次10分鐘一步，看結果
            for (int min = 0; min < 60 && (targetArrival == false); min += minInterval) {
                nowXY = runMap(nowXY, firstTargetXY);

                // 判斷有無到達目標，有則上午目標結束
                if (isArrivalTarget(nowXY, firstTargetXY)) {
                    nowXY = firstTargetXY;
                    targetArrival = true;
                }

                // 判斷此點是否有positionCode，有則需要紀錄(目標)
                // 目前是直接紀錄
                dataRecord(currentPos, positionCodes, currentTime, isPositionCodeExist, counter, localDate, hours, min,
                        nowXY);
                counter++;
            }
        }

        targetArrival = false;

        // 晚上
        for (int hours = 13; hours < 18; hours++) {
            // 每次10分鐘一步，看結果
            for (int min = 0; min < 60 && targetArrival == false; min += minInterval) {
                nowXY = runMap(nowXY, secondTargetXY);

                // 判斷有無到達目標，有則上午目標結束
                if (isArrivalTarget(nowXY, secondTargetXY)) {
                    nowXY = secondTargetXY;
                    targetArrival = true;
                }

                // 判斷此點是否有positionCode，有則需要紀錄(目標)
                // 目前是直接紀錄
                dataRecord(currentPos, positionCodes, currentTime, isPositionCodeExist, counter, localDate, hours, min,
                        nowXY);
                counter++;

            }
        }

        p.setPlaceCodes(currentPos);
        p.setPositionBooleans(isPositionCodeExist);
        p.setPositionCodes(positionCodes);
        p.setTime(currentTime);
        p.setArrayLength(counter);
    }

    public static void dataRecord(int[] currentPos, long[] positionCodes, String[] currentTime,
            boolean[] isPositionCodeExist, int i, LocalDate date, int hour, int min, pair<Integer, Integer> XY) {

        int pos = getPositionFromPair(XY);

        if (pos > 10000) {
            System.out.println(pos);
        }
        // 時間細節設定
        String time = getTime(date, hour, min);

        //
        currentPos[i] = blockDatas[pos].getPlaceCode();
        positionCodes[i] = blockDatas[pos].getPositionCode();
        isPositionCodeExist[i] = blockDatas[pos].getPositionBoolean();
        currentTime[i] = time;

        return;
    }

    // 時間處理函數
    public static String getTime(LocalDate date, int hour, int min) {

        // 預設時間
        String time = date.toString() + "T";
        if (hour < 10) {
            time = time + "0" + Integer.toString(hour);
        } else {
            time = time + Integer.toString(hour);
        }
        time = time + ":";

        if (min < 10) {
            time = time + "0" + Integer.toString(min);
        } else {
            time = time + Integer.toString(min);
        }
        time = time + ":";
        int seconds = random.nextInt(60);
        if (seconds < 10)
            time += "0" + Integer.toString(seconds);
        else
            time += Integer.toString(seconds);

        return time;
    }

    // 當離目的地距離為五以內，直接視為到達目的地
    public static boolean isArrivalTarget(pair<Integer, Integer> now, pair<Integer, Integer> target) {

        // 隨機走路，不會到達目標
        if (target.getFirst() < 0 && target.getSecond() < 0)
            return false;

        // 停在原地，視為已到達目標
        if (target.getFirst() == 0 && target.getSecond() == 0)
            return true;

        int differentX = abs(now.getFirst() - target.getFirst());
        int differentY = abs(now.getSecond() - target.getSecond());

        if (differentX <= 5 && differentY <= 5)
            return true;

        return false;

    }

    public static int abs(int a) {
        return a >= 0 ? a : -1 * a;
    }

    public static boolean isPositionCodeExist(pair<Integer, Integer> XY) {

        // 位置(-1, -1)表示此點不存在
        if (XY.getFirst() < 0 && XY.getSecond() < 0)
            return false;

        int position = XY.getSecond() * MapGenerator.getBlockLength() + XY.getFirst();

        if (blockDatas[position].getPositionBoolean())
            return true;
        else
            return false;
    }

    public static int getPositionFromPair(pair<Integer, Integer> XY) {

        if (XY.getFirst() == -1 && XY.getSecond() == -1)
            return -1;

        if (XY.getFirst() >= MapGenerator.getBlockLength())
            XY.setFirst(MapGenerator.getBlockLength() - 1);

        if (XY.getSecond() >= MapGenerator.getBlockLength())
            XY.setSecond(MapGenerator.getBlockLength() - 1);

        if (XY.getFirst() < 0)
            XY.setFirst(0);

        if (XY.getSecond() < 0)
            XY.setSecond(0);

        return XY.getFirst() + XY.getSecond() * MapGenerator.getBlockLength();

    }

    public static pair<Integer, Integer> getPairXY(int placeCode) {

        if (placeCode == -1)
            return new pair<Integer, Integer>(-1, -1);
        if (placeCode == 0)
            return new pair<Integer, Integer>(0, 0);

        int blockLength = MapGenerator.getBlockLength();
        int blockSize = MapGenerator.getBlockSize();

        int X = (placeCode - blockSize) % blockLength;
        int Y = (placeCode - blockSize) / blockLength;

        if (X >= blockLength)
            X = blockLength - 1;

        if (X < 0)
            X = 0;

        if (Y >= blockLength)
            Y = blockLength - 1;

        if (Y < 0)
            Y = 0;

        return new pair<Integer, Integer>(X, Y);

    }

    public static pair<Integer, Integer> runMap(pair<Integer, Integer> now, pair<Integer, Integer> target) {

        // Random random = new Random();

        // 步數為 runStep
        runMode runMode = new runMode(MapGenerator.getBlockLength());
        now = runMode.runSteps(runStep, now, target);

        return now;
    }

    /*
     * // 將人物隨機位置開始，並亂數走動
     * public static void generateDataToPerson(Person p, Random random, BlockData[]
     * blockDatas) throws IOException {
     * 
     * // 移動runStep次
     * LocalDateTime dateTime = LocalDateTime.now();
     * 
     * int blockLength = MapGenerator.getBlockLength();
     * int startX = random.nextInt(blockLength);
     * int startY = random.nextInt(blockLength);
     * int displacementX = 0; // X位移
     * int displacementY = 0; // y位移
     * int temp;
     * int[] currentPos = new int[runStep];
     * long[] positionCodes = new long[runStep];
     * String[] currentTime = new String[runStep];
     * boolean isStay = false;
     * boolean[] isPositionCodeExist = new boolean[runStep];
     * 
     * int numOfStep = 5;
     * int timeInterval = 0;
     * for (int i = 0; i < DataGenerator.runStep; i++) {
     * // 將時間與場所代碼放入person
     * // 每走一步，X +-01, Y +- 0,10
     * // 時間間隔亂數產生
     * int pos = startX + blockLength * startY;
     * currentPos[i] = blockDatas[pos].getPlaceCode();
     * positionCodes[i] = blockDatas[pos].getPositionCode();
     * currentTime[i] = dateTime.plusSeconds(timeInterval).toString();
     * timeInterval += random.nextInt(3000) + 600;
     * 
     * if(isStay) // Which means the person dose not move during this iteration
     * isPositionCodeExist[i] = false;
     * else
     * isPositionCodeExist[i] = blockDatas[pos].getPositionBoolean();
     * 
     * temp = startX;
     * startX = runSteps(startX, random, random.nextInt(numOfStep)); // 每次走隨機步數
     * displacementX = startX - temp;
     * 
     * temp = startY;
     * startY = runSteps(startY, random, random.nextInt(numOfStep));
     * displacementY = startY - temp;
     * // if the person didn't move
     * if(displacementX == 0 && displacementY == 0)
     * isStay = true;
     * else
     * isStay = false;
     * }
     * 
     * p.setPlaceCodes(currentPos);
     * p.setPositionBooleans(isPositionCodeExist);
     * p.setPositionCodes(positionCodes);
     * p.setTime(currentTime);
     * }
     */
    // Run multiple steps in 1 iteration. Direcion is fixed if location + s[r] is
    // still in the block.
    public static int runSteps(int location, Random random, int stepNum) {
        int blockLength = MapGenerator.getBlockLength();
        int direction;
        int[][] s = new int[][] { { 0, 1, 1, 1, 1, 1, 1, 0 },
                { 0, -1, -1, -1, 0, -1, -1, -1 } };
        int r = random.nextInt(8);
        direction = random.nextInt(2);
        for (int i = 0; i < stepNum; i++) {
            if ((location + s[direction][r]) < 0 || (location + s[direction][r]) >= blockLength) // boundary check
                r = 0;
            location += s[direction][r];
        }
        return location;
    }
}
