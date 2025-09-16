package org.example.model;

import org.example.sort.HasAge;

import java.util.Locale;

public class Person implements Comparable<Person>, HasAge {
    private final String name;
    private int age;
    private final double salary;
    public static final int COUNT_COLUMNS = 3;
    
    
    public static final int DOUBLE_AFTERDOT = 3;
    // Умножаем на 10^decimalPlaces и округляем до целых
    public static final long factor = (long) Math.pow(10, DOUBLE_AFTERDOT);    
    

//    В программе должен использоваться паттерн стратегия.
//    Для кастомной сортировки разрешено использовать компаратор.
    public static class NameComparator implements PersonComparator {
        @Override
        public int compare(Person p1, Person p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }

    public static class AgeComparator implements PersonComparator {
        @Override
        public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }
    }

    public static class SalaryComparator implements PersonComparator {
        @Override
        public int compare(Person p1, Person p2) {
//            return Double.compare(p1.getSalary(), p2.getSalary());
            return isAlmostEqual(p1.getSalary(), p2.getSalary());
        }
    }
    
//    public static class FullComparator implements PersonComparator {
//        @Override
//        public int compare(Person p1, Person p2) {
//            int nameComparison = p1.getName().compareTo(p2.getName());
//            if (nameComparison != 0) return nameComparison;
//            int ageComparison = Integer.compare(p1.getAge(), p2.getAge());
//            if (ageComparison != 0) return ageComparison;
//            //return Double.compare(p1.getSalary(), p2.getSalary());
//            return isAlmostEqual(p1.getSalary(), p2.getSalary());
//        }
//    }
   
    
    //7. Класс объекта
    private Person(PersonBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.salary = builder.salary;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }


//    Все классы должны базово реализовывать сортировку по всем 3 полям.
//    используется если не задан PersonComparator
    @Override
    public int compareTo(Person other) {
        int nameComparison = this.getName().compareTo(other.getName());
        if (nameComparison != 0) return nameComparison;
        int ageComparison = Integer.compare(this.getAge(), other.getAge());
        if (ageComparison != 0) return ageComparison;
//        return Double.compare(this.getSalary(), other.getSalary());
        return isAlmostEqual(this.getSalary(), other.getSalary() );
    }


    private static int isAlmostEqual(double a, double b) {
        // Умножаем на 10^decimalPlaces и округляем до целых
//        long factor = (long) Math.pow(10, DOUBLE_AFTERDOT);
        return Long.compare(Math.round(a * factor) , Math.round(b * factor));
    }

    
    @Override
    public String toString() {
        return String.format(Locale.US,"Person{name='%s', age=%d, salary=%."+DOUBLE_AFTERDOT+"f}",
                name, age, ((double)Math.round(salary * factor)/factor));
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