package org.example.strategy;

//import org.example.customcollection.CustomList;

import org.example.model.Person;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public interface SortStrategy<T extends Comparable<T>>/* extends AutoCloseable*/ {
//interface SortStrategy<T> {
    void setExecutor(ExecutorService executor);
    void setComparator(Comparator<T> comparator); // Comparator<? super T> comparator) 
    Comparator< T> getComparator(); 
    void sort(List<T> list);
    String getStrategyName();
//    @Override
//    void close();

}
