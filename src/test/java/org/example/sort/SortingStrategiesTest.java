package org.example.sort;

import org.example.model.Person;
import org.example.strategy.SortStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Простые тесты для всех стратегий сортировки с проверкой базовой сортировки по всем полям
 * Реализованы без использования JUnit
 */
public class SortingStrategiesTest {

    private final List<Person> testData;
    private final ExecutorService executor;

    public SortingStrategiesTest() {
        // Создаем тестовые данные для проверки сортировки по всем полям
        testData = new ArrayList<>();
        testData.add(Person.builder().name("Анна").age(30).salary(60000.00).build());
        testData.add(Person.builder().name("Анна").age(25).salary(50000.00).build());
        testData.add(Person.builder().name("Анна").age(25).salary(55000.00).build());
        testData.add(Person.builder().name("Борис").age(25).salary(50000.00).build());
        testData.add(Person.builder().name("Борис").age(30).salary(45000.00).build());
        testData.add(Person.builder().name("Виктор").age(20).salary(40000.00).build());

        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * Запуск всех тестов
     */
    public static void main(String[] args) {
        SortingStrategiesTest test = new SortingStrategiesTest();
        
        System.out.println("=== Запуск тестов стратегий сортировки ===");
        
        try {
            test.testBubbleSortStrategy_BaseSorting();
            test.testMergeSortStrategy_BaseSorting();
            test.testIterativeQuickSortStrategy_BaseSorting();
            test.testAllStrategies_SortByName();
            test.testAllStrategies_SortByAge();
            test.testAllStrategies_SortBySalary();
            test.testAllStrategies_EmptyList();
            test.testAllStrategies_SingleElement();
            
            System.out.println("=== Все тесты прошли успешно! ===");
        } catch (Exception e) {
            System.err.println("=== Тест провален: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }

    /**
     * Тест базовой сортировки по всем полям для BubbleSortStrategy
     */
    public void testBubbleSortStrategy_BaseSorting() {
        System.out.println("Тест: BubbleSortStrategy - базовая сортировка");
        
        BubbleSortStrategy<Person> strategy = new BubbleSortStrategy<>();
        strategy.setExecutor(executor);
        strategy.setComparator(null); // Используем базовую сортировку

        List<Person> sortedData = new ArrayList<>(testData);
        strategy.sort(sortedData);

        assertSortedByAllFields(sortedData);
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест базовой сортировки по всем полям для MergeSortStrategy
     */
    public void testMergeSortStrategy_BaseSorting() {
        System.out.println("Тест: MergeSortStrategy - базовая сортировка");
        
        MergeSortStrategy<Person> strategy = new MergeSortStrategy<>();
        strategy.setExecutor(executor);
        strategy.setComparator(null); // Используем базовую сортировку

        List<Person> sortedData = new ArrayList<>(testData);
        strategy.sort(sortedData);

        assertSortedByAllFields(sortedData);
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест базовой сортировки по всем полям для IterativeQuickSortStrategy
     */
    public void testIterativeQuickSortStrategy_BaseSorting() {
        System.out.println("Тест: IterativeQuickSortStrategy - базовая сортировка");
        
        IterativeQuickSortStrategy<Person> strategy = new IterativeQuickSortStrategy<>();
        strategy.setExecutor(executor);
        strategy.setComparator(null); // Используем базовую сортировку

        List<Person> sortedData = new ArrayList<>(testData);
        strategy.sort(sortedData);

        assertSortedByAllFields(sortedData);
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест сортировки по имени для всех стратегий
     */
    public void testAllStrategies_SortByName() {
        System.out.println("Тест: все стратегии - сортировка по имени");
        
        List<SortStrategy<Person>> strategies = List.of(
            new BubbleSortStrategy<>(),
            new MergeSortStrategy<>(),
            new IterativeQuickSortStrategy<>()
        );

        for (SortStrategy<Person> strategy : strategies) {
            strategy.setExecutor(executor);
            strategy.setComparator(new Person.NameComparator());

            List<Person> sortedData = new ArrayList<>(testData);
            strategy.sort(sortedData);

            assertSortedByName(sortedData);
        }
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест сортировки по возрасту для всех стратегий
     */
    public void testAllStrategies_SortByAge() {
        System.out.println("Тест: все стратегии - сортировка по возрасту");
        
        List<SortStrategy<Person>> strategies = List.of(
            new BubbleSortStrategy<>(),
            new MergeSortStrategy<>(),
            new IterativeQuickSortStrategy<>()
        );

        for (SortStrategy<Person> strategy : strategies) {
            strategy.setExecutor(executor);
            strategy.setComparator(new Person.AgeComparator());

            List<Person> sortedData = new ArrayList<>(testData);
            strategy.sort(sortedData);

            assertSortedByAge(sortedData);
        }
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест сортировки по зарплате для всех стратегий
     */
    public void testAllStrategies_SortBySalary() {
        System.out.println("Тест: все стратегии - сортировка по зарплате");
        
        List<SortStrategy<Person>> strategies = List.of(
            new BubbleSortStrategy<>(),
            new MergeSortStrategy<>(),
            new IterativeQuickSortStrategy<>()
        );

        for (SortStrategy<Person> strategy : strategies) {
            strategy.setExecutor(executor);
            strategy.setComparator(new Person.SalaryComparator());

            List<Person> sortedData = new ArrayList<>(testData);
            strategy.sort(sortedData);

            assertSortedBySalary(sortedData);
        }
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест сортировки пустого списка
     */
    public void testAllStrategies_EmptyList() {
        System.out.println("Тест: все стратегии - пустой список");
        
        List<SortStrategy<Person>> strategies = List.of(
            new BubbleSortStrategy<>(),
            new MergeSortStrategy<>(),
            new IterativeQuickSortStrategy<>()
        );

        for (SortStrategy<Person> strategy : strategies) {
            strategy.setExecutor(executor);

            List<Person> emptyList = new ArrayList<>();
            strategy.sort(emptyList);

            assertTrue("Пустой список должен остаться пустым", emptyList.isEmpty());
        }
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест сортировки списка с одним элементом
     */
    public void testAllStrategies_SingleElement() {
        System.out.println("Тест: все стратегии - список с одним элементом");
        
        List<SortStrategy<Person>> strategies = List.of(
            new BubbleSortStrategy<>(),
            new MergeSortStrategy<>(),
            new IterativeQuickSortStrategy<>()
        );

        Person singlePerson = Person.builder().name("Тест").age(25).salary(50000.00).build();

        for (SortStrategy<Person> strategy : strategies) {
            strategy.setExecutor(executor);

            List<Person> singleElementList = new ArrayList<>();
            singleElementList.add(singlePerson);
            strategy.sort(singleElementList);

            assertEquals("Список с одним элементом должен содержать тот же элемент", 
                1, singleElementList.size());
            assertEquals("Элемент должен остаться тем же", 
                singlePerson, singleElementList.get(0));
        }
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Проверяет, что список отсортирован по всем полям (имя -> возраст -> зарплата)
     */
    private void assertSortedByAllFields(List<Person> sortedList) {
        for (int i = 0; i < sortedList.size() - 1; i++) {
            Person current = sortedList.get(i);
            Person next = sortedList.get(i + 1);

            int nameComparison = current.getName().compareTo(next.getName());
            if (nameComparison > 0) {
                fail("Список не отсортирован по имени: " + current.getName() + " > " + next.getName());
            } else if (nameComparison == 0) {
                int ageComparison = Integer.compare(current.getAge(), next.getAge());
                if (ageComparison > 0) {
                    fail("Список не отсортирован по возрасту при одинаковых именах: " + 
                         current.getAge() + " > " + next.getAge());
                } else if (ageComparison == 0) {
                    int salaryComparison = Double.compare(current.getSalary(), next.getSalary());
                    if (salaryComparison > 0) {
                        fail("Список не отсортирован по зарплате при одинаковых именах и возрастах: " + 
                             current.getSalary() + " > " + next.getSalary());
                    }
                }
            }
        }
    }

    /**
     * Проверяет, что список отсортирован по имени
     */
    private void assertSortedByName(List<Person> sortedList) {
        for (int i = 0; i < sortedList.size() - 1; i++) {
            String currentName = sortedList.get(i).getName();
            String nextName = sortedList.get(i + 1).getName();
            assertTrue("Список не отсортирован по имени: " + currentName + " > " + nextName,
                currentName.compareTo(nextName) <= 0);
        }
    }

    /**
     * Проверяет, что список отсортирован по возрасту
     */
    private void assertSortedByAge(List<Person> sortedList) {
        for (int i = 0; i < sortedList.size() - 1; i++) {
            int currentAge = sortedList.get(i).getAge();
            int nextAge = sortedList.get(i + 1).getAge();
            assertTrue("Список не отсортирован по возрасту: " + currentAge + " > " + nextAge,
                currentAge <= nextAge);
        }
    }

    /**
     * Проверяет, что список отсортирован по зарплате
     */
    private void assertSortedBySalary(List<Person> sortedList) {
        for (int i = 0; i < sortedList.size() - 1; i++) {
            double currentSalary = sortedList.get(i).getSalary();
            double nextSalary = sortedList.get(i + 1).getSalary();
            assertTrue("Список не отсортирован по зарплате: " + currentSalary + " > " + nextSalary,
                currentSalary <= nextSalary);
        }
    }

    // Вспомогательные методы для тестирования

    private void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private void assertEquals(String message, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(String message, Person expected, Person actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void fail(String message) {
        throw new AssertionError(message);
    }
}