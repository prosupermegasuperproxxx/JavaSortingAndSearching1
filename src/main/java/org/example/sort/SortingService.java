package org.example.sort;

import org.example.model.Person;

import java.util.Arrays;
import java.util.Comparator;

public class SortingService {
    public void sortArray(Person[] array, Comparator<Person> comparator) {
        Arrays.sort(array, comparator);
    }
}