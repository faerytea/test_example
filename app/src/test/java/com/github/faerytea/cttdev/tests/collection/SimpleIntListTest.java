package com.github.faerytea.cttdev.tests.collection;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleIntListTest {
    private IntList list;

    @Before
    public void setUp() {
        list = new IntArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
    }

    @After
    public void tearDown() {
        list = null;
    }

    @Test
    public void size() {
        assertEquals(3, list.size());
    }

    @Test
    public void containsWhenContains() {
        assertTrue(list.contains(2));
    }

    @Test
    public void containsWhenDoesNotContains() {
        assertFalse(list.contains(-1));
    }

    @Test
    public void add() {
        assertTrue(list.add(1));
        assertEquals(4, list.size());
        assertEquals(1, list.at(3));
    }

    @Test
    public void removeElement() {
        assertTrue(list.removeElement(2));
        assertFalse(list.removeElement(2));
    }

    @Test
    public void clear() {
        list.clear();
        //noinspection ConstantConditions
        assertTrue(list.isEmpty());
    }

    @Test
    public void at() {
        assertEquals(1, list.at(0));
        assertEquals(2, list.at(1));
        assertEquals(3, list.at(2));
    }

    @Test
    public void set() {
        assertEquals(1, list.set(0, 7));
        assertEquals(7, list.at(0));
    }

    @Test
    public void removeAt() {
        assertEquals(1, list.removeAt(0));
        assertEquals(2, list.size());
    }

    @Test
    public void indexOfExistent() {
        assertEquals(1, list.indexOf(2));
    }

    @Test
    public void indexOfNonExistent() {
        assertEquals(-1, list.indexOf(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsAccess() {
        list.at(10);
    }

    @Test
    public void iteration() {
        final List<Integer> trusted = new ArrayList<>(3);
        for (Integer e : list) {
            trusted.add(e);
        }
        assertThat(trusted, Matchers.contains(1, 2, 3));
    }

    @Test
    public void collectionsEquality() {
        final List<Integer> trusted = new ArrayList<>(3);
        trusted.addAll(list);
        assertEquals(list, trusted);
    }
}