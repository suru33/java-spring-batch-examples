package com.suru.springbatch.xmlreader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class GenerateRandomData {
    public static void main(String[] args) {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        buffer.append("<customers>");
        for (int i = 1; i <= 1000; i++) {
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();
            buffer.append("<customer>");
            buffer.append("<id>");
            buffer.append(i);
            buffer.append("</id>");
            buffer.append("<firstName>");
            buffer.append(uuid1.toString().replaceAll("[0-9\\-]", ""));
            buffer.append("</firstName>");
            buffer.append("<lastName>");
            buffer.append(uuid2.toString().replaceAll("[0-9\\-]", ""));
            buffer.append("</lastName>");
            buffer.append("<random>");
            buffer.append(uuid2.getMostSignificantBits());
            buffer.append("</random>");
            buffer.append("</customer>");
        }
        buffer.append("</customers>");
        File file = new File(".\\src\\main\\resources\\data\\random_data.xml");
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
