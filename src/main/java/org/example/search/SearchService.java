package org.example.search;

import org.example.model.Person;

public class SearchService {
    public Person searchPerson(Person[] array, String key) {
        for (Person person : array) {
            if (person.getName().contains(key)) {
                return person;
            }
        }
        return null;
    }
}