package org.example.counterN;

import org.example.counterN.CounterN;
import org.example.model.Person;
import org.example.strategy.SortStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class CountNStrategy implements SortStrategy<Person> {
    private final CounterN counter;
    private final Object targetValue;
    private final Function<Person, ?> fieldExtractor;
    private ExecutorService executor;
    private Comparator<Person> comparator;

    public CountNStrategy(CounterN counter, Object targetValue, Function<Person, ?> fieldExtractor) {
        this.counter = counter;
        this.targetValue = targetValue;
        this.fieldExtractor = fieldExtractor;
    }

    @Override
    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void setComparator(Comparator<Person> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Comparator<Person> getComparator() {
        return comparator;
    }

    @Override
    public void sort(List<Person> list) {
        counter.reset();

        if (executor == null) {
            throw new IllegalStateException("ExecutorService не установлен");
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        int batchSize = Math.max(1, list.size() / numThreads);

        for (int i = 0; i < numThreads; i++) {
            int fromIndex = i * batchSize;
            int toIndex = (i == numThreads - 1) ? list.size() : (i + 1) * batchSize;

            if (fromIndex < list.size()) {
                List<Person> subList = list.subList(fromIndex, toIndex);
                executor.submit(new CountingTask(subList, targetValue, fieldExtractor, counter));
            }
        }
    }

    @Override
    public String getStrategyName() {
        return "Подсчет вхождений: " + targetValue;
    }

    private static class CountingTask implements Runnable {
        private final List<Person> subList;
        private final Object targetValue;
        private final Function<Person, ?> fieldExtractor;
        private final CounterN counter;

        public CountingTask(List<Person> subList, Object targetValue,
                            Function<Person, ?> fieldExtractor, CounterN counter) {
            this.subList = subList;
            this.targetValue = targetValue;
            this.fieldExtractor = fieldExtractor;
            this.counter = counter;
        }

        @Override
        public void run() {
            for (Person person : subList) {
                Object fieldValue = fieldExtractor.apply(person);
                if (targetValue.equals(fieldValue)) {
                    counter.increment();
                }
            }
        }
    }
}