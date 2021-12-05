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
    private static runMode runMd;

    static {
        BlockLength = 100;
        BlockSize = (int)Math.pow(BlockLength, 2);
        timeRange = 500;
        runStep = 100;
        blockdatas = new Blockdata[BlockSize];
        runMd = new runMode(BlockLength);
    }

    public static void main(String[] args) throws IOException {
        //int numOfPeople = ;
        Person [] people;

        Random random = new Random();
        generateBlockData(random);


        Person apple = new Person("Apple", 1000024);
        generateDataToPerson(apple, random);

        writeToXlsx(apple, "D:\\test\\mytest01\\test.xlsx");

        return;

    }

    public static void test(Person p) {
        p.setName("Banana");
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

        // name, timestamp, placeCodes, positionCode
        Object[][] objects = new Object[runStep][5];

        // 將資料存入 object
        for (int i = 0; i < runStep; i++){
            objects[i][0] = p.getName();
            objects[i][1] = p.getTime()[i];
            objects[i][2] = p.getPlaceCodes()[i];
            objects[i][3] = p.getPositionCodes()[i];
            objects[i][4] = p.getTargetDir()[i];
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
                    cell.setCellValue((String) obj);
                if (obj instanceof Integer)
                    cell.setCellValue((int) obj);
                if (obj instanceof Boolean)
                    cell.setCellValue((boolean) obj);
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
    // EX. x = 2, y = 0, placeCodes = 10002
    public static void generateBlockData(Random random){
 
        int x = 0, y = 0;
        int placeCodes = 1000000;
        boolean isPositionCodeExist;
        // About 1/3 dots have positionCode
        boolean [] temp = new boolean [] {true, false, false};

        for (int i = 0; i < BlockSize; i++) {
            if (x >= BlockLength){
                x = 0;
                y += 1;
            }
            isPositionCodeExist = temp[random.nextInt(3)];
            blockdatas[i] = new Blockdata(x++, y, placeCodes++, isPositionCodeExist);
        }
    }

    // 將人物隨機位置開始，並亂數走動
    public static void generateDataToPerson(Person p, Random random){
        
        // time 0 ~ 500，走20步
        int start_time = random.nextInt(timeRange);
        int startX = random.nextInt(BlockLength);
        int startY = random.nextInt(BlockLength);
        int[] currentPos = new int[runStep];
        int[] currentTime = new int[runStep];
        String [] nowTargetDir = new String[runStep];
        boolean isSame = false;
        boolean[] isPositionCodeExist = new boolean[runStep];
        pair<Integer, Integer> XY = new pair<Integer, Integer>(startX, startY);

        // 將時間與場所代碼放入person
        for (int i = 0; i < dataMap.runStep; i++) {

            // 每次走 0~5 步
            int pos = startX + BlockLength * startY;
            currentPos[i] = blockdatas[pos].getPlaceCode();
            currentTime[i] = start_time + i;
            if (!isSame)
                isPositionCodeExist[i] = blockdatas[pos].getPositionCode();
            else
                isPositionCodeExist[i] = false;

            // 取得目的地方位
            String dir = targetDirection(startX, startY, p.getTarget());
            nowTargetDir[i] = dir;

            // 如果以到達目的地後，之後將隨機亂逛
            if (dir.equals("目的地"))
                p.setTarget(0);

            // 決定走的步數以及方向
            XY = runDirectionAndStep(5, XY, random, dir, p.getTarget());

            // 判斷沒有移動的話，即表示此人不需再掃一次QRCode，則isPos為false
            if(XY.getFirst() == startX && XY.getSecond() == startY)
                isSame = true;
            else
                isSame = false;

            startX = XY.getFirst();
            startY = XY.getSecond();
        }

        p.setPlaceCodes(currentPos);
        p.setPositionCodes(isPositionCodeExist);
        p.setTime(currentTime);
        p.setTargetDir(nowTargetDir);
    }

    // 決定走的方向、步數
    public static pair<Integer, Integer> runDirectionAndStep(int runStepRange, pair<Integer, Integer>XY, Random random, String direction, int target){

        int steps = random.nextInt(runStepRange);


        if (direction.equals("右上")){
            // 隨機走右或上
            if (random.nextInt(2) == 0){
                // 走右邊
                XY = runMd.runSteps(runMd.chooseRight(), steps, XY, target);
            }
            else {
                // 走上
                XY =runMd.runSteps(runMd.chooseUp(), steps, XY, target);
            }
        }
        else if (direction.equals("左上")){
            if (random.nextInt(2) == 0){
                XY =runMd.runSteps(runMd.chooseLeft(), steps, XY, target);
            }
            else {
                XY =runMd.runSteps(runMd.chooseUp(), steps, XY, target);
            }
        }
        else if (direction.equals("右下")){
            if (random.nextInt(2) == 0){
                XY =runMd.runSteps(runMd.chooseRight(), steps, XY, target);
            }
            else {
                XY =runMd.runSteps(runMd.chooseDown(), steps, XY, target);
            }
        }
        else if (direction.equals("左下")){
            if (random.nextInt(2) == 0){
                XY =runMd.runSteps(runMd.chooseLeft(), steps, XY, target);
            }
            else {
                XY =runMd.runSteps(runMd.chooseDown(), steps, XY, target);
            }
        }
        else if (direction.equals("上")){
            XY =runMd.runSteps(runMd.chooseUp(), steps, XY, target);
        }
        else if (direction.equals("下")){
            XY =runMd.runSteps(runMd.chooseDown(), steps, XY, target);
        }
        else if (direction.equals("左")){
            XY =runMd.runSteps(runMd.chooseLeft(), steps, XY, target);
        }
        else if (direction.equals("右")){
            XY =runMd.runSteps(runMd.chooseRight(), steps, XY, target);
        }
        else{
            XY =runMd.runSteps(runMd.chooseRANDOM(), steps, XY, target);
        }

        return XY;
    }

    // 判決此人目標方向
    public static String targetDirection(int nowX, int nowY, int targetPlaceCode){

        // 取得對象目標想去的方位
        int targetX = targetPlaceCode % BlockLength;
        int targetY = (targetPlaceCode / BlockLength) % BlockLength;

        if (targetPlaceCode == 0)
            return "無";

        if (targetX > nowX && targetY > nowY)
            return "右上";
        else if (targetX > nowX && targetY < nowY)
            return "右下";
        else if (targetX < nowX && targetY > nowY)
            return "左上";
        else if (targetX < nowX && targetY < nowY)
            return "左下";
        else if (targetX == nowX && targetY > nowY)
            return "上";
        else if (targetX == nowX && targetY < nowY)
            return "下";
        else if (targetY == nowY && targetX > nowX)
            return "右";
        else if (targetY == nowY && targetX < nowX)
            return "左";
        else
            return "目的地";

    }

    // Run multiple steps in 1 iteration. Direcion is fixed if n + s[r] is still in the block.
    public static int runSteps(int n, Random random, int stepNum) {
        int direction;
        int[][] s = new int[][]{ {0 , 1, 1, 1, 0, 1, 0, 1},
                                 {-1, -1, 0, -1, 0, -1, 0, -1} };
        int r = random.nextInt(8);
        direction = random.nextInt(2);
        for(int i = 0; i < stepNum; i++) {
            while ((n + s[direction][r]) <= 0 || (n + s[direction][r]) >= BlockLength)
                r = random.nextInt(8);
            n += s[direction][r];
        }
        return n;
    }
}

class Blockdata {
    private int xPos;
    private int yPos;
    // 實際地址
    private int placeCode;
    // 此地有無商店
    private boolean isPositionCodeExist;
    
    public Blockdata (int xPos, int yPos, int placeCode, boolean isPositionCodeExist){
        setxPos(xPos);
        setyPos(yPos);
        setPlaceCode(placeCode);
        setPositionCode(isPositionCodeExist);
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
    public boolean getPositionCode() {
        return isPositionCodeExist;
    }
    public void setPositionCode(boolean isPositionCodeExist) {
        this.isPositionCodeExist = isPositionCodeExist;
    }
}

class Person {
    private String name;
    private String phoneNum;
    private int target; // target placeCode
    private int [] time;
    private int [] placeCodes;
    private boolean [] positionCodes;
    private String [] targetDir;

    public Person(String name, int target) {
        this.name = name;
        this.target = target;
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

    public int[] getPlaceCodes() {
        return placeCodes;
    }

    public void setPlaceCodes(int[] placeCodes) {
        this.placeCodes = placeCodes;
    }

    public boolean[] getPositionCodes() {
        return positionCodes;
    }

    public void setPositionCodes(boolean[] positionCodes) {
        this.positionCodes = positionCodes;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    public String[] getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String[] targetDir) {
        this.targetDir = targetDir;
    }
}

