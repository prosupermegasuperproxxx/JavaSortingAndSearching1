package org.example.sort;

import FileWriter.FileWriterUtil;
import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class SortingService {
    public <T extends Comparable<T>> List<T> sortArray(List<T> list, SortStrategy<T> strategy, java.util.function.Function<T, Number> getEvenValue) {

        if (getEvenValue != null) {
            EvenNumbersNaturalOrder.EvenRecord<T> evenRecord = EvenNumbersNaturalOrder.getEvenNumbersRecord(list, getEvenValue);

            // Сортируем чётные элементы
            sortArray(evenRecord.evenElements, strategy);

            // Вставляем отсортированные элементы на их места
            for (int i = 0; i < evenRecord.evenIndices.size(); i++) {
                int index = evenRecord.evenIndices.get(i);
                list.set(index, evenRecord.evenElements.get(i));
            }

            return list;
        }

        List<T> sortedArray = sortArray(list, strategy);

        return sortedArray;
    }

    public <T extends Comparable<T>> List<T> sortArray(List<T> list, SortStrategy<T> strategy) {
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

    public void saveSortedCollectionToFile(String filename,List sortedList) {
        if (sortedList == null || sortedList.isEmpty()) {
            System.out.println("Нет отсортированной коллекции для сохранения!");
            return;
        }

        try {
            FileWriterUtil.writeCollectionToFile(sortedList, filename);
            System.out.println("Коллекция успешно сохранена в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

}