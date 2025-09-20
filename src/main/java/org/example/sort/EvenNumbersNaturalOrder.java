package org.example.sort;

import java.util.ArrayList;
import java.util.List;

public class EvenNumbersNaturalOrder {
    
    public static class EvenRecord<T> {
        public final List<Integer> evenIndices;
        public final List<T> evenElements;
        
        public EvenRecord(List<Integer> evenIndices, List<T> evenElements) {
            this.evenIndices = evenIndices;
            this.evenElements = evenElements;
        }

        public List<Integer> getEvenIndices() {
            return evenIndices;
        }

        public List<T> getEvenElements() {
            return evenElements;
        }
    }
    
    public static <T> EvenRecord<T> getEvenNumbersRecord(List<T> list, java.util.function.Function<T, Number> getter) {
        List<Integer> evenIndices = new ArrayList<>();
        List<T> evenElements = new ArrayList<>();

        // Собираем индексы и элементы с чётными значениями
        for (int i = 0; i < list.size(); i++) {
            Number value = getter.apply(list.get(i));
            if (value instanceof Integer) {
                if (((Integer) value) % 2 == 0) {
                    evenIndices.add(i);
                    evenElements.add(list.get(i));
                }
            } else if (value instanceof Double || value instanceof Float) {
                double val = value.doubleValue();
                if ((int) val == val && (int) val % 2 == 0) { // проверка, что число целое и чётное
                    evenIndices.add(i);
                    evenElements.add(list.get(i));
                }
            } else if (value instanceof Long || value instanceof Short || value instanceof Byte) {
                long val = value.longValue();
                if (val % 2 == 0) {
                    evenIndices.add(i);
                    evenElements.add(list.get(i));
                }
            }
        }

        return new EvenRecord<>(evenIndices, evenElements);
        
    }

}