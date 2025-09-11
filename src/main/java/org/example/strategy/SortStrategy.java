package org.example.strategy;

//import org.example.customcollection.CustomList;

import java.util.List;

public interface SortStrategy<T extends Comparable<T>> {
    void sort(List<T> list);
    String getStrategyName();
}