package org.example.search;

import org.example.model.Person;

import java.util.Comparator;
import java.util.List;

public class SearchService {
    /**
     
     находит пока что один элемент, 
     который может быть не единственным, и не является крайним.
     в случае когда с таким же значением поля есть множество элементов.
     можно доделать с выводом результата в виде Range ( , )
     и показывать самый первый крайний сверху.
     <br/>
     чтоб бинарный поиск работал, искать надо тем же компаратором, что и сортировка была

     @param sortedList 
     @param comparator
     @param key  Person target = new Person.PersonBuilder().age(219).name("Andrew").salary(2576).build(); 
     @param <T>
     @return
     */
    public static <T extends Comparable<T>> T searchKey(List<T> sortedList, Comparator<T> comparator, T key) {

            BinarySearch<T> binarySearch = new BinarySearch<>();

            int index = binarySearch.binarySearch(sortedList, key, comparator);

            if (index >= 0) {
                System.out.println("Person found at index: " + index);
                System.out.println(sortedList.get(index));
                
                int left = binarySearch.binarySearchFindFirstSlow(index, sortedList, key, comparator);
                int right = binarySearch.binarySearchFindLastSlow(index, sortedList, key, comparator);
                
                System.out.println("vvvvvvvvvvvvvvvvvvvv");
                System.out.println(left + ": первый с таким же значением\n" + sortedList.get(left));
                System.out.println(right + ": последний с таким же значением\n" + sortedList.get(right));
                System.out.println("^^^^^^^^^^^^^^^^^^^^");
            } else {
                System.out.println("Person not found.");
            }

        return null;
    }
}