package org.example.utils;

import org.example.strategy.SortStrategy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Threads<T extends Comparable<T>> {
    private final ExecutorService executor;
    public static final int MINIMUM_THREADS = 3;

    public Threads() {
        this.executor = Executors.newFixedThreadPool(1);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public <T extends Comparable<T>> Future<List<T>> executeSort(List<T> list, SortStrategy<T> strategy) {
        return executor.submit(new SortTask<>(list, strategy));
    }
}