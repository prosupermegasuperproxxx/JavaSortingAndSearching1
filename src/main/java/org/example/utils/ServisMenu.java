package org.example.utils;

import org.example.model.Person;
import org.example.search.SearchService;
import org.example.sort.BubbleSortStrategy;
import org.example.sort.IterativeQuickSortStrategy;
import org.example.sort.MergeSortStrategy;
import org.example.sort.SortingService;
import org.example.strategy.SortStrategy;
import org.example.ui.DataService;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class ServisMenu {

    private static SortStrategy<Person> strategy = null;

    public static   List<Person>  choiceArr(String choice, int size, Scanner scanner){

        List<Person> dataArray = null;
       switch(choice){
            case "1":

                dataArray = DataService.fillManually(size).toList();
                break;
            case "2":
                dataArray = DataService.fillRandomly(size).toList();
                break;
            case "3":
                String[] defFile = {"persons.txt", "evensorttest.txt"};
                String seldefFile = defFile[0];
                System.out.println("Введите имя файла и оставьте пустое. Будет использован файл " + seldefFile);


                String pathToFile = scanner.nextLine();
                if(pathToFile.trim().isEmpty()){
                    pathToFile = seldefFile;
                }
                dataArray = DataService.fillFromFile(pathToFile).toList();
                break;
            default:
                System.out.println("Некорректный выбор.");


        }

    return dataArray;
    }
    public static void handleSearchServ(List<Person> dataArray, Scanner scanner) {
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

        Person.PersonBuilder targetTemp = Person.builder().age(1).name("A").salary(1);

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
      public static void handleSortingStrategia(String fieldChoice){
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
                  System.out.println("Не верный выбор.");

          }
      }
      public static Boolean handleSortServ(String fieldChoice, List<Person> dataArray){
        Boolean isSorted = false;
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
                  break;
              default:
                  System.out.println("Некорректный выбор.");
                  return  false;
          }


          System.out.println("Не сортированный список размер: " + dataArray.size());

          if(SortingService.sortArray(dataArray, strategy, getEvenValue)!=null)
              isSorted = true;

          if(getEvenValue!=null) isSorted = false;

          System.out.println("Отсортированный список размер : " + dataArray.size());

          System.out.println("Отсортированный массив: ");
          return isSorted;
      }
}
