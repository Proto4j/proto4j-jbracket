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

package io.github.proto4j.graph;//@date 07.01.2023

import java.awt.*;

/**
 * Identifies components that can be used as "rubber stamps" to paint
 * the cells in a {@code JBracketView}. For example, to use a JLabel as a
 * BracketCellRenderer, you would write something like this:
 * <pre>
 *  {@code
 *  class MyCellRenderer extends JLabel implements BracketCellRenderer<Object> {
 *       public MyCellRenderer() {
 *           setOpaque(true);
 *       }
 *
 *       public Component getBracketCellRendererComponent(
 *             JBracketView<? extends T> bracketView,
 *             T value,
 *             int columnIndex,
 *             int rowIndex) {
 *
 *          setText(value.toString());
 *          return this;
 *      }
 *  }
 * }
 * </pre>
 *
 * @param <E> the type of values this renderer can be used for
 * @see JBracketView
 */
public interface BracketCellRenderer<E> {

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.
     *
     * @param bracketView the {@code JBracketView} we're painting
     * @param value the value at the given column and row index
     * @param columnIndex the column index
     * @param rowIndex the cells row index
     * @return A component whose paint() method will render the specified value.
     *
     * @see JBracketView
     * @see BracketUtil
     */
    Component getBracketCellRendererComponent(
            JBracketView<? extends E> bracketView,
            E value,
            int columnIndex,
            int rowIndex);
}
