package org.example.ui;

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

public class ConsoleUI {
    private final Scanner scanner;
    private final DataService dataService;
    private final SortingService sortingService;
    private final SearchService searchService;
    private List<Person> dataArray;

    public ConsoleUI () {
        this.scanner = new Scanner(System.in);
        this.dataService = new DataService();
        this.sortingService = new SortingService();
        this.searchService = new SearchService();
    }

    //2.1 Вопрос(из файла/вручную/рандомно)
    //2.6 Выход по соответствующему слову
    //2.7 Вопрос о желании найти элемент из консоли

    public void start(){
        boolean isRunning = true;
        while(isRunning){
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1":
                    handleDataInput();//выбор способа заполнения
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
                    writeToFile();//5.запись файла
                    break;
                case "exit"://2.6 выход по слову "exit"
                    isRunning = false;
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    //2.1 интерфейс меню
    private void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Заполнение массива");
        System.out.println("2. Сортировка");
        System.out.println("3. Найти элемент");
        System.out.println("4. Вывести в консоль");
        System.out.println("5. Записать в файл");
        System.out.println("Выход 'exit'");
        System.out.println("Введите ваш выбор:");
    }

    //2.1 выбор варианта заполнения исходного массива данных (из файла, рандом, вручную)
    //2.5 вопрос о размерности
    private void handleDataInput () {
        System.out.println("\n--- Datainpit ---");
        System.out.println("1. Вручную");
        System.out.println("2. Случайно");
        System.out.println("3. Из файла");
        System.out.println("Ваш выбор:");

        String inputChoice = scanner.nextLine().trim();
        int size = 0;

        if(!inputChoice.equals("3")){
            System.out.println("Enter the size of the array");
            size = Integer.parseInt(scanner.nextLine());
        }

        switch(inputChoice){
            case "1":
                dataService.fillManually(size);
                break;
            case "2":
                dataService.fillRandomly(size);
                break;
            case "3":
                System.out.println("Enter the path to the file");
                String pathToFile = scanner.nextLine();
                this.dataArray = dataService.fillFromFile(pathToFile).toList();
                break;
            default:
                System.out.println("Invalid choice.");
        }

        if(dataArray != null){
            System.out.println("The array has been filled. Size: " + dataArray.size());
        }
    }

    //2.7 вопрос о желании найти элемент из консоли
    private void handleSearch() {
        if(dataArray == null){
            System.out.println("Массив пустой введите что нибудь.");
            return;
        }
        System.out.println("Введите элемент для поиска: ");
        String key = scanner.nextLine();
    }

    //2.3 вывод результат, если выбрали консоль
    private void printArray() {
        if(dataArray == null||dataArray.size() == 0){
            System.out.println("Array is empty. Please fill up the data.");
            return;
        }

        System.out.println("\n---Array contents---");
        for (int i = 0; i < dataArray.size(); i++){
            System.out.println(dataArray.get(i));
        }

    }

    //5. запись отсортированной коллекции в файл
    private void writeToFile() {
        if(dataArray == null){
            System.out.println("Array is empty. Please fill up the data.");
            return;
        }
        System.out.println("Write to file functionality not implemented  yet");

    }

    //2.4 проверка на поле для сортировки
    private void handleSorting() {

        System.out.println("Выберите сортировку");
        System.out.println("1. Пузырьковая");
        System.out.println("2. Быстрая");
        System.out.println("3. Слияние");
        System.out.println("4. По четным");
        System.out.println("Выберите: ");
        SortStrategy<Person> strategy = null;


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
            case "4":
                // пока нет доделать
                strategy = null;
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
        System.out.println("4. All Fields");
        System.out.println("Выберите: ");

        fieldChoice = scanner.nextLine();
        Comparator<Person> comparator = null;

        switch (fieldChoice){
            case "1":
                //comparator = Comparator.comparing(Person::getAge);
                strategy.setComparator( new Person.NameComparator());
                break;
            case "2":
                //comparator = Comparator.comparing(Person::getName);
                strategy.setComparator( new Person.AgeComparator());
                break;
            case "3":
                //comparator = Comparator.comparing(Person::getSalary);
                strategy.setComparator( new Person.SalaryComparator());
                break;
            case "4":
               // comparator = null;
                strategy.setComparator( null);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }



        sortingService.sortArray(dataArray, comparator, strategy);
        System.out.println("The sorted array is: ");
        printArray();
    }
}
