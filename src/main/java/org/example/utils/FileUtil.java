package org.example.utils;

import org.example.customcollection.CustomList;
import org.example.model.Person;

import java.io.*;

public class FileUtil {
    public static CustomList<Person> readPersonsFromFile(String filename) throws IOException  {
        CustomList<Person> persons = new CustomList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == Person.COUNT_COLUMNS) {
                    Person person = Person.builder()
                            .name(parts[0].trim())
                            .age(Integer.parseInt(parts[1].trim()))
                            .salary(Double.parseDouble(parts[2].trim()))
                            .build();
                    persons.add(person);
                } else
                    throw new IllegalArgumentException("File with broken data. Incorrect count columns");

            }
        }
        return persons;
    }

    public static <T> void writeToFile(String filename, CustomList<T> list, boolean append) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, append))) {
            for (T item : list) {
                writer.println(item.toString());
            }
        }
    }

    public static <T> void writeToFile(String filename, T item, boolean append) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, append))) {
            writer.println(item.toString());
        }
    }
}