package org.example.model;

public class Person implements Comparable<Person> {
    private final String name;
    private final int age;
    private final double salary;
    public static final int COUNT_COLUMNS = 3;

    //7. Класс объекта
    private Person(PersonBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.salary = builder.salary;
    }


    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }


    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }


    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, salary=%.2f}",
                name, age, salary);
    }


    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {
        private String name;
        private int age;
        private double salary;

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder age(int age) {
            this.age = age;
            return this;
        }

        public PersonBuilder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Person build() {
            validate();
            return new Person(this);
        }

        private void validate() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (age <= 0) {
                throw new IllegalArgumentException("Age must be positive");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
        }
    }
}