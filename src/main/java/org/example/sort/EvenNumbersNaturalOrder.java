package org.example.sort;

import org.example.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class EvenNumbersNaturalOrder<T extends Comparable<T>&HasAge> implements SortStrategy<T> {
    private ExecutorService executor;
    private Comparator<T> comparator = (a, b) -> a.compareTo(b);

    @Override
    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator != null ? comparator : (a, b) -> a.compareTo(b);
    }

    @Override
    public Comparator<T> getComparator() {
        return this.comparator;
    }

    @Override
    public void sort(List<T> list) {
        if (list == null || list.isEmpty()) return;

        int size = list.size();
        int threads = ((ThreadPoolExecutor) executor).getCorePoolSize();

        // Создаем списки для сбора четных возрастов и индексов из всех потоков
        List<Integer> allEvenAges = Collections.synchronizedList(new ArrayList<>());
        List<Integer> allEvenIndices = Collections.synchronizedList(new ArrayList<>());

        List<Future<Void>> futures = new ArrayList<>();

        // Запускаем потоки для поиска четных чисел в разных частях списка
        for (int i = 0; i < threads; i++) {
            final int threadIndex = i;
            Future<Void> future = executor.submit(() -> {
                int start = (size * threadIndex) / threads;
                int end = (size * (threadIndex + 1)) / threads;

                // Поиск четных возрастов в своей части списка
                for (int j = start; j < end; j++) {
                    int age = list.get(j).getAge();
                    if (age % 2 == 0) {
                        allEvenAges.add(age);
                        allEvenIndices.add(j);
                    }
                }

                return null;
            });
            futures.add(future);
        }

        // Ожидание завершения всех потоков
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ошибка при поиске четных чисел в потоке", e);
            }
        }

        // Сортируем четные возраста (в основном потоке)
        Collections.sort(allEvenAges);

        // Обновляем возрасты в исходном списке (можно также распараллелить)
        for (int i = 0; i < allEvenIndices.size(); i++) {
            int index = allEvenIndices.get(i);
            int sortedAge = allEvenAges.get(i);
            list.get(index).setAge(sortedAge);
        }
    }

    @Override
    public String getStrategyName() {
        return "EvenNumbersNaturalOrder";
    }
}






















//public class EvenNumbersNaturalOrder {
//    public static <T> void sortEvenNumbersNaturalOrder(List<T> list, java.util.function.Function<T, Number> getter) {
//        List<Integer> evenIndices = new ArrayList<>();
//        List<T> evenElements = new ArrayList<>();
//
//        // Собираем индексы и элементы с чётными значениями
//        for (int i = 0; i < list.size(); i++) {
//            Number value = getter.apply(list.get(i));
//            if (value instanceof Integer) {
//                if (((Integer) value) % 2 == 0) {
//                    evenIndices.add(i);
//                    evenElements.add(list.get(i));
//                }
//            } else if (value instanceof Double || value instanceof Float) {
//                double val = value.doubleValue();
//                if ((int) val == val && (int) val % 2 == 0) { // проверка, что число целое и чётное
//                    evenIndices.add(i);
//                    evenElements.add(list.get(i));
//                }
//            } else if (value instanceof Long || value instanceof Short || value instanceof Byte) {
//                long val = value.longValue();
//                if (val % 2 == 0) {
//                    evenIndices.add(i);
//                    evenElements.add(list.get(i));
//                }
//            }
//        }
//
//        // Сортируем чётные элементы
//        // тут делаем изменения
//        evenElements.sort((a, b) -> {
//            Number aVal = getter.apply(a);
//            Number bVal = getter.apply(b);
//            return Double.compare(aVal.doubleValue(), bVal.doubleValue());
//        });
//
//        // Вставляем отсортированные элементы на их места
//        for (int i = 0; i < evenIndices.size(); i++) {
//            int index = evenIndices.get(i);
//            list.set(index, evenElements.get(i));
//        }
//    }
//
//}