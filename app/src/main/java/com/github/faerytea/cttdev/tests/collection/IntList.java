package com.github.faerytea.cttdev.tests.collection;

import java.util.List;

public interface IntList extends List<Integer> {

    boolean contains(int x);

    int[] toIntArray();

    boolean add(int integer);

    boolean removeElement(int x);

    boolean containsAll(int... data);

    boolean addAll(int index, int... elements);

    boolean addPart(int index, int from, int count, int... elements);

    int at(int index);

    int set(int index, int element);

    void add(int index, int element);

    int removeAt(int index);

    int indexOf(int x);

    int lastIndexOf(int x);

    void sort();
}
