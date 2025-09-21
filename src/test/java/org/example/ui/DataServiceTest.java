package org.example.ui;

import org.example.customcollection.CustomList;
import org.example.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Простые тесты для DataService с проверкой работы стримов и валидации
 * Реализованы без использования JUnit
 */
public class DataServiceTest {

    private final DataService<Person> dataService;
    private static final String TEST_FILE_PATH = "test_persons.txt";
    private static final String INVALID_FILE_PATH = "invalid_persons.txt";

    public DataServiceTest() {
        dataService = new DataService<>();
    }

    /**
     * Запуск всех тестов
     */
    public static void main(String[] args) {
        DataServiceTest test = new DataServiceTest();
        
        System.out.println("=== Запуск тестов DataService ===");
        
        try {
            test.testFillFromFile_ValidData();
            test.testFillFromFile_InvalidData();
            test.testFillFromFile_NonExistentFile();
            test.testFillFromFile_EmptyFile();
            test.testFillRandomly();
            test.testFillRandomly_ZeroSize();
            test.testBaseSorting_AllFields();
            test.testPersonValidation();
            
            System.out.println("=== Все тесты прошли успешно! ===");
        } catch (Exception e) {
            System.err.println("=== Тест провален: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }

    /**
     * Тест заполнения из файла с корректными данными
     */
    public void testFillFromFile_ValidData() throws IOException {
        System.out.println("Тест: заполнение из файла с корректными данными");
        
        // Создаем тестовый файл с корректными данными
        createTestFile(TEST_FILE_PATH, 
            "Иван,25,50000.50\n" +
            "Мария,30,60000.75\n" +
            "Петр,22,45000.00\n"
        );

        try {
            CustomList<Person> result = dataService.fillFromFile(TEST_FILE_PATH);

            assertNotNull("Результат не должен быть null", result);
            assertEquals("Должно быть 3 элемента", 3, result.size());

            // Проверяем первый элемент
            Person firstPerson = result.get(0);
            assertEquals("Имя должно быть 'Иван'", "Иван", firstPerson.getName());
            assertEquals("Возраст должен быть 25", 25, firstPerson.getAge());
            assertEquals("Зарплата должна быть 50000.50", 50000.50, firstPerson.getSalary(), 0.01);

            System.out.println("✓ Тест прошел успешно");
        } finally {
            // Удаляем тестовый файл
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        }
    }

    /**
     * Тест заполнения из файла с некорректными данными
     */
    public void testFillFromFile_InvalidData() throws IOException {
        System.out.println("Тест: заполнение из файла с некорректными данными");
        
        // Создаем тестовый файл с некорректными данными
        createTestFile(INVALID_FILE_PATH,
            "Иван,25,50000.50\n" +           // Корректная строка
            "Мария,abc,60000.75\n" +         // Некорректный возраст
            "Петр,22,45000.00\n" +           // Корректная строка
            "Анна,15,30000.00\n" +           // Некорректный возраст (меньше 18)
            "Олег,30,1500000.00\n" +         // Некорректная зарплата (больше 1000000)
            "Сергей,35,70000.25\n"           // Корректная строка
        );

        try {
            CustomList<Person> result = dataService.fillFromFile(INVALID_FILE_PATH);

            assertNotNull("Результат не должен быть null", result);
            // Должны остаться только корректные записи
            assertEquals("Должно быть 3 корректных элемента", 3, result.size());

            // Проверяем, что все элементы валидны
            for (Person person : result) {
                assertTrue("Имя не должно быть пустым", 
                    person.getName() != null && !person.getName().trim().isEmpty());
                assertTrue("Возраст должен быть от 18 до 65", 
                    person.getAge() >= 18 && person.getAge() <= 65);
                assertTrue("Зарплата должна быть от 0 до 1000000", 
                    person.getSalary() >= 0 && person.getSalary() <= 1000000);
            }

            System.out.println("✓ Тест прошел успешно");
        } finally {
            // Удаляем тестовый файл
            Files.deleteIfExists(Paths.get(INVALID_FILE_PATH));
        }
    }

    /**
     * Тест заполнения из несуществующего файла
     */
    public void testFillFromFile_NonExistentFile() {
        System.out.println("Тест: заполнение из несуществующего файла");
        
        CustomList<Person> result = dataService.fillFromFile("non_existent_file.txt");
        
        assertNotNull("Результат не должен быть null", result);
        assertTrue("Результат должен быть пустым", result.size() == 0);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест заполнения из пустого файла
     */
    public void testFillFromFile_EmptyFile() throws IOException {
        System.out.println("Тест: заполнение из пустого файла");
        
        createTestFile(TEST_FILE_PATH, "");

        try {
            CustomList<Person> result = dataService.fillFromFile(TEST_FILE_PATH);
            
            assertNotNull("Результат не должен быть null", result);
            assertTrue("Результат должен быть пустым", result.size() == 0);

            System.out.println("✓ Тест прошел успешно");
        } finally {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        }
    }

    /**
     * Тест заполнения случайными данными
     */
    public void testFillRandomly() {
        System.out.println("Тест: заполнение случайными данными");
        
        int size = 10;
        CustomList<Person> result = dataService.fillRandomly(size);

        assertNotNull("Результат не должен быть null", result);
        assertEquals("Размер должен соответствовать запрошенному", size, result.size());

        // Проверяем, что все элементы валидны
        for (Person person : result) {
            assertTrue("Имя не должно быть пустым", 
                person.getName() != null && !person.getName().trim().isEmpty());
            assertTrue("Возраст должен быть от 18 до 65", 
                person.getAge() >= 18 && person.getAge() <= 65);
            assertTrue("Зарплата должна быть от 0 до 100000", 
                person.getSalary() >= 0 && person.getSalary() <= 100000);
        }
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест заполнения случайными данными с размером 0
     */
    public void testFillRandomly_ZeroSize() {
        System.out.println("Тест: заполнение случайными данными с размером 0");
        
        CustomList<Person> result = dataService.fillRandomly(0);
        
        assertNotNull("Результат не должен быть null", result);
        assertTrue("Результат должен быть пустым", result.size() == 0);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест базовой сортировки по всем полям
     */
    public void testBaseSorting_AllFields() {
        System.out.println("Тест: базовая сортировка по всем полям");
        
        // Создаем список с разными значениями для проверки сортировки
        CustomList<Person> persons = new CustomList<>();
        persons.add(Person.builder().name("Анна").age(25).salary(50000.00).build());
        persons.add(Person.builder().name("Анна").age(30).salary(45000.00).build());
        persons.add(Person.builder().name("Анна").age(25).salary(55000.00).build());
        persons.add(Person.builder().name("Борис").age(25).salary(50000.00).build());

        List<Person> sortedList = persons.toList();
        sortedList.sort(Person::compareTo);

        // Проверяем сортировку по имени (первый приоритет)
        assertEquals("Анна", sortedList.get(0).getName());
        assertEquals("Анна", sortedList.get(1).getName());
        assertEquals("Анна", sortedList.get(2).getName());
        assertEquals("Борис", sortedList.get(3).getName());

        // Проверяем сортировку по возрасту (второй приоритет) для одинаковых имен
        assertEquals(25, sortedList.get(0).getAge());
        assertEquals(25, sortedList.get(1).getAge());
        assertEquals(30, sortedList.get(2).getAge());

        // Проверяем сортировку по зарплате (третий приоритет) для одинаковых имен и возрастов
        assertEquals(50000.00, sortedList.get(0).getSalary(), 0.01);
        assertEquals(55000.00, sortedList.get(1).getSalary(), 0.01);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест валидации Person
     */
    public void testPersonValidation() {
        System.out.println("Тест: валидация Person");
        
        // Тест валидного Person
        Person validPerson = Person.builder()
            .name("Иван")
            .age(25)
            .salary(50000.00)
            .build();
        
        assertNotNull("Валидный Person должен создаваться", validPerson);
        assertEquals("Иван", validPerson.getName());
        assertEquals(25, validPerson.getAge());
        assertEquals(50000.00, validPerson.getSalary(), 0.01);
        
        System.out.println("✓ Тест прошел успешно");
    }

    // Вспомогательные методы для тестирования

    private void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    private void assertEquals(String message, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(String message, String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(String message, double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError("Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Вспомогательный метод для создания тестового файла
     */
    private void createTestFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content.getBytes());
    }
}