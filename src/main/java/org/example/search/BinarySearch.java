package org.example.search;

import java.util.Comparator;
import java.util.List;

public class BinarySearch<T> {
    public  int binarySearch(List<T> list, T key, Comparator</*? super */T> comparator) {
//        System.out.println(comparator.toString());
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
    
    public  int binarySearchFindFirstSlow(int index, List<T> list, T key, Comparator</*? super */T> comparator) {
        while (index > 0) {
            int mid = index-1;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);

            if (cmp != 0)
                return index;
            else
                index--; // key found
        }
        return 0;  // key not found.
    }

    public  int binarySearchFindLastSlow(int index, List<T> list, T key, Comparator</*? super */T> comparator) {
        int high = list.size() - 1;

        while (index < high) {
            int mid = index+1;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);

            if (cmp != 0)
                return index;
            else
                index++; // key found
        }
        return high;  // key not found.
    }
}