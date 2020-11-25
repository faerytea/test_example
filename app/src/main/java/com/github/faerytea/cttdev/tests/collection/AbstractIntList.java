package com.github.faerytea.cttdev.tests.collection;

import androidx.annotation.NonNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

abstract class AbstractIntList extends AbstractList<Integer> implements IntList {
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        final Integer val = checkInt(o);
        return val != null && contains(val.intValue());
    }

    @Override
    public boolean add(Integer integer) {
        return add(integer.intValue());
    }

    @Override
    public boolean remove(Object o) {
        final Integer val = checkInt(o);
        return val != null && removeElement(val);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(int... data) {
        for (int x : data) {
            if (!contains(x)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        if (c.size() < 32) {
            boolean changed = false;
            for (Integer e : c) {
                if (e != null) changed |= add(e.intValue());
            }
            return changed;
        } else {
            return addAll(size(), c);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
        final int[] toInsert = new int[c.size()];
        int cSize = 0;
        for (Integer e : c) {
            if (e != null) toInsert[cSize++] = e;
        }
        return addPart(index, 0, cSize, toInsert);
    }

    public boolean addAll(int index, int... elements) {
        return addPart(index, 0, elements.length, elements);
    }

    @Override
    public Integer get(int index) {
        return at(index);
    }

    @Override
    public Integer set(int index, Integer element) {
        return set(index, element.intValue());
    }

    @Override
    public void add(int index, Integer element) {
        add(index, element.intValue());
    }

    @Override
    public Integer remove(int index) {
        return removeAt(index);
    }

    @Override
    public int indexOf(Object o) {
        Integer n = checkInt(o);
        return n == null ? -1 : indexOf(n.intValue());
    }

    @Override
    public int lastIndexOf(Object o) {
        Integer n = checkInt(o);
        return n == null ? -1 : lastIndexOf(n.intValue());
    }

    @NonNull
    @Override
    public Iterator<Integer> iterator() {
        return listIterator();
    }

    @NonNull
    @Override
    public ListIterator<Integer> listIterator() {
        return listIterator(0);
    }

    public static Integer checkInt(Object o) {
        if (!(o instanceof Number)) return null;
        int val = ((Number) o).intValue();
        if (!o.equals(val)) return null;
        return val;
    }
}
