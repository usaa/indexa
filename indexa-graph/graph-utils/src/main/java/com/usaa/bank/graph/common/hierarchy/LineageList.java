package com.usaa.bank.graph.common.hierarchy;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is an util class for creating a lineage list. Internally it uses a LinkedList to store the data.
 *
 * @param <T> Type of LineageList to be create i.e. data-types.
 */
public class LineageList<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_DELIMITER = ".";

    private LinkedList<T> lList;
    private String delimiter;

    public LineageList() {
        this.lList = new LinkedList<T>();
        this.delimiter = LineageList.DEFAULT_DELIMITER;
    }

    /**
     * Use a custom delimiter to be used while creating the linage list.
     *
     * Default delimiter used is "."
     *
     * @param delimiter - Custom delimiter string.
     */
    public LineageList(String delimiter) {
        this.lList = new LinkedList<T>();
        this.delimiter = delimiter;
    }

    /**
     * Get the delimiter used in the linage list.
     *
     * @return - A string representing the delimiter.
     */
    public String getDelimiter() {
        return this.delimiter;
    }

    /**
     * Add a entry to the front of the linage list.
     *
     * @param t - Object to be added.
     */
    public void pushParent(T t) {
        this.lList.addFirst(t);
    }

    /**
     * Removes and returns the first entry present in the linage list.
     *
     * @return - Object removed from the linage list.
     */
    public T popParent() {
        return this.lList.removeFirst();
    }

    /**
     * Adds a entry to the end of the linage list.
     *
     * @param t - Object to be added.
     */
    public void queueChild(T t) {
        this.lList.addLast(t);
    }

    /**
     * Removes and returns the last entry present in the linage list.
     *
     * @return - Object removed from the linage list.
     */
    public T dequeueChild() {
        return this.lList.removeLast();
    }

    /**
     * Get an iterator for the linage list that iterates from first to last.
     *
     * @return - A iterator for the linage list.
     */
    public Iterator<T> getParentsFirstIterator() {
        return this.lList.iterator();
    }

    /**
     * * Get an iterator for the linage list that iterates from last to first.
     *
     * @return - A iterator for the linage list.
     */
    public Iterator<T> getChildrenFirstIterator() {
        return this.lList.descendingIterator();
    }

    /**
     * Get the linage list created as a List.
     *
     * @return - A List representing the linage.
     */
    public List<T> asList() {
        return Collections.unmodifiableList(this.lList);
    }

    /**
     * Check if the linage list contains the given object.
     *
     * @param t - Object to be searched for.
     * @return - A boolean based on it presenece. True if found else false.
     */
    public boolean contains(T t) {
        return this.lList.contains(t);
    }

    /**
     * Create a String where each entry is delimited by the custom delimiter or default delimiter.
     *
     * @return - A lineageList represented as string.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<T> it = this.getParentsFirstIterator();
        if (it != null) {
            if (it.hasNext()) {
                sb.append(it.next());
            }
            while (it.hasNext()) {
                sb.append(this.getDelimiter());
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

}