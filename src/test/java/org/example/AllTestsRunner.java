package org.example;

import org.example.ui.DataServiceTest;
import org.example.sort.SortingStrategiesTest;
import org.example.validation.ValidationTest;

/**
 * Главный класс для запуска всех тестов
 * Демонстрирует работу всех улучшенных функций:
 * - Ввод через стримы с валидацией
 * - Базовая сортировка по всем полям
 * - Обработка ошибок
 */
public class AllTestsRunner {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("    ЗАПУСК ВСЕХ ТЕСТОВ ПРОЕКТА");
        System.out.println("==========================================");
        System.out.println();
        
        boolean allTestsPassed = true;
        
        try {
            // Тесты DataService (стримы и валидация)
            System.out.println("1. ТЕСТИРОВАНИЕ DataService (стримы и валидация)");
            System.out.println("================================================");
            DataServiceTest dataServiceTest = new DataServiceTest();
            dataServiceTest.main(args);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("❌ Тесты DataService провалились: " + e.getMessage());
            allTestsPassed = false;
        }
        
        try {
            // Тесты стратегий сортировки
            System.out.println("2. ТЕСТИРОВАНИЕ СТРАТЕГИЙ СОРТИРОВКИ");
            System.out.println("====================================");
            SortingStrategiesTest sortingTest = new SortingStrategiesTest();
            sortingTest.main(args);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("❌ Тесты стратегий сортировки провалились: " + e.getMessage());
            allTestsPassed = false;
        }
        
        try {
            // Тесты валидации
            System.out.println("3. ТЕСТИРОВАНИЕ ВАЛИДАЦИИ");
            System.out.println("========================");
            ValidationTest validationTest = new ValidationTest();
            validationTest.main(args);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("❌ Тесты валидации провалились: " + e.getMessage());
            allTestsPassed = false;
        }
        
        // Итоговый результат
        System.out.println("==========================================");
        if (allTestsPassed) {
            System.out.println("✅ ВСЕ ТЕСТЫ ПРОШЛИ УСПЕШНО!");
            System.out.println();
            System.out.println("РЕАЛИЗОВАННЫЕ УЛУЧШЕНИЯ:");
            System.out.println("• Ввод через стримы с валидацией данных");
            System.out.println("• Базовая сортировка по всем 3 полям (имя → возраст → зарплата)");
            System.out.println("• Надежная обработка ошибок для стримов");
            System.out.println("• Улучшенная валидация при чтении из файла");
            System.out.println("• Ручной ввод с использованием стримов");
            System.out.println("• Полное тестовое покрытие всех функций");
        } else {
            System.out.println("❌ НЕКОТОРЫЕ ТЕСТЫ ПРОВАЛИЛИСЬ!");
        }
        System.out.println("==========================================");
    }
}
