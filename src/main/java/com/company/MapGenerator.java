package com.company;

// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Random;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MapGenerator {
    final private static int blockSize = 10000;
    final private static int blockLength = (int) Math.sqrt(blockSize);
    private static BlockData[] blockDatas = new BlockData[blockSize];
    private static int clusterNum = 100;

    // Generate the Map
    // public static void main(String[] args) throws Exception, IOException {
    // Random random = new Random();
    // generateBlockData(random);
    // generateRandomCluster(blockSize);
    // writeToXlsx("./test/map/map.xlsx");
    // return;
    // }

    public static BlockData[] getBlockDatas() {
        return blockDatas;
    }

    public static void generateMap() {
        generateBlockData();
        generateRandomCluster(blockSize);
    }

    // 以2維陣列產生區域
    // 地址(placeCode) 從10000開始
    // EX. x = 2, y = 0, placeCodes = 10002
    private static void generateBlockData() {
        Random random = new Random();
        int x = 0, y = 0;
        int placeCode = blockSize;

        HashMap<Integer, Long> map = new HashMap<Integer, Long>(blockSize);
        long positionCode = 0;
        boolean isPositionCodeExist;
        // About 2/5 dots have positionCode
        boolean[] temp = new boolean[] { true, false, false, true, false };

        for (int i = 0; i < blockSize; i++) {
            if (x >= blockLength) {
                x = 0;
                y += 1;
            }

            isPositionCodeExist = temp[random.nextInt(5)];
            if (isPositionCodeExist) {
                positionCode = ThreadLocalRandom.current().nextLong(100000000000000l, 300000000000000l);
                while (map.containsValue(positionCode)) // The position code MUST be unique.
                    positionCode = ThreadLocalRandom.current().nextLong(100000000000000l, 300000000000000l);
                map.put(i, positionCode);
            } else
                positionCode = 0;

            blockDatas[i] = new BlockData(x++, y, placeCode++, isPositionCodeExist, positionCode);
        }
    }

    public static void generateRandomCluster(int area) {
        Random random = new Random();

        for (int NeedNum = clusterNum; NeedNum > 0; /* do nothing */) {
            int target = random.nextInt(area);

            if (blockDatas[target].getPositionBoolean() == true && blockDatas[target].getClusterBoolean() == false) {
                blockDatas[target].setClusterBoolean(true);
                NeedNum--;
            }
        }

        System.out.println("------Cluster generating finish-------");

        return;
    }

    // public static void writeToXlsx(String filePath) throws IOException{
    // File file = new File(filePath);
    // SXSSFWorkbook wb = null;
    // Sheet sheet = null;

    // if (file.exists()) {
    // IOException exception = new IOException("File Exists Exception");
    // throw exception;
    // }
    // else {
    // wb = new SXSSFWorkbook(blockLength);
    // sheet = wb.createSheet();
    // }
    // // (pos, positionCode, isPositionCode, isCluster)
    // ArrayList <Object[]> objectLists = new ArrayList<> (blockSize);
    // for(int i=0; i<blockSize; i++) {
    // Object[] objects = new Object[4];
    // objects[0] = blockDatas[i].getPlaceCode();
    // objects[1] = blockDatas[i].getPositionCode();
    // objects[2] = blockDatas[i].getPositionBoolean();
    // objects[3] = blockDatas[i].getClusterBoolean();
    // objectLists.add(objects);
    // objects = null;
    // }
    // System.out.println("Writing to xlsx file starts.");

    // int rowNum = 0;
    // int columnNum = 0;

    // for (Object[] objects : objectLists) {
    // Row row = sheet.createRow(rowNum++);
    // columnNum = 0;

    // for (Object object : objects) {
    // Cell cell = row.createCell(columnNum++);
    // if (object instanceof Integer)
    // cell.setCellValue((int) object);
    // if (object instanceof Long)
    // cell.setCellValue((long) object);
    // if (object instanceof Boolean)
    // cell.setCellValue((boolean) object);
    // if (object instanceof String)
    // cell.setCellValue((String) object);
    // }
    // }

    // // 將object存入xlsx
    // FileOutputStream fileOut = new FileOutputStream(filePath);
    // try {
    // wb.write(fileOut);
    // wb.close();
    // } catch (Exception e) {
    // System.out.println("Write Error");
    // }
    // System.out.println("Writing to XLSX file Finished ...");
    // }

    public static int getBlockLength() {
        return blockLength;
    }

    public static int getBlockSize() {
        return blockSize;
    }

    public static int getClusterNum() {
        return clusterNum;
    }

}

class BlockData {
    private int xPos;
    private int yPos;
    private int placeCode;
    private long positionCode;
    private boolean isPositionCodeExist;
    private boolean isCluster = false;

    public BlockData(int xPos, int yPos, int placeCode, boolean isPositionCodeExist, long positionCode) {
        setxPos(xPos);
        setyPos(yPos);
        setPlaceCode(placeCode);
        setPositionBoolean(isPositionCodeExist);
        setPositionCode(positionCode);
    }

    public BlockData() {
        // Default Constructor
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

    public boolean getPositionBoolean() {
        return isPositionCodeExist;
    }

    public void setPositionBoolean(boolean isPositionCodeExist) {
        this.isPositionCodeExist = isPositionCodeExist;
    }

    public long getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(long positionCode) {
        this.positionCode = positionCode;
    }

    public boolean getClusterBoolean() {
        return isCluster;
    };

    public void setClusterBoolean(boolean isCluster) {
        this.isCluster = isCluster;
    }

}
