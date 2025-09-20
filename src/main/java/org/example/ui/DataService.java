package org.example.ui;

import org.example.customcollection.CustomList;
import org.example.model.Person;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

public class DataService<T> {

    private static final String SEPARATOR = ",";

    //2.1 Заполнение из файла
    //4.3 Валидация
    //6. Заполнение коллекции в стриме

    //Доделать под коллекцию
    public CustomList<Person> fillFromFile(String pathToFile) {
        try (Stream<String> lines = Files.lines(Paths.get(pathToFile))) {

            return CustomList.fromStream(
                    lines
                            .map(line -> line.split(SEPARATOR))
                            .filter(line -> line.length == Person.COUNT_COLUMNS)
                            .map(parts -> Person.builder()
                                    .name(parts[0].trim())
                                    .age(Integer.parseInt(parts[1].trim()))
                                    .salary(Double.parseDouble(parts[2].trim()))
                                    .build())
            );

        } catch (Exception e) {
            System.err.println("Файл с ошибками: " + e.getMessage());
            System.err.println("Выберите другой файл.");
            return new CustomList<>();
        }

    }


    //2.1 Заполнение вручную
    //4.3 Валидация данных
    //6. Заполнение коллекции в стриме

    //доработана для соединения, используется колекция не массив
    public CustomList<Person> fillManually(int size) {
        Scanner scanner = new Scanner(System.in);

        List<Person> personList = new ArrayList<>();

        while (size > 0) {
            try {
                System.out.println("Name: ");
                String name = scanner.nextLine();
                if (name.trim().isEmpty()) throw new Exception("Имя не может быть пустым");

                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine());

                System.out.print("Salary: ");
                double salary = Double.parseDouble(scanner.nextLine());

                personList.add(
                        Person.builder()
                                .name(name)
                                .age(age)
                                .salary(salary)
                                .build());

                size--;
            } catch (Exception e) {
                System.out.println("Введите корректные данные: " + e.getMessage());

                System.out.println("Вы хотите продолжить? (N) для остановки");
                String s = scanner.nextLine();
                if (s.equalsIgnoreCase("N")) {
                    break;
                }
            }
        }

        return CustomList.fromStream(personList.stream());
    }

    //2.1 Заполнение рандомно
    //4.3 Валидация данных
    public CustomList<Person> fillRandomly(int size) {
        Random random = new Random();
        String[] names = {"Артем", "Андрей", "Аслан", "Анна"};

        CustomList<Person> persons = CustomList.fromStream(
                Stream.generate(() -> {

                    String name = names[random.nextInt(names.length)];
                    int age = random.nextInt(47) + 18;
                    double salary = random.nextDouble() * 1000;

                    Person built = Person.builder()
                            .name(name)
                            .age(age)
                            .salary(salary)
                            .build();

                    return built;
                }).limit(size)
        );

        return persons;
    }
}