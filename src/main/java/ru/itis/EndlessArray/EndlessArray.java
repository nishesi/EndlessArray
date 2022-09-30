package ru.itis.EndlessArray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings({"unused", "unchecked"})
public class EndlessArray<T> implements Iterable<T> {
  protected final static int MAX_LENGTH = 10;
  protected final static int DEFAULT_INITIAL_SIZE = 5;
  protected final Class<? extends Object[]> TYPE;
  protected T[] array;
  protected int clientLen;

  public EndlessArray() {
    initNewArr();
    TYPE = array.getClass();
  }

  public static int capacity() {
    return MAX_LENGTH;
  }

  public final int length() {
    return this.clientLen;
  }

  public final void clear() {
    initNewArr();
  }

  public final boolean isEmpty() {
    return clientLen == 0;
  }

  public final int indexOf(T element) {
    int index = -1;
    for (int i = 0; i < clientLen; i++) {
      if (element.equals(array[i])) {
        return index;
      }
    }
    return index;
  }

  public final T get(int index) {
    if (isValidIndex(index)) {
      return array[index];
    } else {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  public final void add(T element) {
    try {
      if (clientLen == this.array.length) {
        increaseLen();
      }
      array[clientLen] = element;
      clientLen++;
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new ArrayStoreException("Array is full");
    }
  }

  public final void addTo(int index, T element) {
    if (isValidIndex(index)) {
      if (clientLen >= MAX_LENGTH) {
        throw new ArrayStoreException("Array is full!");
      }
      T lastElement = array[clientLen - 1];
      for (int i = clientLen - 1; i > index; i--) {
        array[i] = array[i - 1];
      }
      array[index] = element;
      this.add(lastElement);
    } else {
      throw new IllegalArgumentException("Index out of bounds");
    }
  }

  public final void set(int index, T element) {
    if (isValidIndex(index)) {
      array[index] = element;
    } else {
      throw new IllegalArgumentException("Index out of bounds");
    }
  }

  public final T removeFrom(int index) {
    if (isValidIndex(index)) {
      T removedElement = array[index];
      for (int i = index; i < clientLen - 1; i++) {
        array[i] = array[i + 1];
      }
      array[clientLen - 1] = null;
      clientLen--;
      return removedElement;
    } else {
      throw new IllegalArgumentException("Index out of bounds");
    }
  }

  public final boolean remove(T element) {
    int index = this.indexOf(element);
    if (index != -1) {
      this.removeFrom(index);
      return true;
    } else {
      return false;
    }
  }

  public final void deleteFrom(int index) {
    this.removeFrom(index);
  }

  public final void delete(T element) {
    this.remove(element);
  }

  public final T[] toArray() {
    return Arrays.copyOf(array, clientLen);
  }

  public final EndlessArray<T> copy() {
    EndlessArray<T> copy = new EndlessArray<>();
    for (int i = 0; i < clientLen; i++) {
      copy.add( array[i]);
    }
    return copy;
  }

  @Override
  public String toString() {
    if (clientLen == 0) {
      return "[]";
    } else {
      StringBuilder strRepresent = new StringBuilder("[" + array[0] + "]");
      for (int i = 1; i < clientLen; i++) {
        strRepresent.insert(strRepresent.length() - 1, ", " + this.array[i]);
      }
      return strRepresent.toString();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    } else if (obj == this) {
      return true;
    } else {

      EndlessArray<?> arr2 = (EndlessArray<?>) obj;
      if (this.clientLen != arr2.length() || array.getClass() != arr2.getType()) {
        return false;
      } else {
        for (int i = 0; i < clientLen; i++) {
          if (!array[i].equals(arr2.get(i))) {
            return false;
          }
        }
        return true;
      }
    }
  }

  public Class getType() {
    return TYPE;
  }

  @Override
  public int hashCode() {
    int base = 31;
    for (int i = 0; i < clientLen; i++) {
      base = base * 17 + array[i].hashCode();
    }
    return base;
  }

  private void increaseLen() {
    int newLen = (int) (this.array.length * 1.5);
    if (newLen > MAX_LENGTH) {
      newLen = MAX_LENGTH;
    }
    this.array = Arrays.copyOf(this.array, newLen);
  }

  private boolean isValidIndex(int i) {
    return (i >= 0 && i < clientLen);
  }

  private void initNewArr() {
    this.array = (T[]) (new Object[DEFAULT_INITIAL_SIZE]);
    clientLen = 0;
  }

  public int compareTo(EndlessArray<?> obj) {
    return this.clientLen - obj.length();
  }

  @Override
  public Iterator<T> iterator() {
    return new EndlessArrayIterator<>(this);
  }

  private class EndlessArrayIterator<E> implements Iterator<E> {
    private int cursor;
    EndlessArray<T> arr;

    EndlessArrayIterator(EndlessArray<T> arr) {
      cursor = 0;
      this.arr = arr;
    }

    @Override
    public boolean hasNext() {
      while(cursor < clientLen) {
        if (arr.get(cursor) != null) {
          return true;
        } else {
          cursor++;
        }
      }
      return false;
    }

    @Override
    public E next() {
      try {
        E element = (E)arr.get(cursor);
        cursor++;
        return element;
      } catch (ArrayIndexOutOfBoundsException ex) {
        throw new NoSuchElementException("");
      }
    }
  }

  private class EndlessArrayIterator2<E> implements Iterator<E> {
    protected EndlessArray<T> arr;
    protected int cursor;
    protected int endCursor;
    protected boolean side = true;

    EndlessArrayIterator2(EndlessArray<T> arr) {
      this.arr = arr;
      cursor = 0;
      endCursor = arr.clientLen-1;
    }

    @Override
    public boolean hasNext() {
      return cursor < endCursor;
    }

    @Override
    public E next() {
      if (this.hasNext()) {
        int currentCursor;
        if (side) {
          currentCursor = cursor;
          cursor++;
        } else {
          currentCursor = endCursor;
          endCursor--;
        }
        side = !side;

        return (E) arr.get(currentCursor);
      } else {
        throw new ArrayIndexOutOfBoundsException();
      }
    }
  }

  public static class EndlessArrayComparator {
    public static int compare(EndlessArray<?> first, EndlessArray<?> second) {
      return first.compareTo(second);
    }
  }
}

