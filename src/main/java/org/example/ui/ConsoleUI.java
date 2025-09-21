package org.example.ui;


import org.example.counterN.CountNStrategy;
import org.example.counterN.CounterN;
import org.example.model.Person;
import org.example.sort.BubbleSortStrategy;
import org.example.sort.IterativeQuickSortStrategy;
import org.example.sort.MergeSortStrategy;
import org.example.sort.SortingService;
import org.example.strategy.SortStrategy;
import org.example.utils.ServisMenu;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;


public class ConsoleUI {
    private final Scanner scanner;
    private final DataService<Person> dataService;

    private List<Person> dataArray;

    private boolean isSorted = false;

    private final CounterN counterN;
    private final ExecutorService executor;


    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.dataService = new DataService<>();

        this.counterN = new CounterN();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    //2.1 Вопрос(из файла/вручную/рандомно)
    //2.6 Выход по соответствующему слову
    //2.7 Вопрос о желании найти элемент из консоли

    public void start() {
        boolean isRunning = true;

        boolean dataArrayIsOK = false;

        if (dataArray == null || dataArray.isEmpty()) {
            System.out.println("Для начала работы необходимо заполнить массив данных");
            dataArrayIsOK = handleDataInput();
        }

        while (isRunning) {

            String choice = null;
            if (dataArrayIsOK) {
                printMainMenu();
                choice = scanner.nextLine().trim();
            } else {
                System.out.println("Для начала работы необходимо заполнить массив данных");
                choice = "1";
            }

            switch (choice) {
                case "1":
                    dataArrayIsOK = handleDataInput();//выбор способа заполнения
                    break;
                case "2":
                    handleSorting();//сортировка
                    break;
                case "3":
                    handleSearch();//поиск элемента
                    break;
                case "4":
                    printArray();//2.3 вывод результата, если выбрали консоль
                    break;
                case "5":
                    handleSaveToFile();//5.запись файла
                    break;
                case "6":
                    //6. подсчет и вывод в консоль элемента N
                    performAndShowCounting();
                    break;
                case "exit"://2.6 выход по слову "exit"
                    isRunning = false;
                    executor.shutdown();
                    System.out.println("Пока!");
                    break;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
        scanner.close();
    }


    //2.1 интерфейс меню
    private void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Заполнение массива");
        System.out.println("2. Сортировка");
        if (isSorted)
            System.out.println("3. Найти элемент");
        System.out.println("4. Вывести в консоль");
        System.out.println("5. Записать в файл");

        System.out.println("6. Подсчитать количество вхождений элемента N. ");


        System.out.println("Выход 'exit'");
        System.out.println("Введите ваш выбор:");
    }

    //2.1 выбор варианта заполнения исходного массива данных (из файла, рандом, вручную)
    //2.5 вопрос о размерности
    private boolean handleDataInput() {
        System.out.println("\n--- Способ ввода данных ---");
        System.out.println("1. Вручную");
        System.out.println("2. Случайно");
        System.out.println("3. Из файла");
        System.out.println("Ваш выбор:");

        String inputChoice = scanner.nextLine().trim();
        int size = 0;
        isSorted = false;

        if (inputChoice.equals("1") || inputChoice.equals("2")) {
            System.out.println("Введите размер");
            size = getIntInput();
        }

        dataArray = ServisMenu.choiceArr(inputChoice, size, scanner);


        if (dataArray != null) {
            System.out.println("Массив заполнен. Размер: " + dataArray.size());

        }

        return dataArray != null && (!this.dataArray.isEmpty());

    }

    //2.7 вопрос о желании найти элемент из консоли
    private void handleSearch() {
        if (dataArray == null) {
            System.out.println("Массив пустой введите что нибудь.");
            return;
        }
        if (!isSorted) {
            System.out.println("Массив не отсортирован. Сначала отсортируй");
            return;
        }
        ServisMenu.handleSearchServ(dataArray, scanner);


    }


    //2.3 вывод результат, если выбрали консоль
    private void printArray() {
        if (dataArray == null || dataArray.size() == 0) {
            System.out.println("Массив пустой, заполните пожалуйста.");
            return;
        }

        System.out.println("\n---Содержимое массива---");
        for (int i = 0; i < dataArray.size(); i++) {
            System.out.println("index: " + i + " | " + dataArray.get(i));
        }

    }

    //5. запись отсортированной коллекции в файл
    private void handleSaveToFile() {
        System.out.print("Введите имя файла для сохранения: ");
        String filename = scanner.nextLine();

        SortingService.saveSortedCollectionToFile(filename, dataArray);
    }

    //6. подсчет элемента N
    private void performAndShowCounting() {
        System.out.println("\n=== Выбор параметра для поиска ===");
        System.out.println("1. По имени (String)");
        System.out.println("2. По возрасту (int)");
        System.out.println("3. По зарплате (double)");
        System.out.print("Выберите параметр: ");

        int paramChoice = getIntInput();

        Function<Person, ?> fieldExtractor;
        Object targetValue;

        switch (paramChoice) {
            case 1:
                System.out.print("Введите имя для поиска: ");
                targetValue = scanner.nextLine();
                fieldExtractor = Person::getName;
                break;

            case 2:
                System.out.print("Введите возраст для поиска: ");
                targetValue = getIntInput();
                fieldExtractor = Person::getAge;
                break;

            case 3:
                System.out.print("Введите зарплату для поиска: ");
                targetValue = getDoubleInput();
                fieldExtractor = Person::getSalary;
                break;

            default:
                System.out.println("Неверный выбор параметра");
                return;
        }

        CountNStrategy countingStrategy =
                new CountNStrategy(counterN, targetValue, fieldExtractor);

        countingStrategy.setExecutor(executor);
        countingStrategy.sort(dataArray);

        // Ждем завершения подсчета
        // Сразу показываем результат
        long count = counterN.getCount();
        System.out.println("Количество вхождений: " + count);
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }

    //2.4 проверка на поле для сортировки
    private void handleSorting() {

        System.out.println("Выберите сортировку");
        System.out.println("1. Пузырьковая");
        System.out.println("2. Быстрая");
        System.out.println("3. Слияние");
        System.out.println("Выберите: ");


        String fieldChoice = scanner.nextLine();
        ServisMenu.handleSortingStrategia(fieldChoice);


        if (dataArray == null) {
            System.out.println("Пожалуйста введите размер массива");
            return;
        }

        System.out.println("Выберите поле сортировки:");
        System.out.println("1. Имя");
        System.out.println("2. Возраст");
        System.out.println("3. Зарплата");
        System.out.println("4. Все поля");
        System.out.println("5. Возраст только четные");
        System.out.println("Выберите: ");

        fieldChoice = scanner.nextLine();
        isSorted = ServisMenu.handleSortServ(fieldChoice, dataArray);

        printArray();
    }
}