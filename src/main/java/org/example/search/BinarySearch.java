package org.example.search;

import java.util.Comparator;
import java.util.List;

public class BinarySearch<T> {
    public  int binarySearch(List<T> list, T key, Comparator<T> comparator) {
//    public int binarySearch(List<? extends T> list, T key, Comparator<? super T> comparator) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }
}