package org.example.search;

import org.example.customcollection.CustomList;
import org.example.model.Person;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

//бинарный поиск
class BinarySearchTest {

    private final BinarySearch<Integer> intSearch = new BinarySearch<>();


//находим номер элемента в  массиве
    @Test
    void found_integer_naturalOrder() {
       List<Integer> data = List.of(1, 3, 5, 7, 9);
        int idx = intSearch.binarySearch(data, 5, Comparator.naturalOrder());
        assertEquals(2, idx);
    }

       //возвращает позицию при повторяющихся элементах, первую и последнюю
    @Test
    void duplicates_findFirstAndLast_withSlowHelpers() {
        List<Integer> data = List.of(1, 2, 2, 2, 3, 4);
        int anyIdx = intSearch.binarySearch(data, 2, Comparator.naturalOrder());
        assertTrue(anyIdx >= 1 && anyIdx <= 3, "binarySearch должен вернуть индекс одного из '2'");

        int first = intSearch.binarySearchFindFirstSlow(anyIdx, data, 2, Comparator.naturalOrder());
        int last  = intSearch.binarySearchFindLastSlow(anyIdx,  data, 2, Comparator.naturalOrder());

        assertEquals(1, first);
        assertEquals(3, last);
    }
    // пустой лист и лист с одним элементом
    @Test
    void emptyAndSingleElement() {
        // пустой
        assertEquals(-1, intSearch.binarySearch(List.of(), 42, Comparator.naturalOrder()));

        // один элемент — совпадение
        List<Integer> one = List.of(5);
        assertEquals(0, intSearch.binarySearch(one, 5, Comparator.naturalOrder()));

        // один элемент — нет совпадения (вставка в позицию 0)
        assertEquals(-1, intSearch.binarySearch(one, 1, Comparator.naturalOrder()));
    }
}