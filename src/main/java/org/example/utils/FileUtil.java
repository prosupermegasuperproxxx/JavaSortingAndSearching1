package org.example.utils;

import org.example.customcollection.CustomList;
import org.example.model.Person;

import java.io.*;
import java.util.stream.Collectors;

public class FileUtil {
    /*public static CustomList<Person> readPersonsFromFile(String filename) throws IOException  {
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
    }*/
    public static CustomList<Person> readPersonsFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == Person.COUNT_COLUMNS)
                    .map(parts -> Person.builder()
                            .name(parts[0].trim())
                            .age(Integer.parseInt(parts[1].trim()))
                            .salary(Double.parseDouble(parts[2].trim()))
                            .build())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                CustomList<Person> customList = new CustomList<>();
                                customList.addAll(list);
                                return customList;
                            }
                    ));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in file", e);
        }
    }

    /*private static CustomList<Person> createManualPersons(int count) {
        CustomList<Person> persons = CustomList.fromStream(
                java.util.stream.Stream.generate(() -> {
                    System.out.println("\nВведите данные Person:");
                    String name = getStringInput("Имя: ");
                    int age = getIntInput("Возраст: ");
                    double salary = getDoubleInput("Зарплата: ");

                    return Person.builder()
                            .name(name)
                            .age(age)
                            .salary(salary)
                            .build();
                }).limit(count)
        );
        System.out.println("Созданы Persons: " + persons);
        return persons;
    }*/
    
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