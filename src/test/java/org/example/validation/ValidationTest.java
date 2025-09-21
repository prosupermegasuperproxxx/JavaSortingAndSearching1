package org.example.validation;

import org.example.model.Person;

/**
 * Простые тесты для валидации данных Person
 * Реализованы без использования JUnit
 */
public class ValidationTest {

    /**
     * Запуск всех тестов
     */
    public static void main(String[] args) {
        ValidationTest test = new ValidationTest();
        
        System.out.println("=== Запуск тестов валидации ===");
        
        try {
            test.testValidPerson();
            test.testValidation_EmptyName();
            test.testValidation_NullName();
            test.testValidation_WhitespaceName();
            test.testValidation_AgeTooYoung();
            test.testValidation_AgeTooOld();
            test.testValidation_AgeBoundaryValues();
            test.testValidation_NegativeSalary();
            test.testValidation_SalaryTooHigh();
            test.testValidation_SalaryBoundaryValues();
            test.testValidation_AllFieldsInvalid();
            test.testValidation_MultipleFieldsInvalid();
            test.testCompareTo_AllFields();
            test.testToString();
            
            System.out.println("=== Все тесты прошли успешно! ===");
        } catch (Exception e) {
            System.err.println("=== Тест провален: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }

    /**
     * Тест валидации корректных данных
     */
    public void testValidPerson() {
        System.out.println("Тест: валидация корректных данных");
        
        Person person = Person.builder()
            .name("Иван Петров")
            .age(25)
            .salary(50000.50)
            .build();

        assertNotNull("Person должен создаваться с валидными данными", person);
        assertEquals("Иван Петров", person.getName());
        assertEquals(25, person.getAge());
        assertEquals(50000.50, person.getSalary(), 0.01);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест валидации имени - пустая строка
     */
    public void testValidation_EmptyName() {
        System.out.println("Тест: валидация имени - пустая строка");
        
        try {
            Person.builder()
                .name("")
                .age(25)
                .salary(50000.00)
                .build();
            fail("Должно было быть выброшено исключение для пустого имени");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации имени - null
     */
    public void testValidation_NullName() {
        System.out.println("Тест: валидация имени - null");
        
        try {
            Person.builder()
                .name(null)
                .age(25)
                .salary(50000.00)
                .build();
            fail("Должно было быть выброшено исключение для null имени");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации имени - только пробелы
     */
    public void testValidation_WhitespaceName() {
        System.out.println("Тест: валидация имени - только пробелы");
        
        try {
            Person.builder()
                .name("   ")
                .age(25)
                .salary(50000.00)
                .build();
            fail("Должно было быть выброшено исключение для имени из пробелов");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации возраста - меньше 18
     */
    public void testValidation_AgeTooYoung() {
        System.out.println("Тест: валидация возраста - меньше 18");
        
        try {
            Person.builder()
                .name("Иван")
                .age(17)
                .salary(50000.00)
                .build();
            fail("Должно было быть выброшено исключение для возраста меньше 18");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации возраста - больше 65
     */
    public void testValidation_AgeTooOld() {
        System.out.println("Тест: валидация возраста - больше 65");
        
        try {
            Person.builder()
                .name("Иван")
                .age(66)
                .salary(50000.00)
                .build();
            fail("Должно было быть выброшено исключение для возраста больше 65");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации возраста - граничные значения
     */
    public void testValidation_AgeBoundaryValues() {
        System.out.println("Тест: валидация возраста - граничные значения");
        
        // Минимальный возраст
        Person youngPerson = Person.builder()
            .name("Иван")
            .age(18)
            .salary(50000.00)
            .build();
        assertNotNull("Person с возрастом 18 должен создаваться", youngPerson);

        // Максимальный возраст
        Person oldPerson = Person.builder()
            .name("Петр")
            .age(65)
            .salary(50000.00)
            .build();
        assertNotNull("Person с возрастом 65 должен создаваться", oldPerson);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест валидации зарплаты - отрицательная
     */
    public void testValidation_NegativeSalary() {
        System.out.println("Тест: валидация зарплаты - отрицательная");
        
        try {
            Person.builder()
                .name("Иван")
                .age(25)
                .salary(-1000.00)
                .build();
            fail("Должно было быть выброшено исключение для отрицательной зарплаты");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации зарплаты - больше 1000000
     */
    public void testValidation_SalaryTooHigh() {
        System.out.println("Тест: валидация зарплаты - больше 1000000");
        
        try {
            Person.builder()
                .name("Иван")
                .age(25)
                .salary(1000001.00)
                .build();
            fail("Должно было быть выброшено исключение для зарплаты больше 1000000");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации зарплаты - граничные значения
     */
    public void testValidation_SalaryBoundaryValues() {
        System.out.println("Тест: валидация зарплаты - граничные значения");
        
        // Минимальная зарплата
        Person lowSalaryPerson = Person.builder()
            .name("Иван")
            .age(25)
            .salary(0.00)
            .build();
        assertNotNull("Person с зарплатой 0 должен создаваться", lowSalaryPerson);

        // Максимальная зарплата
        Person highSalaryPerson = Person.builder()
            .name("Петр")
            .age(30)
            .salary(1000000.00)
            .build();
        assertNotNull("Person с зарплатой 1000000 должен создаваться", highSalaryPerson);
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест валидации - все поля некорректны
     */
    public void testValidation_AllFieldsInvalid() {
        System.out.println("Тест: валидация - все поля некорректны");
        
        try {
            Person.builder()
                .name("")           // Пустое имя
                .age(15)            // Слишком молодой
                .salary(1500000.00) // Слишком высокая зарплата
                .build();
            fail("Должно было быть выброшено исключение для всех некорректных полей");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест валидации - несколько полей некорректны
     */
    public void testValidation_MultipleFieldsInvalid() {
        System.out.println("Тест: валидация - несколько полей некорректны");
        
        try {
            Person.builder()
                .name("Иван")
                .age(15)            // Слишком молодой
                .salary(1500000.00) // Слишком высокая зарплата
                .build();
            fail("Должно было быть выброшено исключение для некорректных полей");
        } catch (IllegalArgumentException e) {
            // Ожидаемое исключение
            System.out.println("✓ Тест прошел успешно");
        }
    }

    /**
     * Тест сравнения Person с учетом всех полей
     */
    public void testCompareTo_AllFields() {
        System.out.println("Тест: сравнение Person с учетом всех полей");
        
        Person person1 = Person.builder().name("Анна").age(25).salary(50000.00).build();
        Person person2 = Person.builder().name("Анна").age(30).salary(50000.00).build();
        Person person3 = Person.builder().name("Анна").age(25).salary(55000.00).build();
        Person person4 = Person.builder().name("Борис").age(25).salary(50000.00).build();

        // Сравнение по имени
        assertTrue("Анна должна быть меньше Бориса", person1.compareTo(person4) < 0);
        assertTrue("Борис должен быть больше Анны", person4.compareTo(person1) > 0);

        // Сравнение по возрасту при одинаковых именах
        assertTrue("25 лет должно быть меньше 30 лет", person1.compareTo(person2) < 0);
        assertTrue("30 лет должно быть больше 25 лет", person2.compareTo(person1) > 0);

        // Сравнение по зарплате при одинаковых именах и возрастах
        assertTrue("50000 должно быть меньше 55000", person1.compareTo(person3) < 0);
        assertTrue("55000 должно быть больше 50000", person3.compareTo(person1) > 0);

        // Равные объекты
        Person person5 = Person.builder().name("Анна").age(25).salary(50000.00).build();
        assertEquals("Одинаковые объекты должны быть равны", 0, person1.compareTo(person5));
        
        System.out.println("✓ Тест прошел успешно");
    }

    /**
     * Тест toString метода
     */
    public void testToString() {
        System.out.println("Тест: toString метод");
        
        Person person = Person.builder()
            .name("Иван Петров")
            .age(25)
            .salary(50000.75)
            .build();

        String result = person.toString();
        assertTrue("toString должен содержать имя", result.contains("Иван Петров"));
        assertTrue("toString должен содержать возраст", result.contains("25"));
        assertTrue("toString должен содержать зарплату", result.contains("50000.75"));
        
        System.out.println("✓ Тест прошел успешно");
    }

    // Вспомогательные методы для тестирования

    private void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    private void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    private void assertEquals(String message, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError(message + " - ожидалось: " + expected + ", получено: " + actual);
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

    private void fail(String message) {
        throw new AssertionError(message);
    }
}