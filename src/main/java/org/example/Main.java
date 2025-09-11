package org.example;

import org.example.customcollection.CustomList;
import org.example.model.Person;
import org.example.utils.FileUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println( 
        System.getProperty("user.dir")
        
        );
        CustomList<Person> customList = FileUtil.readPersonsFromFile("persons.txt");
        
//        CustomList<String> customList = new CustomList<>();

//        customList.add("0");
//        customList.add("1");
//        customList.add("2");
//        customList.add("3");
//        customList.add("4");

        customList.forEach(System.out::println);
//        for (String s : customList) {
//            System.out.println(s);
//        }

        System.out.println("Size: " + customList.size()); // Output: Size: 3

//        for (String s : customList) {
//            System.out.print(s + " "); // Output: Hello World !
//        }
//
//        System.out.println("\nElement at index 1: " + customList.get(1)); // Output: Element at index 1: World
//
////        customList.clear();
////        System.out.println("Size after clear: " + customList.size()); // Output: Size after clear: 0
//
//        Object old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
//
//        old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
//
//        old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
//        old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
//        old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
//        old = customList.remove(0);
//        System.out.println("remove: " + old); // Output: Size after clear: 0
//        System.out.println("Size after remove: " + customList.size()); // Output: Size after clear: 0
        
    }
}