package org.example.sort;

import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

import java.util.*;
import java.util.concurrent.*;

public class MergeSortStrategy<T extends Comparable<T>> implements SortStrategy<T> {
    private ExecutorService executor;

    // Если компаратор не задан, используем естественный порядок
    private Comparator<T> comparator = (a, b) -> a.compareTo(b);

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
//        this.executor = Executors.newCachedThreadPool();
//        this.executor = Executors.newFixedThreadPool(3);

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
        if (list == null || list.isEmpty()) {
            return;
        }

        // Проверка, что executor задан и соответствует требованиям
        if (executor == null) {
            throw new IllegalStateException("Executor not initialized");
        }

        int threads = ((ThreadPoolExecutor) this.executor).getCorePoolSize();
        if (threads != SortTask.MINIMUM_THREADS)
            throw new RuntimeException("Invalid number of threads");

        // Разбиваем список на части
        List<List<T>> parts = splitList(list, threads);

        // Создаём задачи для сортировки каждой части
        List<Future<List<T>>> futures = new ArrayList<>();
        for (List<T> part : parts) {
            Future<List<T>> future = executor.submit(() -> mergeSortRecursive(new ArrayList<>(part), comparator));
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
        List<T> result = mergeSortedLists(sortedParts);

        // Заменяем исходный список на результат
        list.clear();
        list.addAll(result);
    }

    /**
     * Разбивает список на указанное количество частей.
     */
    private <T> List<List<T>> splitList(List<T> list, int parts) {
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int chunkSize = (size + parts - 1) / parts;

        for (int i = 0; i < size; i += chunkSize) {
            int end = Math.min(i + chunkSize, size);
            result.add(new ArrayList<>(list.subList(i, end)));
        }

        return result;
    }


    private static class QueueElement <T extends Comparable<T>> implements Comparable<QueueElement> {
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
        public int compareTo(QueueElement  o) {
//            Comparable<T> t = (T) ((QueueElement) o);
//            return this.list.get(index).compareTo((T) ((QueueElement)t).list.get( ((QueueElement)t).index));
            
            return this.list.get(index).compareTo((T) o.list.get(o.getIndex()));
        }
    }

    private List<T> mergeSortedLists(List<List<T>> sortedParts) {
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


    public static <T> List<T> mergeSortRecursive(List<T> list, java.util.Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) {
            return new ArrayList<>(list);
        }

        int mid = list.size() / 2;
        List<T> left = mergeSortRecursive(list.subList(0, mid), comparator);
        List<T> right = mergeSortRecursive(list.subList(mid, list.size()), comparator);

        return merge(left, right, comparator);
    }

    private static <T> List<T> merge(List<T> left, List<T> right, java.util.Comparator<? super T> comparator) {
        List<T> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        // Добавляем оставшиеся элементы
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }
    
    @Override
    public String getStrategyName() {
        return "MergeSortStrategy";
    }

}