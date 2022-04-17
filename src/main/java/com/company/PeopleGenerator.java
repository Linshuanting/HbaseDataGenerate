package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

public class PeopleGenerator {
    final private static int numOfPeople = 1000;
    private static Person[] people = new Person[numOfPeople];

    // Generate people list
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        generatePeople(random);
        writeToXlsx("./test/people/people.xlsx");
        return;
    }

    private static void generatePeople(Random random) {
        HashMap <Integer, ArrayList <Integer>> map = new HashMap<>(numOfPeople);
        
        for(int name = 0; name < numOfPeople; name++) {
            ArrayList <Integer> phoneNumber = new ArrayList<>(10);
            String phoneNumString = new String();

            for(int i = 0; i < 10; i++) {
                if(i == 0)
                    phoneNumber.add(0);
                else if(i == 1) {
                    phoneNumber.add(9);
                }
                else
                    phoneNumber.add(random.nextInt(10));
            }
            while (map.containsValue(phoneNumber)) {
                for(int i = 0; i < 10; i++) {
                    if(i == 0)
                        phoneNumber.add(0);
                    else if(i == 1) {
                        phoneNumber.add(9);
                    }
                    else
                        phoneNumber.add(random.nextInt(10));
                }
            }
            map.put(name, phoneNumber);

            for (int i = 0; i < 10; i++) {
                phoneNumString += Integer.toString(phoneNumber.get(i));
            }

            int livingState = random.nextInt(6) + 1;

            people[name] = new Person(Integer.toString(name));
            people[name].setePhoneNum(phoneNumString);
            people[name].setLivingPattern(livingState);
            phoneNumber = null;
            phoneNumString = null;
        }
        map = null;
        return;
    }

    public static void writeToXlsx(String filePath) throws IOException{
        File file = new File(filePath);
        SXSSFWorkbook wb = null;
        Sheet sheet = null;
        
        if (file.exists()) {
            IOException exception = new IOException("File Exists Exception");
            throw exception;
        }
        else {
            wb = new SXSSFWorkbook(numOfPeople);
            sheet      = wb.createSheet();
        }

        // (name, phone number, living pattern)
        ArrayList <Object[]> objectLists = new ArrayList<> (numOfPeople);
        for(int i=0; i<numOfPeople; i++) {
            Object[] objects = new Object[3];
            objects[0] = people[i].getName();
            objects[1] = people[i].getPhoneNum();
            objects[2] = people[i].getLivingPattern();
            objectLists.add(objects);
            objects = null;
        }
        System.out.println("Writing to xlsx file starts.");

        int rowNum = 0;
        int columnNum = 0;

        for (Object[] objects : objectLists) {
            Row row = sheet.createRow(rowNum++);
            columnNum = 0;

            for (Object object : objects) {
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
            wb.close();
        } catch (Exception e) {
            System.out.println("Write Error");
        }
        System.out.println("Writing to XLSX file Finished ...");
    }

    public static int getNumOfPeople() {
        return numOfPeople;
    }
}

class Person {
    private String name;
    private String phoneNum;
    private String [] time;
    /* Definiton of living pattern
    1 : 早八晚五型，目標相同
    2 : 早八晚五型，目標不同
    3 : 早八晚五型，無活動
    4 : 早八午十二，單目標
    5 : 午十二晚五，單目標
    6 : 活動時間與路徑相對隨機 (已完成)
    */
    private int livingPattern;
    private int [] placeCodes;
    private long [] positionCodes;
    private boolean [] positionBooleans;
    private int arrayLength;

    public Person(String name) {
        this.name = name;
    }
    public Person() {
        // Default constructor
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public int[] getPlaceCodes() {
        return placeCodes;
    }

    public void setPlaceCodes(int[] placeCodes) {
        this.placeCodes = placeCodes;
    }

    public boolean[] getPositionBooleans() {
        return positionBooleans;
    }
    public void setPositionBooleans(boolean[] positionBooleans) {
        this.positionBooleans = positionBooleans;
    }

    public long[] getPositionCodes() {
        return positionCodes;
    }
    public void setPositionCodes(long[] positionCodes) {
        this.positionCodes = positionCodes;
    }

    public int getLivingPattern(){ return livingPattern; }
    public void setLivingPattern(int livingPattern){ this.livingPattern = livingPattern; }

    public int getArrayLength(){ return arrayLength; }
    public void setArrayLength(int length){ this.arrayLength = length; }
    
}
