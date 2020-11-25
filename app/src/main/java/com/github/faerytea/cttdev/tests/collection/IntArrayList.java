package com.github.faerytea.cttdev.tests.collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IntArrayList extends AbstractRandomAccessIntList {
    private int size = 0;
    private int[] data;

    public IntArrayList() {
        this(4);
    }

    public IntArrayList(int initialCapacity) {
        data = new int[initialCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(int x) {
        return indexOf(x) != -1;
    }

    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    public boolean add(int integer) {
        ensureCapacity(size + 1);
        data[size] = integer;
        ++size;
        return true;
    }

    @Override
    public boolean removeElement(int x) {
        final int ix = indexOf(x);
        if (ix == -1) return false;
        removeAt(ix);
        return true;
    }

    @Override
    public boolean addPart(int index, int from, int count, int... elements) {
        checkRangeInsertion(index);
        ensureCapacity(size + count);
        System.arraycopy(data, index, data, index + count, size - index);
        System.arraycopy(elements, from, data, index, count);
        size += count;
        return true;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public int at(int index) {
        checkRange(index);
        return data[index];
    }

    @Override
    public int set(int index, int element) {
        checkRange(index);
        final int prev = data[index];
        data[index] = element;
        return prev;
    }

    @Override
    public void add(int index, int element) {
        checkRangeInsertion(index);
        if (index == size) {
            add(element);
        } else {
            ensureCapacity(size + 1);
            System.arraycopy(data, index, data, index + 1, size - index);
            data[index] = element;
            ++size;
        }
    }

    @Override
    public int removeAt(int index) {
        checkRange(index);
        final int prev = data[index];
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        --size;
        return prev;
    }

    @Override
    public int indexOf(int x) {
        return indexOf(x, 0, size);
    }

    public int indexOf(int x, int from, int to) {
        for (int i = from; i < to; i++) {
            if (x == data[i]) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int x) {
        return lastIndexOf(x, 0, size);
    }

    public int lastIndexOf(int x, int from, int to) {
        for (int i = to - 1; i >= from; --i) {
            if (x == data[i]) return i;
        }
        return -1;
    }

    @NonNull
    @Override
    public ListIterator<Integer> listIterator(int index) {
        return new IntArrayListIterator(this, index, 0, size);
    }

    @NonNull
    @Override
    public List<Integer> subList(int fromIndex, int toIndex) {
        return new SubList(fromIndex, toIndex);
    }

    @Override
    public void sort() {
        Arrays.sort(data);
    }

    public void shrink() {
        if (size == data.length) return;
        final int[] newData = new int[size];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    protected final void ensureCapacity(int capacity) {
        if (capacity > data.length) {
            final int[] newData = new int[Math.max(data.length << 1, capacity)];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    protected final void checkRange(int ix) {
        if (ix < 0 || ix >= size) throw new IndexOutOfBoundsException(ix + " not in 0.." + size);
    }

    protected final void checkRangeInsertion(int ix) {
        if (ix < 0 || ix > size) throw new IndexOutOfBoundsException(ix + " not in 0.." + size);
    }

    public class IntArrayListIterator implements ListIterator<Integer> {
        protected final int start;
        protected final int end;
        protected final IntList modDelegate;
        protected int pos;
        protected int lastReturned = -1;

        protected IntArrayListIterator(IntList modDelegate, int pos, int start, int end) {
            this.modDelegate = modDelegate;
            this.pos = pos;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            return pos < end;
        }

        @Override
        public Integer next() {
            return nextInt();
        }

        public int nextInt() {
            if (!hasNext()) throw new NoSuchElementException("reached the end");
            lastReturned = pos;
            return data[pos++];
        }

        @Override
        public boolean hasPrevious() {
            return pos > start;
        }

        @Override
        public Integer previous() {
            return previousInt();
        }

        public int previousInt() {
            if (!hasPrevious()) throw new NoSuchElementException("reached the end");
            lastReturned = pos - 1;
            return data[--pos];
        }

        @Override
        public int nextIndex() {
            return pos - start;
        }

        @Override
        public int previousIndex() {
            return pos - 1 - start;
        }

        @Override
        public void remove() {
            checkLast();
            modDelegate.removeAt(lastReturned - start);
            lastReturned = -1;
        }

        @Override
        public void set(Integer integer) {
            set(checkNull(integer));
        }

        public void set(int integer) {
            checkLast();
            data[lastReturned] = integer;
        }

        @Override
        public void add(Integer integer) {
            add(checkNull(integer));
            ++pos;
        }

        public void add(int x) {
            modDelegate.add(pos - start, x);
        }

        protected void checkLast() {
            if (lastReturned == -1) throw new IllegalStateException("next() hasn't called");
        }

        protected int checkNull(Integer integer) {
            if (integer == null) throw new IllegalArgumentException("this list does not support nulls");
            return integer;
        }
    }

    protected class SubList extends AbstractRandomAccessIntList {
        protected final int start;
        protected int end;

        public SubList(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean contains(int x) {
            return indexOf(x) != -1;
        }

        @Override
        public int[] toIntArray() {
            final int[] res = new int[end - start];
            System.arraycopy(data, start, res, 0, res.length);
            return res;
        }

        @Override
        public boolean add(int integer) {
            add(end, integer);
            return true;
        }

        @Override
        public boolean removeElement(int x) {
            final int ix = indexOf(x);
            if (ix == -1) return false;
            removeAt(ix);
            return true;
        }

        @Override
        public boolean addPart(int index, int from, int count, int... elements) {
            final boolean res = IntArrayList.this.addPart(index + start, from + start, count, elements);
            end += count;
            return res;
        }

        @Override
        public int at(int index) {
            return IntArrayList.this.at(index + start);
        }

        @Override
        public int set(int index, int element) {
            return IntArrayList.this.set(index + start, element);
        }

        @Override
        public void add(int index, int element) {
            IntArrayList.this.add(index + start, element);
            ++end;
        }

        @Override
        public int removeAt(int index) {
            final int res = IntArrayList.this.removeAt(index + start);
            --end;
            return res;
        }

        @Override
        public int indexOf(int x) {
            return IntArrayList.this.indexOf(x, start, end);
        }

        @Override
        public int lastIndexOf(int x) {
            return IntArrayList.this.lastIndexOf(x, start, end);
        }

        @Override
        public int size() {
            return end - start;
        }

        @Override
        public void clear() {
            System.arraycopy(data, end, data, start, end - start);
            IntArrayList.this.size -= end - start;
            end = start;
        }

        @NonNull
        @Override
        public ListIterator<Integer> listIterator(int index) {
            return new IntArrayListIterator(this, index, start, end);
        }

        @NonNull
        @Override
        public List<Integer> subList(int fromIndex, int toIndex) {
            return new SubList(fromIndex + start, Math.min(end, toIndex + start));
        }

        @Override
        public void sort() {
            Arrays.sort(data, start, end);
        }
    }
}
