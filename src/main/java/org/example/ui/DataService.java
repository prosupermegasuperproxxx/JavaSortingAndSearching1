package org.example.ui;

import org.example.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

public class DataService<T> {

    //2.1 Заполнение из файла
    //4.3 Валидация
    //6. Заполнение коллекции в стриме
    public Person[] fillFromFile(String pathToFile) {
        try (Stream<String> lines = Files.lines(Paths.get(pathToFile))) {
            return lines
                    .map(line -> line.split(" ; "))
                    .map(parts ->{
                        String name = parts[0].trim();
                        int age = Integer.parseInt(parts[1].trim());
                        double salary = Double.parseDouble(parts[2].trim());

                        return Person.builder()
                                .name(name)
                                .age(age)
                                .salary(salary)
                                .build();

                    })
                    .toArray(Person[]::new);
        }catch (IOException e){
            System.err.println("File error" + e.getMessage());
            return new Person[0];
        }
    }

    //2.1 Заполнение вручную
    //4.3 Валидация данных
    public Person[] fillManually(int size){
        Scanner scanner = new Scanner(System.in);
        Person[] persons = new Person[size];

        for (int i = 0; i < size; i++) {
            System.out.print("Enter number of items in array: "+i+":");

            System.out.println("Name: ");
            String name = scanner.nextLine();

            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Salary: ");
            double salary = Double.parseDouble(scanner.nextLine());

            persons[i] = Person.builder()
                    .name(name)
                    .age(age)
                    .salary(salary)
                    .build();
        }
        return persons;
    }

    //2.1 Заполнение рандомно
    //4.3 Валидация данных
    public Person[] fillRandomly(int size) {
        Random random = new Random();
        Person[] persons = new Person[size];
        String[] names = new String[size];

        for (int i = 0; i < size; i++) {
            String name = names[random.nextInt(names.length)];
            int age = random.nextInt(100);
            double salary = random.nextDouble();

            persons[i] = Person.builder()
                    .name(name)
                    .age(age)
                    .salary(salary)
                    .build();
        }

        return persons;
    }
}
