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

package io.github.proto4j.graph;//@date 12.01.2023

/**
 * Manages the computing of each bracket position in the {@link JBracketView}
 * container. The default algorithm is implemented in {@link BracketConstraints}.
 * <p>
 * A basic algorithm to position the cells in the next column centered
 * according to their previous column candidates:
 * <pre>
 * {@code
 * class MyBracketLocator implements BracketLocator {
 *     int bracketHeight;
 *     int bracketWidth;
 *     int spaceX;
 *     int spaceY;
 *
 *     public int getX(int colIndex) {
 *         return (bracketWidth + spaceX) * colIndex;
 *     }
 *
 *     public int getY(int colIndex, int rowIndex) {
 *         if (columnIndex == 0) {
 *             return ((bracketHeight + spaceY) * rowIndex);
 *         } else {
 *             int pos = rowIndex * 2;
 *             int yMin = getY(columnIndex - 1, pos);
 *             int yMax = getY(columnIndex - 1, pos + 1);
 *             return (yMax + yMin) / 2;
 *         }
 *     }
 * }
 * }
 * </pre>
 *
 * @see BracketConstraints
 * @see JBracketView
 */
public interface BracketLocator {

    /**
     * Creates a new {@code BracketLocator} that positions the brackets/cells
     * of a {@code JBracketView} as a grid.
     *
     * @return a {@code BracketLocator} that calculates positions of brackets
     *         in a grid.
     */
    public static BracketLocator asGrid() {
        return new BracketLocator() {
            @Override
            public int getX(int column, BracketConstraints constraints) {
                return constraints.ipadX + (column * (constraints.bracketWidth + constraints.spaceX));
            }

            @Override
            public int getY(int column, int row, BracketConstraints constraints) {
                return constraints.ipadY + (row * (constraints.bracketHeight + constraints.spaceY));
            }
        };
    }

    /**
     * Computes the relative X-position of the given column.
     *
     * @param column the column index
     * @param constraints the current bracket constraints
     * @return the relative X-position of the given column
     */
    public int getX(final int column, BracketConstraints constraints);

    /**
     * Computes the relative Y-Position of the given cell/bracket.
     *
     * @param column the column index
     * @param row the cell's row index
     * @param constraints the current bracket constraints
     * @return the relative Y-Position of the given cell/bracket
     */
    public int getY(final int column, final int row, BracketConstraints constraints);
}
