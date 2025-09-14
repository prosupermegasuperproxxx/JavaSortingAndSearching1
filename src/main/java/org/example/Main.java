package org.example;

import org.example.customcollection.CustomList;
import org.example.model.Person;
import org.example.search.BinarySearch;
import org.example.sort.IterativeQuickSortStrategy;
import org.example.strategy.SortStrategy;
import org.example.ui.ConsoleUI;
import org.example.utils.FileUtil;
import org.example.utils.SortTask;
import org.example.utils.Threads;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Threads<?> sortExecutor;


    private static <T extends Comparable<T>> void performSortingOperations(List<T> list) {
        System.out.println("\n=== Операции сортировки ===");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Quick Sort");
        System.out.println("3. Custom Sort (even/odd)");
        System.out.println("0. Назад");


        //        int choice = getIntInput("Выберите алгоритм сортировки: ");
//
//        SortStrategy<T> strategy = null;
//        switch (choice) {
//            case 1 -> strategy = new BubbleSort<>();
//            case 2 -> strategy = new QuickSort<>();
//            case 3 -> {
//                String fieldName = getStringInput("Введите имя поля для custom sort: ");
//                strategy = new CustomSort<>(fieldName);
//            }
//            case 0 -> { return; }
//            default -> {
//                System.out.println("Неверный выбор.");
//                return;
//            }
//        }

        // Фоновый поток. может и не нужен
        sortExecutor = new Threads<>();

//        SortStrategy<T> strategy = new BubbleSortStrategy<>();
        SortStrategy<T> strategy = new IterativeQuickSortStrategy<>();
        strategy.setComparator((Comparator<T>) new Person.NameComparator());
//        strategy.setComparator((Comparator<T>) new Person.SalaryComparator());



        try {
            System.out.println("Начало сортировки...");
            List<T> sortedList;
            if(!false) {
                Future<List<T>> future = sortExecutor.executeSort(list, strategy);
                sortedList = future.get();
            }
            else
                sortedList = new SortTask<>(list, strategy).call();
                                           
//            System.out.println("Отсортированный список: " + sortedList);
            System.out.println("Сортировка завершена");
            System.out.println("Использован алгоритм: " + strategy.getStrategyName());

            Person target = new Person.PersonBuilder().age(219).name("Andrew").salary(2576).build();

            BinarySearch<Person> binarySearch = new BinarySearch<>();

            //  чтоб бинарный поиск работал, искать надо тем же компаратором, что и сортировка была 
            int index = binarySearch.binarySearch((List<Person>) sortedList, target, (Comparator< Person>) strategy.getComparator());

            if (index >= 0) {
                System.out.println("Person found at index: " + index);
            } else {
                System.out.println("Person not found.");
            }

            // Запись в файл (Доп задание 2)
//            if (getBooleanInput("Записать результат в файл? (y/n): ")) {
//                String filename = getStringInput("Введите имя файла: ");
//                FileUtil.writeToFile(filename, sortedList, true);
//                System.out.println("Результат записан в файл: " + filename);
//            }

        } catch (Exception e) {
            System.out.println("Ошибка при сортировке: " + e.getMessage());
        }
    }

    private static <T extends Comparable<T>> void performSearchOperations(List<T> list) {
        
        System.out.println("\n=== Операции поиска ===");
        if (list.isEmpty()) {
            System.out.println("Список пуст. Сначала выполните сортировку.");
            return;
        }

        BinarySearch<T> binarySearch = new BinarySearch<>();
        Person searchKey = createSearchKey( );

        if (searchKey != null) {
            int index = binarySearch.binarySearch(list, (T) searchKey, (o1, o2) -> o1.compareTo(o2));
            if (index != -1) {
                System.out.println("Элемент найден на позиции: " + index);
                System.out.println("Элемент: " + list.get(index));

                // Запись в файл (Дополнительное задание 2)
                if (getBooleanInput("Записать найденный элемент в файл? (y/n): ")) {
                    String filename = getStringInput("Введите имя файла: ");
                    try {
//                      **Дополнительное задание 2:** реализовать функционал для записи отсортированных 
//                      коллекций/ найденных значений в файл в режиме добавления данных.  
                        FileUtil.writeToFile(filename, list.get(index), true);
                        System.out.println("Элемент записан в файл: " + filename);
                    } catch (Exception e) {
                        System.out.println("Ошибка записи в файл: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Элемент не найден.");
            }
        }
    }

    private static boolean getBooleanInput(String s) {
        return true;
    }

    private static <T> T createSearchKey() {
        try {
                System.out.println("Введите данные для поиска Person:");
                String name = getStringInput("Имя: ");
                int age = getIntInput("Возраст: ");
                double salary = getDoubleInput("Зарплата: ");

                return (T) Person.builder()
                        .name(name)
                        .age(age)
                        .salary(salary)
                        .build();
        } catch (Exception e) {
            System.out.println("Ошибка создания ключа поиска: " + e.getMessage());
        }
        return null;
    }

    private static int getIntInput(String s) {
        return 0;
    }

    private static double getDoubleInput(String s) {
        return 0;
    }

    private static String getStringInput(String s) {
        return "null";
    }

    public static void main(String[] args) throws IOException {

        System.out.println( 
            System.getProperty("user.dir")
        );
        // пример заполнения коллекции из файла перенести в UI
 //       CustomList<Person> customList = FileUtil.readPersonsFromFile("persons.txt");
//        performSortingOperations(customList.toList());
//        performSearchOperations(customList.toList());
        
        ConsoleUI c = new ConsoleUI();
        c.start();


        if (sortExecutor != null) {
            sortExecutor.shutdown();
        }
    }
}