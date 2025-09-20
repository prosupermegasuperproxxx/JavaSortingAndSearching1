package org.example.sort;


import org.example.utils.SortTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
//сортировка слияние
class MergeSortStrategyTest {

    private ExecutorService pool;
    @AfterEach
    void tearDown() {

        if (pool != null) pool.shutdownNow();
    }
    //проверяет что выкинет нужного типа исключение если не передан executor
    @Test
    void sort_throwsIfExecutorNotSet() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        List<Integer> data = new ArrayList<>(List.of(3, 2, 1));
        assertThrows(IllegalStateException.class, () -> s.sort(data));
    }

    // проверяем правильность сортировки и название стратегии
    @Test
    void sort_ok_whenThreadsEqualToMinimum() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        pool = Executors.newFixedThreadPool(SortTask.MINIMUM_THREADS);
        s.setExecutor(pool);
        List<Integer> data = new ArrayList<>(List.of(5, 2, 9, 1, 5));
        s.sort(data);
        assertEquals(List.of(1, 2, 5, 5, 9), data);
        assertEquals("MergeSortStrategy", s.getStrategyName());
    }
    //сортировка по убыванию
    @Test
    void sort_withCustomComparator_reverse() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        pool = Executors.newFixedThreadPool(SortTask.MINIMUM_THREADS);
        s.setExecutor(pool);
        s.setComparator(Comparator.reverseOrder());
        List<Integer> data = new ArrayList<>(List.of(1, 2, 3, 4));
        s.sort(data);
        assertEquals(List.of(4, 3, 2, 1), data);
    }

}