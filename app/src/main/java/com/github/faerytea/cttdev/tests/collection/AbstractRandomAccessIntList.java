package com.github.faerytea.cttdev.tests.collection;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.RandomAccess;

public abstract class AbstractRandomAccessIntList extends AbstractIntList implements RandomAccess {
    @NonNull
    @Override
    public Object[] toArray() {
        final int size = size();
        Object[] res = new Object[size];
        for (int i = 0; i < size; ++i) {
            res[i] = at(i);
        }
        return res;
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] a) {
        final int size = size();
        //noinspection unchecked,ConstantConditions
        final T[] res = a.length <= size
                ? a
                : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; ++i) {
            //noinspection unchecked
            res[i] = (T) Integer.valueOf(at(i));
        }
        if (size < a.length) {
            res[size] = null;
        }
        return res;
    }
}
