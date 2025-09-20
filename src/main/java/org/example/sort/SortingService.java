package org.example.sort;

import FileWriter.FileWriterUtil;
import org.example.strategy.SortStrategy;
import org.example.utils.SortTask;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SortingService {
    public static <T extends Comparable<T>> List<T> sortArray(List<T> list, SortStrategy<T> strategy, Function<T, Number> getEvenValue) {

        if (getEvenValue != null) {
            EvenNumbersNaturalOrder.EvenRecord<T> evenRecord = EvenNumbersNaturalOrder.getEvenNumbersRecord(list, getEvenValue);

            // Сортируем чётные элементы
            if(sortArray(evenRecord.evenElements, strategy)==null) return null;

            // Вставляем отсортированные элементы на их места
            for (int i = 0; i < evenRecord.evenIndices.size(); i++) {
                int index = evenRecord.evenIndices.get(i);
                list.set(index, evenRecord.evenElements.get(i));
            }
            System.out.println("Возраст только с чётным значением отсортирован");

            return list;
        }

        return sortArray(list, strategy);
    }

    public static <T extends Comparable<T>> List<T> sortArray(List<T> list, SortStrategy<T> strategy) {
        try {
            System.out.println("Начало сортировки...");
            List<T> sortedList;

            sortedList = new SortTask<>(list, strategy).call();

            System.out.println("Сортировка завершена");
            System.out.println("Использован алгоритм: " + strategy.getStrategyName());

            return sortedList;
        } catch (Exception e) {
            System.out.println("Ошибка при сортировке: " + e.getMessage());
        }
        return null;
    }

    public static void saveSortedCollectionToFile(String filename,List sortedList) {
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