package org.example.counterN;

import org.example.model.Person;
import org.example.strategy.SortStrategy;
import org.example.ui.DataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//поиск количества вхождений элементов
class CountNStrategyTest {

    private ExecutorService pool;

    @AfterEach
    void tearDown() {
        if (pool != null) {
            pool.shutdown();
            try { pool.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {}
        }
    }
    /** Создаёт мок CounterN, который хранит счётчик в AtomicLong. */
    private CounterN atomicCounterMock(AtomicLong state) {
        CounterN counter = mock(CounterN.class);

        // reset() -> 0
        doAnswer(inv -> { state.set(0L); return null; }).when(counter).reset();

        // increment() -> ++
        doAnswer(inv -> { state.incrementAndGet(); return null; }).when(counter).increment();

        // getCount() -> текущее
        when(counter.getCount()).thenAnswer(inv -> state.get());

        return counter;
    }
    private static Person p(String name, int age, double salary) {
        return new Person.PersonBuilder()
                .name(name).age(age).salary(salary)
                .build();
    }
    //проверяем количество вхождений искать по возрасту
    @Test
    void countsByAge_exactMatch() throws Exception {
        AtomicLong state = new AtomicLong();
        CounterN counter = atomicCounterMock(state);
//будем искать 25 по возрасту
        CountNStrategy strategy =
                new CountNStrategy(counter, 25, Person::getAge);

        pool = Executors.newFixedThreadPool(4);
        strategy.setExecutor(pool);

        List<Person> data = List.of(
                p("A", 25, 1.0),
                p("B", 20, 2.0),
                p("C", 25, 3.0),
                p("D", 40, 4.0)
        );

        strategy.sort(data);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
        //проверяем что 2 вхождения с возрастом 25
        assertEquals(2L, counter.getCount());
    }
    //проверяем количество вхождений искать по зарплате
    @Test
    void countsBySalary_exactDoubleEquals() throws Exception {
        AtomicLong state = new AtomicLong();
        CounterN counter = atomicCounterMock(state);

        double target = 1234.56;
        CountNStrategy strategy =
                new CountNStrategy(counter, target, Person::getSalary);

        pool = Executors.newFixedThreadPool(2);
        strategy.setExecutor(pool);

        List<Person> data = List.of(
                p("A", 25, 1234.56),
                p("B", 30, 100.00),
                p("C", 40, 1234.56),
                p("D", 22, 1234.5600001) // НЕ равен через equals()
        );

        strategy.sort(data);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(2L, counter.getCount());
    }
    //проверяем функцию getStrategyName()
    @Test
    void strategyName_containsTargetValue() {
        AtomicLong state = new AtomicLong();
        CounterN counter = atomicCounterMock(state);

        CountNStrategy strategy =
                new CountNStrategy(counter, "TestName", Person::getName);

        assertTrue(strategy.getStrategyName().contains("TestName"));
    }

}