/*
 * MIT License
 *
 * Copyright (c) 2023 Proto4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.proto4j.graph; //@date 07.01.2023

import java.util.*;

/**
 * Default {@code BracketModel} that used a {@code LinkedList} to store all
 * columns.
 *
 * @param <E> the data type stored in each cell
 *
 * @see BracketModel
 */
public class DefaultBracketModel<E>
        implements BracketModel<E> {

    /**
     * A {@code LinkedList} of {@code Column} objects that store the data
     * of this model.
     */
    private final List<Column<E>> columns = new LinkedList<>();

    /**
     * Creates a new {@code DefaultBracketModel} with the amount of columns.
     *
     * @param columns the amount of columns to create
     */
    public DefaultBracketModel(final int columns) {
        for (int i = 0; i < columns; i++) {
            this.columns.add(new Column<>());
        }
    }

    /**
     * Returns the column count for this model.
     *
     * @return the element count
     */
    @Override
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Returns the number of rows in the specified column.
     *
     * @param columnIndex the column index
     * @return the row count for the specified column
     */
    @Override
    public int getRowCount(int columnIndex) {
        return columnIndex >= getColumnCount() ? 0 : columns.get(columnIndex).size();
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param columnIndex the column whose value is to be queried
     * @param rowIndex the row whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public E getValueAt(int columnIndex, int rowIndex) {
        Column<E> column = getColumn(columnIndex);

        if (column != null) {
            return column.size() > rowIndex ? column.get(rowIndex) : null;
        }
        return null;
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>value</code>.
     *
     * @param value the new value
     * @param rowIndex the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     */
    @Override
    public void setValueAt(E value, int columnIndex, int rowIndex) {
        Column<E> column = getColumn(columnIndex);
        if (column != null) {
            if (column.size() <= rowIndex) {
                column.add(value);
            } else {
                column.set(rowIndex, value);
            }
        }
    }

    /**
     * Returns the column at the given column index or {@code null} if the
     * index is invalid.
     *
     * @param columnIndex the column index
     * @return the column at the given column index or {@code null} if the
     *         index is invalid.
     */
    protected final Column<E> getColumn(int columnIndex) {
        return columnIndex >= getColumnCount()
                ? null
                : columns.get(columnIndex);
    }

    /**
     * Small wrapper class for a synchronized {@code ArrayList}.
     *
     * @param <E> the element type
     */
    protected static class Column<E> {

        private final List<E> brackets =
                Collections.synchronizedList(new ArrayList<>());

        public int size() {
            return brackets.size();
        }

        public boolean isEmpty() {
            return brackets.isEmpty();
        }

        public boolean contains(E o) {
            return brackets.contains(o);
        }

        public void add(E element) {
            brackets.add(element);
        }

        public void set(int index, E element) {
            brackets.set(index, element);
        }

        public boolean addAll(Collection<? extends E> c) {
            return brackets.addAll(c);
        }

        public E get(int index) {
            return brackets.get(index);
        }

    }
}
