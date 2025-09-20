package org.example.sort;

import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

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

        ListSplitAndMerge.multiThreadSort(list, this.executor, this.comparator, MergeSortStrategy::mergeSortRecursive);

    }

    public static <T> List<T> mergeSortRecursive(List<T> list, java.util.Comparator<? super T> comparator) {
        if (list == null) throw new NullPointerException("list is null");
        if (list.size() <= 1) {
            return new ArrayList<>(list);
        }

        int mid = list.size() / 2;
        List<T> left = mergeSortRecursive(list.subList(0, mid), comparator);
        List<T> right = mergeSortRecursive(list.subList(mid, list.size()), comparator);

        return merge(left, right, comparator);
    }

    private static <T> List<T> merge(List<T> left, List<T> right, java.util.Comparator<? super T> comparator) {
        List<T> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        // Добавляем оставшиеся элементы
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    @Override
    public String getStrategyName() {
        return "MergeSortStrategy";
    }

}