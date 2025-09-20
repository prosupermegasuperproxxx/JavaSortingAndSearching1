package org.example.model;

import java.util.Locale;

public class Person implements Comparable<Person> {
    private final String name;
    private final int age;
    private final double salary;
    public static final int COUNT_COLUMNS = 3;


    public static final int DOUBLE_AFTERDOT = 2;
    // Умножаем на 10^decimalPlaces и округляем до целых
    private static final long FACTOR = (long) Math.pow(10, DOUBLE_AFTERDOT);


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
            return isAlmostEqual(p1.getSalary(), p2.getSalary());
        }
    }

    //7. Класс объекта
    public Person(PersonBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.salary = builder.salary;
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }


    //    Все классы должны базово реализовывать сортировку по всем 3 полям.
    //    используется если не задан PersonComparator
    @Override
    public int compareTo(Person other) {
        int nameComparison = this.getName().compareTo(other.getName());
        if (nameComparison != 0) return nameComparison;
        int ageComparison = Integer.compare(this.getAge(), other.getAge());
        if (ageComparison != 0) return ageComparison;
        return isAlmostEqual(this.getSalary(), other.getSalary());
    }

     private static int isAlmostEqual(double a, double b) {
        return Long.compare(Math.round(a * FACTOR), Math.round(b * FACTOR));
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Person{name='%s', age=%d, salary=%." + DOUBLE_AFTERDOT + "f}",
                name, age, ((double) Math.round(salary * FACTOR) / FACTOR));
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

			boolean nameInvalid = (name == null || name.trim().isEmpty());
			boolean ageInvalid = (age < 18 || age > 65);
			boolean salaryInvalid = (salary < 0 || salary > 1_000_000);

			StringBuilder errors = new StringBuilder();

			if (nameInvalid) {
				errors.append("Имя не может быть пустым");
			}
			if (ageInvalid) {
				if (errors.length() > 0) errors.append("; ");
				errors.append("Возраст должен быть от 18 до 65");
			}
			if (salaryInvalid) {
				if (errors.length() > 0) errors.append("; ");
				errors.append("Зарплата должна быть от 0 до 1000000");
			}

			if (nameInvalid && ageInvalid && salaryInvalid) {
				throw new IllegalArgumentException("Все данные некорректны");
			}

			if (errors.length() > 0) {
				throw new IllegalArgumentException(errors.toString());
			}
		}
    }
}