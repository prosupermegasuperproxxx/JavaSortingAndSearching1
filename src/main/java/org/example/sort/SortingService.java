package org.example.sort;

import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

import java.util.Comparator;
import java.util.List;

public class SortingService {
    public <T extends Comparable<T>> List<T> sortArray(List<T> list, Comparator<T> comparator, SortStrategy<T> strategy) {
        try {
            System.out.println("Начало сортировки...");
            List<T> sortedList;

            sortedList = new SortTask<>(list, strategy).call();

//            System.out.println("Отсортированный список: " + sortedList);
            System.out.println("Сортировка завершена");
            System.out.println("Использован алгоритм: " + strategy.getStrategyName());


            // Запись в файл (Доп задание 2)
//            if (getBooleanInput("Записать результат в файл? (y/n): ")) {
//                String filename = getStringInput("Введите имя файла: ");
//                FileUtil.writeToFile(filename, sortedList, true);
//                System.out.println("Результат записан в файл: " + filename);
//            }
            return sortedList;
        } catch (Exception e) {
            System.out.println("Ошибка при сортировке: " + e.getMessage());
        }
        return null;
    }
}