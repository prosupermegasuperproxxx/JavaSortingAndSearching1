package org.example.sort;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;
//сортировка быстрая
class IterativeQuickSortStrategyTest {

    private ExecutorService pool;
    @AfterEach
    void tearDown() {

        if (pool != null) pool.shutdownNow();
    }
    //сортировка по возрастанию
    @Test
    void sort_integers_naturalOrder() {
        IterativeQuickSortStrategy<Integer> s = new IterativeQuickSortStrategy<>();
        pool = Executors.newFixedThreadPool(2);
        s.setExecutor(pool);
        List<Integer> data = new ArrayList<>(List.of(7, 3, 9, 1, 5));
        s.sort(data);
        assertEquals(List.of(1, 3, 5, 7, 9), data);
        assertEquals("IterativeQuickSortStrategy", s.getStrategyName());
    }
    //сортировка по убыванию
    @Test
    void sort_integers_customComparator_reverse() {
        IterativeQuickSortStrategy<Integer> s = new IterativeQuickSortStrategy<>();
        pool = Executors.newFixedThreadPool(2);
        s.setExecutor(pool);
        s.setComparator(Comparator.reverseOrder());
        List<Integer> data = new ArrayList<>(List.of(1, 4, 2, 3));
        s.sort(data);
        assertEquals(List.of(4, 3, 2, 1), data);
    }
    //сортировка по одному и пустому
    @Test
    void sort_handles_oneAndEmpty() {
        IterativeQuickSortStrategy<Integer> s = new IterativeQuickSortStrategy<>();
        pool = Executors.newFixedThreadPool(2);
        s.setExecutor(pool);
        List<Integer> one = new ArrayList<>(List.of(42));
        s.sort(one);
        assertEquals(List.of(42), one);
        List<Integer> empty = new ArrayList<>();
        s.sort(empty);

    }
}