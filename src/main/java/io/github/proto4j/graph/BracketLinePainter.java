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
 * Classes implementing this interface are used to draw the lines between
 * different brackets. Some default painting implementations are given in
 * the {@code BracketUtil} class.
 *
 * @see BracketUtil
 * @see DefaultBracketLinePainter
 */
public interface BracketLinePainter {

    /**
     * Draws one line (or more) in the given graphics context according to
     * the given location specified by column and row.
     *
     * @param view the {@code JBracketView} we're painting
     * @param gs the graphics context
     * @param column the current column index
     * @param row the cells row index
     */
    void paintLine(JBracketView<?> view, Graphics gs, int column, int row);
}
