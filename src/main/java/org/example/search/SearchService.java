package org.example.search;

import java.util.Comparator;
import java.util.List;

public class SearchService {
    /**
     
     находит один элемент, 
     который может быть не единственным, и не является крайним.
     с таким же значением поля возможно множество элементов.

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
                System.out.println("Значение найдено, и его индекс: " + index);
                System.out.println(sortedList.get(index));
                
                int left = binarySearch.binarySearchFindFirstSlow(index, sortedList, key, comparator);
                int right = binarySearch.binarySearchFindLastSlow(index, sortedList, key, comparator);
                if(left != index || right != index) {
                    System.out.println("vvvvvvvvvvvvvvvvvvvv");
                    System.out.println("Индекс первого с таким же значением: " + left);
                    System.out.println(sortedList.get(left));
                    System.out.println("Индекс последнего с таким же значением: " + right);
                    System.out.println(sortedList.get(right));
                    System.out.println("^^^^^^^^^^^^^^^^^^^^");
                }
            } else {
                System.out.println("Ничего не найдено.");
            }

        return null;
    }
}