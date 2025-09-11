package org.example.utils;

import org.example.strategy.SortStrategy;

import java.util.List;
import java.util.concurrent.Callable;

public class SortTask<T extends Comparable<T>> implements Callable<List<T>> {
    private final List<T> list;
    private final SortStrategy<T> strategy;

    public SortTask(List<T> list, SortStrategy<T> strategy) {
        this.list = list;
        this.strategy = strategy;
    }

    @Override
    public List<T> call() {
        strategy.sort(list);
        return list;
    }
}
