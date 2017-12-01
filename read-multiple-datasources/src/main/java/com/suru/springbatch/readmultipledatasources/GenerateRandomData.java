package com.suru.springbatch.readmultipledatasources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class GenerateRandomData {
    public static void main(String[] args) {
        StringBuffer buffers[] = new StringBuffer[5];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new StringBuffer("id,firstName,lastName,random\n");
        }
        for (int i = 1; i <= 1000; i++) {
            for (int j = 0; j < buffers.length; j++) {
                UUID uuid1 = UUID.randomUUID();
                UUID uuid2 = UUID.randomUUID();
                buffers[j].append((j + 1) + "-" + i);
                buffers[j].append(',');
                buffers[j].append(uuid1.toString().replaceAll("[0-9\\-]", ""));
                buffers[j].append(',');
                buffers[j].append(uuid2.toString().replaceAll("[0-9\\-]", ""));
                buffers[j].append(',');
                buffers[j].append(uuid2.getLeastSignificantBits());
                buffers[j].append('\n');
            }
        }
        for (int i = 0; i < buffers.length; i++) {
            File file = new File(".\\src\\main\\resources\\data\\random_data_" + (i + 1) + ".csv");
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(buffers[i].toString());
                fileWriter.close();
                System.out.println("Done! writing random file...:" + i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
