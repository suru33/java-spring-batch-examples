package com.suru.springbatch.flatfilereader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class GenerateRandomData {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("id,firstName,lastName,random\n");
        for (int i = 1; i <= 1000; i++) {
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();
            buffer.append(i);
            buffer.append(',');
            buffer.append(uuid1.toString().replaceAll("[0-9\\-]", ""));
            buffer.append(',');
            buffer.append(uuid2.toString().replaceAll("[0-9\\-]", ""));
            buffer.append(',');
            buffer.append(uuid2.getMostSignificantBits());
            buffer.append('\n');
        }
        File file = new File(".\\src\\main\\resources\\data\\random_data.csv");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(buffer.toString());
            fileWriter.close();
            System.out.println("Done! writing random file....");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
