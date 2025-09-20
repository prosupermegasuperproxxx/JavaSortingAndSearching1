package org.example.utils;

import org.example.strategy.SortStrategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SortTask<T extends Comparable<T>> implements Callable<List<T>> {
    private final List<T> list;
    private final SortStrategy<T> strategy;
    public static int MINIMUM_THREADS;
    
    public SortTask(List<T> list, SortStrategy<T> strategy) {
        this.list = list;
        this.strategy = strategy;
    }

    static {
        MINIMUM_THREADS = Runtime.getRuntime().availableProcessors() / 2;
        if (MINIMUM_THREADS <= 1) MINIMUM_THREADS = 2;
    }


    @Override
    public List<T> call() {

        ExecutorService executor = Executors.newFixedThreadPool(MINIMUM_THREADS);
        try {
            strategy.setExecutor(executor);
            strategy.sort(list);
        } 
        finally {
            executor.shutdown();
        }

        return list;
    }
}
