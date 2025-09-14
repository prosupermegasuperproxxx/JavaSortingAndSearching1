package org.example.sort;

import org.example.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

class CustomEvenOddSort<T extends Comparable<T>> implements SortStrategy<T> {

    @Override
    public void setExecutor(ExecutorService executor) {

    }
    
    @Override
    public void setComparator(Comparator<T> comparator) {
        
    }

    @Override
    public Comparator<T> getComparator() {
        return null;
    }

    @Override
    public void sort(List<T> list) {
        
    }

    @Override
    public String getStrategyName() {
        return "";
    }
}


