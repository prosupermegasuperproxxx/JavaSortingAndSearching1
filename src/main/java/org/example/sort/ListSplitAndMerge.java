package org.example.sort;

import org.example.utils.SortTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class ListSplitAndMerge {

    public static <T> void multiThreadSort(List<T> list, ExecutorService executor, Comparator<T> comparator, BiFunction<List<T>, Comparator<T>, List<T>> singleThreadSortMethod) {
        // Проверка, что executor задан и соответствует требованиям
        if (executor == null) {
            throw new IllegalStateException("Executor not initialized");
        }

        int threads = ((ThreadPoolExecutor) executor).getCorePoolSize();
//        if (threads != SortTask.MINIMUM_THREADS) throw new RuntimeException("Invalid number of threads");// only for test

        List<List<T>> parts = ListSplitAndMerge.splitList(list, threads);

        // Создаём задачи для сортировки каждой части
        List<Future<List<T>>> futures = new ArrayList<>();
        for (List<T> part : parts) {
            Future<List<T>> future = executor.submit(() -> singleThreadSortMethod.apply(part, comparator));
            futures.add(future);
        }

        // Ждём завершения всех задач и собираем результаты
        List<List<T>> sortedParts = new ArrayList<>();
        for (Future<List<T>> future : futures) {
            try {
                sortedParts.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error during sorting", e);
            }
        }

        // Сливаем отсортированные части в один список
        List<T> result = ListSplitAndMerge.mergeSortedLists(sortedParts, comparator);

        // Заменяем исходный список на результат
        list.clear();
        list.addAll(result);
    }

    
    public static <T> List<List<T>> splitList(List<T> list, int parts) {
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int chunkSize = (size + parts - 1) / parts;

        for (int i = 0; i < size; i += chunkSize) {
            int end = Math.min(i + chunkSize, size);
            result.add(new ArrayList<>(list.subList(i, end)));
        }

        return result;
    }


    private static class QueueElement <T> implements Comparable<QueueElement> {
        private final List<T> list;
        private final int index;

        public QueueElement(List<T> list, int index) {
            this.list = list;
            this.index = index;
        }

        public List<T> getList() {
            return list;
        }

        public int getIndex() {
            return index;
        }

        public T getValue() {
            return list.get(index);
        }

        @Override
        public int compareTo(QueueElement o) {
            throw new IllegalStateException();
        }

    }

    public static <T> List<T> mergeSortedLists(List<List<T>> sortedParts, Comparator<T> comparator) {
        // Создаем очередь, которая будет хранить текущие элементы из каждого списка
        PriorityQueue<QueueElement<T>> queue = new PriorityQueue<>((a, b) -> {
            return comparator.compare(a.getValue(), b.getValue());
        });

        // Инициализируем очередь. добавляем первый элемент из каждого отсортированного подсписка
        for (List<T> part : sortedParts) {
            if (!part.isEmpty()) {
                queue.add(new QueueElement<>(part, 0));
            }
        }

        List<T> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            QueueElement<T> element = queue.poll();
            result.add(element.getValue());

            // Если есть ещё элементы в этом подсписке, то добавляем следующий
            if (element.getIndex() + 1 < element.getList().size()) {
                queue.add(new QueueElement<>(element.getList(), element.getIndex() + 1));
            }
        }

        return result;
    }
}
