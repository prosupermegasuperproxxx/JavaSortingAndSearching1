package org.example.utils;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil {

    public static <T> void writeCollectionToFile(List<T> collection, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T item : collection) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }
}