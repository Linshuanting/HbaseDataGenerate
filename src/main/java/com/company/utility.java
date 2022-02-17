package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class utility {

    public BlockData[] readFromXlsx(String filePath) throws IOException {
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

}


class pair<T1, T2>{
    private  T1 first;
    private  T2 second;

    public pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst(){
        return first;
    }

    public T2 getSecond(){
        return second;
    }

    public void setFirst(T1 first){
        this.first = first;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }
}
