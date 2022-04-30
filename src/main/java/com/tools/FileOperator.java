package com.tools;

import org.apache.hadoop.io.Writable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileOperator {

    /*
    Put filename & string array datas, this function can create a 'filename.txt' file and
    put datas in file in same file folder
     */
    public void writeTotxt(String filename, String[][] datas){

        // judge whether or not adding ".txt" sub-filename
        if (filename.length() <= 4 || !filename.startsWith(".txt", filename.length()-4))
            filename = filename + ".txt";

        Charset utf8 = StandardCharsets.UTF_8;

        try{
            File myFile = new File(filename);
            FileOutputStream fos = new FileOutputStream(myFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Writer writer = new BufferedWriter(osw);

            int i = 0;
            for (String[] data: datas){

                for (String item: data) {

                    if (i < 10)
                        writer.write(item);
                    else {
                        writer.write(data + "\n");
                        i = 0;
                    }
                }
                writer.write("\n");
                i = 0;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: FileNotFoundException happened!!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: FileNotFoundException happened!!");
            e.printStackTrace();
        }
    }


}
