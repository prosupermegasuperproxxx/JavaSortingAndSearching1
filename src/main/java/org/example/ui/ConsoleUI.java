package org.example.ui;

import org.example.model.Person;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;

public class ConsoleUI {
    private final Scanner scanner;
    private final DataService dataService;
    private final SortingService sortingService;
    private final SearchService searchService;
    private Person[] dataArray;

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
        System.out.println("1. Display all data");
        System.out.println("2. Sorting");
        System.out.println("3. Searching");
        System.out.println("4. Output an array to the console");
        System.out.println("5. Write an array to a fail");
        System.out.println("To exit, enter 'exit'");
        System.out.println("Enter choice");
    }

    //2.1 выбор варианта заполнения исходного массива данных (из файла, рандом, вручную)
    //2.5 вопрос о размерности
    private void handleDataInput () {
        System.out.println("\n--- Datainpit ---");
        System.out.println("1. Manually");
        System.out.println("2. Random");
        System.out.println("3. From file");
        System.out.println("Enter choice");

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
                this.dataArray = dataService.fillFromFile(pathToFile);
                break;
            default:
                System.out.println("Invalid choice.");
        }

        if(dataArray != null){
            System.out.println("The array has been filled. Size: " + dataArray.length);
        }
    }

    //2.7 вопрос о желании найти элемент из консоли
    private void handleSearch() {
        if(dataArray == null){
            System.out.println("Array is empty. Please fill up the data.");
            return;
        }
        System.out.println("Enter the search term: ");
        String key = scanner.nextLine();
    }

    //2.3 вывод результат, если выбрали консоль
    private void printArray() {
        if(dataArray == null||dataArray.length == 0){
            System.out.println("Array is empty. Please fill up the data.");
            return;
        }

        System.out.println("\n---Array contents---");
        for (int i = 0; i < dataArray.length; i++){
            System.out.println(dataArray[i]);
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
        if(dataArray == null){
            System.out.println("Please enter the size of the array");
            return;
        }

        System.out.println("Select the field to sort:");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. According to the assessment");
        System.out.println("Your choise: ");

        String fieldChoice = scanner.nextLine();
        Comparator<Person> comparator = null;

        switch (fieldChoice){
            case "1":
                comparator = Comparator.comparing(Person::getAge);
                break;
            case "2":
                comparator = Comparator.comparing(Person::getName);
                break;
            case "3":
                comparator = Comparator.comparing(Person::getSalary);
            default:
                System.out.println("Invalid choice.");
                return;
        }

        sortingService.sortArray(dataArray, comparator);
        System.out.println("The sorted array is: ");
        printArray();
    }
}
