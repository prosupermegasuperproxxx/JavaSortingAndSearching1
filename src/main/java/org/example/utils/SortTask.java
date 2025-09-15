package org.example.utils;

import org.example.strategy.SortStrategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SortTask<T extends Comparable<T>> implements Callable<List<T>> {
    private final List<T> list;
    private final SortStrategy<T> strategy;
    public static final int MINIMUM_THREADS = 3;
    
    public SortTask(List<T> list, SortStrategy<T> strategy) {
        this.list = list;
        this.strategy = strategy;
    }

    @Override
    public List<T> call() {
        System.out.println("Executing SortTask");
//        ExecutorService executor = Executors.newWorkStealingPool(MINIMUM_THREADS);
        ExecutorService executor = Executors.newFixedThreadPool(MINIMUM_THREADS);
//        ExecutorService executor = Executors.newFixedThreadPool(MINIMUM_THREADS, Executors.defaultThreadFactory());
//        ExecutorService executor = Executors.newCachedThreadPool(  );
        try {
            strategy.setExecutor(executor);
            strategy.sort(list);
//            executor.awaitTermination(999999, TimeUnit.DAYS);
        } 
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } 
        finally {
            executor.shutdown();
        }
//        catch (Exception e) {
//            System.out.println("Sorting failed :" + e.getMessage());            
//        }

        System.out.println("Finished SortTask");
        return list;
    }
}
