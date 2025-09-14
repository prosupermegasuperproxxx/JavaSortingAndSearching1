package org.example.sort;

import org.example.customcollection.CustomList;
import org.example.model.Person;
import org.example.search.BinarySearch;
import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

import java.util.Comparator;
import java.util.List;

public class SortingService {
    public <T extends Comparable<T>> List<T> sortArray(List<T> list, Comparator<T> comparator, SortStrategy<T> strategy){
        //public void <T> sortArray(List array, Comparator<Person> comparator) {
        //Arrays.sort(array, comparator);

        try {
            System.out.println("Начало сортировки...");
            List<T> sortedList;

            sortedList = new SortTask<>(list, strategy).call();

//            System.out.println("Отсортированный список: " + sortedList);
            System.out.println("Сортировка завершена");
            System.out.println("Использован алгоритм: " + strategy.getStrategyName());

            //           Person target = new Person.PersonBuilder().age(219).name("Andrew").salary(2576).build();

//            BinarySearch<Person> binarySearch = new BinarySearch<>();
//
//            //  чтоб бинарный поиск работал, искать надо тем же компаратором, что и сортировка была
//            int index = binarySearch.binarySearch((List<Person>) sortedList, target, (Comparator< Person>) strategy.getComparator());
//
//            if (index >= 0) {
//                System.out.println("Person found at index: " + index);
//            } else {
//                System.out.println("Person not found.");
//            }

            // Запись в файл (Доп задание 2)
//            if (getBooleanInput("Записать результат в файл? (y/n): ")) {
//                String filename = getStringInput("Введите имя файла: ");
//                FileUtil.writeToFile(filename, sortedList, true);
//                System.out.println("Результат записан в файл: " + filename);
//            }
            return sortedList;
        } catch (Exception e) {
            System.out.println("Ошибка при сортировке: " + e.getMessage());
        }
        return null;
    }
}