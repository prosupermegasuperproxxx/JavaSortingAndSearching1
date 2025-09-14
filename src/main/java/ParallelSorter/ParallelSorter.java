package ParallelSorter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelSorter {
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(2);

//    public static <T extends Comparable<T>> CompletableFuture<Void> sortAsync(
//            T[] array, SortStrategyName<T> strategy){
//        return null;
//    }

    public static void shutDown(){
        threadPool.shutdown();
    }
}



