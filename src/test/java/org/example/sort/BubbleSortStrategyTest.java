package org.example.sort;

import org.example.strategy.SortStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;

class BubbleSortStrategyTest {
    private ExecutorService pool;
    @AfterEach    void tearDown() {
        if (pool != null) pool.shutdownNow();
    }
    //сортировка по возрастанию
    @Test
    void sort_integers_naturalOrder() {
        BubbleSortStrategy<Integer> s = new BubbleSortStrategy<>();
        pool = Executors.newFixedThreadPool(3);
        s.setExecutor(pool);
        List<Integer> data = new ArrayList<>(List.of(5, 2, 9, 1, 3, 3));
        s.sort(data);
        assertEquals(List.of(1, 2, 3, 3, 5, 9), data);
        assertEquals("BubbleSortStrategy", s.getStrategyName());
    }
    //сортировка по убыванию
    @Test
    void sort_integers_customComparator_reverse() {
        BubbleSortStrategy<Integer> s = new BubbleSortStrategy<>();
        pool = Executors.newFixedThreadPool(2);
        s.setExecutor(pool);
        s.setComparator(Comparator.reverseOrder());
        List<Integer> data = new ArrayList<>(List.of(1, 4, 2, 3));
        s.sort(data);
        assertEquals(List.of(4, 3, 2, 1), data);
        assertSame(Comparator.reverseOrder().getClass(), s.getComparator().getClass());
    }
    //сортировка пустого
    @Test
    void sort_handlesEmptyAndNull() {
        BubbleSortStrategy<Integer> s = new BubbleSortStrategy<>();
        pool = Executors.newFixedThreadPool(2);
        s.setExecutor(pool);
        List<Integer> empty = new ArrayList<>();
        s.sort(empty);
        assertTrue(empty.isEmpty());
        s.sort(null);
    }


}