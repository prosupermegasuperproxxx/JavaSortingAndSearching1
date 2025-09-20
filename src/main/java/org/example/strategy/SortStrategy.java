package org.example.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public interface SortStrategy<T extends Comparable<T>> {

    void setExecutor(ExecutorService executor);

    void setComparator(Comparator<T> comparator);

    Comparator<T> getComparator();

    void sort(List<T> list);

    String getStrategyName();

}