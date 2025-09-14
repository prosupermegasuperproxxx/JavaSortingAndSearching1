package org.example.utils;

import org.example.strategy.SortStrategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.utils.Threads.MINIMUM_THREADS;

public class SortTask<T extends Comparable<T>> implements Callable<List<T>> {
    private final List<T> list;
    private final SortStrategy<T> strategy;

    public SortTask(List<T> list, SortStrategy<T> strategy) {
        this.list = list;
        this.strategy = strategy;
    }

    @Override
    public List<T> call() {
//        System.out.println("Executing SortTask");
        try(ExecutorService executor = Executors.newFixedThreadPool(MINIMUM_THREADS)) {
            strategy.setExecutor(executor);
            strategy.sort(list);
        }
//        catch (Exception e) {
//            System.out.println("Sorting failed :" + e.getMessage());            
//        }

//        System.out.println("Finished SortTask");
        return list;
    }
}
