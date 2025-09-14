package org.example.sort;

import org.example.strategy.SortStrategy;

import java.util.*;
import java.util.concurrent.*;

public class MergeSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {
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

        List<T> copy = new ArrayList<>(list);
        mergeSort(copy, 0, copy.size() - 1);
        list.clear();
        list.addAll(copy);
    }

    private void mergeSort(List<T> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Рекурсивно сортируем левую и правую части
            Future<Void> leftFuture = executor.submit(() -> {
                mergeSort(list, left, mid);
                return null;
            });

            Future<Void> rightFuture = executor.submit(() -> {
                mergeSort(list, mid + 1, right);
                return null;
            });

            try {
                leftFuture.get();
                rightFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ошибка при выполнении сортировки", e);
            }

            // Сливаем отсортированные части
            merge(list, left, mid, right);
        }
    }

    private void merge(List<T> list, int left, int mid, int right) {
        List<T> temp = new ArrayList<>();
        int i = left;
        int j = mid + 1;

        while (i <= mid && j <= right) {
            if (comparator.compare(list.get(i), list.get(j)) <= 0) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }

        // Добавляем оставшиеся элементы
        while (i <= mid) {
            temp.add(list.get(i++));
        }
        while (j <= right) {
            temp.add(list.get(j++));
        }

        // Копируем отсортированные элементы обратно в исходный список
        for (int k = 0; k < temp.size(); k++) {
            list.set(left + k, temp.get(k));
        }
    }

    @Override
    public String getStrategyName() {
        return "MergeSortStrategy";
    }

}