package org.example.sort;


import org.example.utils.SortTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class MergeSortStrategyTest {

    private ExecutorService pool;
    @AfterEach
    void tearDown() {
        if (pool != null) pool.shutdownNow();
    }
    @Test
    void sort_throwsIfExecutorNotSet() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        List<Integer> data = new ArrayList<>(List.of(3, 2, 1));
        assertThrows(IllegalStateException.class, () -> s.sort(data));
    }
    @Test
    void sort_throwsIfWrongThreadsCount() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        int wrong = Math.max(1, SortTask.MINIMUM_THREADS + 1);
        pool = Executors.newFixedThreadPool(wrong);
        s.setExecutor(pool);
        List<Integer> data = new ArrayList<>(List.of(3, 1, 2));
        assertThrows(RuntimeException.class, () -> s.sort(data));
    }
    @Test    void sort_ok_whenThreadsEqualToMinimum() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        pool = Executors.newFixedThreadPool(SortTask.MINIMUM_THREADS);
        s.setExecutor(pool);
        List<Integer> data = new ArrayList<>(List.of(5, 2, 9, 1, 5));
        s.sort(data);
        assertEquals(List.of(1, 2, 5, 5, 9), data);
        assertEquals("MergeSortStrategy", s.getStrategyName());
    }
    @Test    void sort_withCustomComparator_reverse() {
        MergeSortStrategy<Integer> s = new MergeSortStrategy<>();
        pool = Executors.newFixedThreadPool(SortTask.MINIMUM_THREADS);
        s.setExecutor(pool);
        s.setComparator(Comparator.reverseOrder());
        List<Integer> data = new ArrayList<>(List.of(1, 2, 3, 4));
        s.sort(data);
        assertEquals(List.of(4, 3, 2, 1), data);
    }

}