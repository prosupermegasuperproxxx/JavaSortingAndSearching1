package org.example.ui;

import org.example.customcollection.CustomList;
import org.example.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.Main.*;

public class DataService<T> {

    //2.1 Заполнение из файла
    //4.3 Валидация
    //6. Заполнение коллекции в стриме

    //Доделать под коллекцию
    public CustomList<Person> fillFromFile(String pathToFile) {
//        CustomList<Person> customList = FileUtil.readPersonsFromFile("persons.txt");
        try (Stream<String> lines = Files.lines(Paths.get(pathToFile))) {
            return (CustomList<Person>) lines
                    .map(line -> line.split(","))
                    .map(parts ->{
                        String name = parts[0].trim();
                        int age = Integer.parseInt(parts[1].trim());
                        double salary = Double.parseDouble(parts[2].trim());

                        return Person.builder()
                                .name(name)
                                .age(age)
                                .salary(salary)
                                .build();

                    }).collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                CustomList<Person> customList = new CustomList<>();
                                customList.addAll(list);
                                return customList;
                            }
                    ));

                    //.(Person[]::new);
        }catch (IOException e){
            System.err.println("File error" + e.getMessage());
            return new CustomList<>();
        }
    }

    //2.1 Заполнение вручную
    //4.3 Валидация данных
    //public Person[] fillManually(int size){
    //доработана для соединения, используется колекция не массив
    public CustomList<Person> fillManually(int size){
        Scanner scanner = new Scanner(System.in);
//        Person[] persons = new Person[size];
//
//        for (int i = 0; i < size; i++) {
//            System.out.print("Enter number of items in array: "+i+":");
//
//            System.out.println("Name: ");
//            String name = scanner.nextLine();
//
//            System.out.print("Age: ");
//            int age = Integer.parseInt(scanner.nextLine());
//
//            System.out.print("Salary: ");
//            double salary = Double.parseDouble(scanner.nextLine());
//
//            persons[i] = Person.builder()
//                    .name(name)
//                    .age(age)
//                    .salary(salary)
//                    .build();
//        }
//        return persons;
        CustomList<Person> persons = CustomList.fromStream(
                java.util.stream.Stream.generate(() -> {
                    System.out.println("\nВведите данные Person:");

//                    String name = getStringInput("Имя: ");
//                    int age = getIntInput("Возраст: ");
//                    double salary = getDoubleInput("Зарплата: ");
                    System.out.println("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Age: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    System.out.print("Salary: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    return Person.builder()
                            .name(name)
                            .age(age)
                            .salary(salary)
                            .build();
                }).limit(size)
        );
        System.out.println("Созданы Persons: " + persons);
        return persons;
    }

    //2.1 Заполнение рандомно
    //4.3 Валидация данных
    public CustomList<Person>  fillRandomly(int size) {
        Random random = new Random();
        // persons = new Person[size];
        CustomList<Person> persons = new CustomList<>();
        String[] names = {"Артем", "Андрей", "Аслан", "Анна"};

        for (int i = 0; i < size; i++) {

            String name = names[random.nextInt(names.length)];
            int age = random.nextInt(100)+ 1;
            double salary = random.nextDouble() + 1;

            persons.add(Person.builder().name(name)
                    .age(age)
                    .salary(salary)
                    .build()) ;
        }

        return persons;
    }
}
