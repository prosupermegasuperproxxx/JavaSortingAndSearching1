package org.example.ui;


import org.example.counterN.CountNStrategy;
import org.example.counterN.CounterN;
import org.example.customcollection.CustomList;
import org.example.model.Person;
import org.example.search.SearchService;
import org.example.sort.BubbleSortStrategy;
import org.example.sort.IterativeQuickSortStrategy;
import org.example.sort.MergeSortStrategy;
import org.example.sort.SortingService;
import org.example.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public class ConsoleUI {
    private final Scanner scanner;
    private final DataService dataService;
    private final SortingService sortingService;
    private final SearchService searchService;
    private List<Person> dataArray;
    private SortStrategy<Person> strategy = null;
    private boolean isSorted = false;

    private final CounterN counterN;
    private ExecutorService executor;


    public ConsoleUI () {
        this.scanner = new Scanner(System.in);
        this.dataService = new DataService();
        this.sortingService = new SortingService();
        this.searchService = new SearchService();

        this.counterN=new CounterN();
        this.executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    //2.1 Вопрос(из файла/вручную/рандомно)
    //2.6 Выход по соответствующему слову
    //2.7 Вопрос о желании найти элемент из консоли

    public void start(){
        boolean isRunning = true;

        boolean dataArrayIsOK = false;

        if(dataArray == null||dataArray.isEmpty()){
            System.out.println("Для начала работы необходимо заполнить массив данных");
            dataArrayIsOK = handleDataInput();
        }

        while(isRunning){

            String choice = null;
            if(dataArrayIsOK) {
                printMainMenu();
                choice = scanner.nextLine().trim();
            } else {
                System.out.println("Для начала работы необходимо заполнить массив данных");
                choice = "1";
            }

            switch (choice){
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

    private boolean isArrayRead(){
        if(dataArray==null||dataArray.isEmpty()){
            System.out.println("Массив пуст. Сначала заполните массив (выберите пункт 1).");
            return false;
        }
        return true;
    }

    //2.1 интерфейс меню
    private void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Заполнение массива");
        System.out.println("2. Сортировка");
        if(isSorted)
            System.out.println("3. Найти элемент");
        System.out.println("4. Вывести в консоль");
        System.out.println("5. Записать в файл");

        System.out.println("6. Подсчитать количество вхождений элемента N. ");


        System.out.println("Выход 'exit'");
        System.out.println("Введите ваш выбор:");
    }

    //2.1 выбор варианта заполнения исходного массива данных (из файла, рандом, вручную)
    //2.5 вопрос о размерности
    private boolean handleDataInput () {
        System.out.println("\n--- Способ ввода данных ---");
        System.out.println("1. Вручную");
        System.out.println("2. Случайно");
        System.out.println("3. Из файла");
        System.out.println("Ваш выбор:");

        String inputChoice = scanner.nextLine().trim();
        int size = 0;
        isSorted = false;

        if(!inputChoice.trim().isEmpty())
            if(!inputChoice.equals("3")){
                System.out.println("Введите размер");
                size = Integer.parseInt(scanner.nextLine());
            }

        switch(inputChoice){
            case "1":
                this.dataArray = dataService.fillManually(size).toList();
                break;
            case "2":
                this.dataArray = dataService.fillRandomly(size).toList();
                break;
            case "3":
                String[] defFile = {"persons.txt", "evensorttest.txt"};
                String seldefFile = defFile[0];
                System.out.println("Введите имя файла и оставьте пустое. Будет использован файл " + seldefFile);
                
                String pathToFile = scanner.nextLine();
                if(pathToFile.trim().isEmpty()){
                    pathToFile = seldefFile;
                }
                this.dataArray = dataService.fillFromFile(pathToFile).toList();
                break;
            default:
                System.out.println("Invalid choice.");
                

        }

        if(dataArray != null){
            System.out.println("The array has been filled. Size: " + dataArray.size());
        }
        
        boolean ret = dataArray==null;
        if(ret)
            return false;

        return !this.dataArray.isEmpty();
        
    }

    //2.7 вопрос о желании найти элемент из консоли
    private void handleSearch() {
        if(dataArray == null){
            System.out.println("Массив пустой введите что нибудь.");
            return;
        }
        if(!isSorted){
            System.out.println("Массив не отсортирован. Сначала отсортируй");
            return;
        }
        String strategyComparatorString = "Name";
        
        String tipDouble = "";
        Comparator<Person> strategyComparator = strategy.getComparator();

        
        if(strategyComparator==null || strategyComparator.getClass()==Person.NameComparator.class)
            strategyComparatorString = "Name";
        else if(strategyComparator.getClass()==Person.AgeComparator.class)
            strategyComparatorString = "Age";
        else if(strategyComparator.getClass()==Person.SalaryComparator.class) {
            strategyComparatorString = "Salary";
            tipDouble = ". Округлено до знаков: " + Person.DOUBLE_AFTERDOT;
        }
         
        System.out.println("Отcортировано по полю: " + strategyComparatorString + tipDouble);

        Person.PersonBuilder targetTemp = new Person.PersonBuilder().age(1).name("A").salary(1);

        ok:
        while(true){
            System.out.println("Введите искомое значение");
            String temp = scanner.nextLine();
            try {
                switch(strategyComparatorString){
                    case "Name" :
                        if (temp == null || temp.trim().isEmpty()) {
                            throw new Exception();
                        }
                        targetTemp.name(temp);
                        break ok;

                    case "Age" :
                        targetTemp.age(Integer.parseInt(temp));
                        break ok;
                        
                    case "Salary" :
                        targetTemp.salary(Double.parseDouble(temp));
                        break ok;
                }
            } catch (Exception e) {
                System.err.println("Введите КОРРЕКТНОЕ искомое значение");
            }
        }

        SearchService.searchKey(dataArray, strategyComparator, targetTemp.build());
        
    }

    //2.3 вывод результат, если выбрали консоль
    private void printArray() {
        if(dataArray == null||dataArray.size() == 0){
            System.out.println("Array is empty. Please fill up the data.");
            return;
        }

        System.out.println("\n---Array contents---");
        for (int i = 0; i < dataArray.size(); i++){
            System.out.println("index: "+ i + " | " + dataArray.get(i));
        }

    }

    //5. запись отсортированной коллекции в файл
    private void handleSaveToFile() {
        System.out.print("Введите имя файла для сохранения: ");
        String filename = scanner.nextLine();

        // Если ваш SortingManager параметризован, нужно привести тип
        if (sortingService instanceof SortingService) {
            @SuppressWarnings("unchecked")
            SortingService manager = (SortingService) sortingService;
            manager.saveSortedCollectionToFile(filename, dataArray);

        }
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
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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

        switch (fieldChoice){
            case "1":
                strategy = new BubbleSortStrategy<>();
                break;
            case "2":
                strategy = new IterativeQuickSortStrategy<>();
                break;
            case "3":
                strategy = new MergeSortStrategy<>();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if(dataArray == null){
            System.out.println("Пожалуйста введите размер массива");
            return;
        }

        System.out.println("Выберите поле сортировки:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Salary");
        System.out.println("4. All Fields. Name->Age->Salary");
        System.out.println("5. Age Even Only");
        System.out.println("Выберите: ");

        fieldChoice = scanner.nextLine();

        java.util.function.Function<Person, Number> getEvenValue = null;
        switch (fieldChoice){
            case "1":
                strategy.setComparator( new Person.NameComparator());
                break;
            case "2":
                strategy.setComparator( new Person.AgeComparator());
                break;
            case "3":
                strategy.setComparator( new Person.SalaryComparator());
                break;
            case "4":
               // сортирует по всем полям;
                strategy.setComparator( null);
                break;
            case "5":
                strategy.setComparator( new Person.AgeComparator());
                getEvenValue = (p) -> p.getAge();
                System.out.println("Возраст только с чётным значением отсортирован");
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }


        System.out.println("Не сортированный список размер: " + dataArray.size());

        if(sortingService.sortArray(dataArray, strategy, getEvenValue)!=null)
            isSorted = true;
//        бинарный поиск с "5. Age Even Only" сортировкой не сработает
        if(getEvenValue!=null) isSorted = false;

        System.out.println("Отсортированный список размер : " + dataArray.size());

        System.out.println("The sorted array is: ");
        printArray();
    }
}
