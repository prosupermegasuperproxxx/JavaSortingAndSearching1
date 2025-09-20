package org.example.customcollection;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomList<T> implements Iterable<T> {

    public static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size; // current size
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    public CustomList() {
        this.elements = DEFAULTCAPACITY_EMPTY_ELEMENTDATA; // this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public CustomList(Collection<? extends T> c) {
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c.getClass() == ArrayList.class) {
                elements = a;
            } else {
                elements = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // replace with empty array.
            elements = EMPTY_ELEMENTDATA;
        }
    }

    /**
     <code>
     List<Person> personList = new ArrayList<>();
     <br>
     return CustomList.fromStream (Arrays.stream(personList.toArray(new Person[0])));
     <br>
     </code>
     @param stream
     @param <T>
     @return
     */
    public static <T> CustomList<T> fromStream(Stream<T> stream) {
        return new CustomList<>(stream.collect(Collectors.toList()));
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public int size() { return size; }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            grow(minCapacity);
        }
    }

    private Object[] grow(int minCapacity) {
        int newCapacity = Math.max(elements.length * 2, minCapacity);
        elements = Arrays.copyOf(elements, newCapacity);
        return elements;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public boolean add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        T oldValue = (T) elements[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        --size;
        elements[size] = null;
        return oldValue;
    }

    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new IllegalStateException("No more elements");
                }
                return (T) elements[currentIndex++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    public boolean addAll(List<T>/*Collection<? extends T>*/ list) {
        Object[] a = list.toArray();

        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elements).length - (s = size))
            elementData = grow(s + numNew);
        System.arraycopy(a, 0, elementData, s, numNew);
        size = s + numNew;
        return true;

    }

    public List<T> toList() {
        List<T> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add((T) elements[i]);
        }
        return arrayList;
    }

    public static <T> Collector<T, ?, CustomList<T>> getCollector2() {
        return Collector.of(
                CustomList::new,
                CustomList::add,
                (left, right) -> { left.addAll(right.toList()); return left; },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }

    public static <T> Collector<T, ?, CustomList<T>> getCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    CustomList<T> customList = new CustomList<>();
                    customList.addAll(list);
                    return customList;
                }
        );
    }

}