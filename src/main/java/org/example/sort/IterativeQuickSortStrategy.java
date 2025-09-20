package org.example.sort;

import org.example.strategy.SortStrategy;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class IterativeQuickSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {
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
    public void sort(final List<T> list) {
        if (list == null || list.size() <= 1) return;

        ListSplitAndMerge.multiThreadSort(list,this.executor,this.comparator,IterativeQuickSortStrategy::singleThread);

    }

    private static <T> List<T> singleThread(List<T> list, Comparator<T> comparator) {
        // избежать stack overflow и не использовать рекурсию.        
        // используем стек для хранения подмассивов.
        Deque<Range> stack = new ArrayDeque<>();
        stack.push(new Range(0, list.size() - 1));

        while (!stack.isEmpty()) {
            Range range = stack.pop();
            int left = range.left;
            int right = range.right;

            if (left >= right) continue;

            int pivotIndex = partition(list, left, right, comparator);

            stack.push(new Range(left, pivotIndex - 1));
            stack.push(new Range(pivotIndex + 1, right));
        }
        return list;
    }

    private static <T> int partition(List<T> list, int left, int right, Comparator<T> comparator) {
        T pivot = list.get(right);
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, right);
        return i + 1;
    }

    private static class Range {
        final int left;
        final int right;

        Range(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public String getStrategyName() {
        return "IterativeQuickSortStrategy";
    }
    
}