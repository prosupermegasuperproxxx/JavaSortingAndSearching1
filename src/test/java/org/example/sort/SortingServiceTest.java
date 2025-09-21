package org.example.sort;


import org.example.strategy.SortStrategy;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//сортировка по четным
class SortingServiceTest {
    private ByteArrayOutputStream outBuf;
    private ByteArrayOutputStream errBuf;
    private PrintStream oldOut, oldErr;

    @BeforeEach
    void hookStd() {
        outBuf = new ByteArrayOutputStream();
        errBuf = new ByteArrayOutputStream();
        oldOut = System.out;
        oldErr = System.err;
        System.setOut(new PrintStream(outBuf));
        System.setErr(new PrintStream(errBuf));
    }

    @AfterEach
    void restoreStd() {
        System.setOut(oldOut);
        System.setErr(oldErr);
    }


    /** Простая стратегия: сортирует список на месте по comparator (или natural). */
    static class SimpleStrategy<T extends Comparable<T>> implements SortStrategy<T> {
        private Comparator<T> cmp;
        @Override
        public void setExecutor(java.util.concurrent.ExecutorService executor) {}
        @Override
        public void setComparator(Comparator<T> comparator) { this.cmp = comparator; }
        @Override
        public Comparator<T> getComparator() {
            return cmp;
        }
        @Override public void sort(List<T> list) {
            if (cmp != null) list.sort(cmp);
            else Collections.sort(list);
        }
        @Override public String getStrategyName() { return "SimpleStrategy"; }
    }

// Проверка что результат сортировки не нулевой, индексы четных поменялись а нечетных нет

    @Test
    void sort_basic_noEvenFilter_sortsAndPrints() {

        List<Integer> data = new ArrayList<>(List.of(5, 2, 9, 1, 5));

        SortStrategy<Integer> strategy = new SimpleStrategy<>();

        List<Integer> result = SortingService.sortArray(data, strategy);

        assertNotNull(result);

        assertEquals(List.of(1, 2, 5, 5, 9), result);

    }
}