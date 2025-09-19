package org.example.sort;

import org.example.counterN.CounterN;
import org.example.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class BubbleSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {

    private ExecutorService executor;

    // Если компаратор не задан, используем естественный порядок
    private Comparator<T> comparator = (a, b) -> a.compareTo(b);



    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator != null ? comparator : (a, b) -> a.compareTo(b);
    }

    @Override
    public Comparator<T> getComparator() {
        return this.comparator;
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.isEmpty()) return;

        int size = list.size();
        //  разделить массив на число потоков
        int threads = ((ThreadPoolExecutor) executor).getCorePoolSize();

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            final int threadIndex = i;
            Future<Void> future = executor.submit(() -> {
                int start = (size * threadIndex) / threads;
                int end = (size * (threadIndex + 1)) / threads;

                // Сортировка части списка
                boolean swapped;
                do {
                    swapped = false;
                    for (int j = start; j < end - 1; j++) {
                        if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                            Collections.swap(list, j, j + 1);
                            swapped = true;
                        }
                    }
                } while (swapped);

                return null;
            });

            futures.add(future);
        }

        // Ожидание завершения всех потоков
        for (Future<Void> future : futures) {
            try {
                future.get(); // ожидает завершения
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ошибка при выполнении сортировки в потоке", e);
            }
        }

        // После параллельной сортировки частей, нужно объединить и сделать окончательную сортировку
        // (так как BubbleSort не является полностью распараллеливаемым)
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < list.size() - 1; i++) {
                if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                    Collections.swap(list, i, i + 1);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    @Override
    public String getStrategyName() {
        return "BubbleSortStrategy";
    }

}
