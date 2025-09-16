package org.example.search;

import org.example.model.Person;

import java.util.Comparator;
import java.util.List;

public class SearchService {
    public static <T extends Comparable<T>> T searchKey(List<T> sortedList, Comparator<T> comparator, T key) {

        //           Person target = new Person.PersonBuilder().age(219).name("Andrew").salary(2576).build();

            BinarySearch<T> binarySearch = new BinarySearch<>();
//
//            //  чтоб бинарный поиск работал, искать надо тем же компаратором, что и сортировка была
            int index = binarySearch.binarySearch(sortedList, key, comparator);
//
            if (index >= 0) {
                System.out.println("Person found at index: " + index);
                System.out.println(sortedList.get(index));
            } else {
                System.out.println("Person not found.");
            }
        
//        for (Person person : array) {
//            if (person.getName().contains(key)) {
//                return person;
//            }
//        }
        return null;
    }
}