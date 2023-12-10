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

import java.awt.*;

/**
 * The {@code BracketConstraints} class specifies constraints either for the
 * {@code BracketLocator} or {@code BracketLinePainter} to use when painting
 * the parent {@code JBracketView}.
 *
 * @see BracketLocator
 * @see JBracketView
 */
public final class BracketConstraints
        implements BracketLocator {

    /**
     * Specifies that the used {@code BracketLinePainter} should paint a
     * line above according to the next bracket.
     *
     * @see BracketUtil#paintLineAbove(JBracketView, Graphics, int, int)
     */
    public static final int ABOVE = 0;

    /**
     * Specifies that the used {@code BracketLinePainter} should paint a
     * centered line to the next bracket.
     *
     * @see BracketUtil#paintLineCentered(JBracketView, Graphics, int, int)
     */
    public static final int CENTER = 1;

    /**
     * Specifies that the used {@code BracketLinePainter} should paint a
     * centered line to the next bracket.
     *
     * @see BracketUtil#paintLineBelow(JBracketView, Graphics, int, int)
     */
    public static final int BELOW = 2;

    /**
     * Specifies the default bracket height to use for all brackets within the
     * {@code JBracketView}.
     *
     * @see BracketConstraints#bracketWidth
     */
    public int bracketHeight;

    /**
     * Specifies the default bracket width to use for all brackets within the
     * {@code JBracketView}.
     *
     * @see BracketConstraints#bracketHeight
     */
    public int bracketWidth;

    /**
     * Specifies the amount of pixels the lines in the {@code JBracketView}
     * are wide.
     */
    public int lineThickness;

    /**
     * Specifies the current line painting mode.
     *
     * @see BracketConstraints#ABOVE
     * @see BracketConstraints#CENTER
     * @see BracketConstraints#BELOW
     */
    public int paintMode;

    /**
     * Specifies the initial space that will be computed on the X-axis.
     *
     * @see BracketConstraints#ipadY
     */
    public int ipadX;

    /**
     * Specifies the initial space that will be computed on the Y-axis.
     *
     * @see BracketConstraints#ipadX
     */
    public int ipadY;

    /**
     * Specifies the X-axis space between brackets (or space between two
     * columns).
     */
    public int spaceX;

    /**
     * Specifies the Y-axis space between brackets (or space between two
     * brackets/cells).
     */
    public int spaceY;

    /**
     * Creates a {@code BracketConstraints} object with all of its fields set
     * to the passed-in arguments.
     *
     * @param bracketHeight The initial bracketHeight value.
     * @param bracketWidth The initial bracketWidth value.
     * @param lineThickness The initial lineThickness value.
     * @param paintMode The initial paintMode value.
     * @param ipadX The initial ipadX value.
     * @param ipadY The initial ipadY value.
     * @param spaceX The initial spaceX value.
     * @param spaceY The initial spaceY value.
     */
    public BracketConstraints(
            int bracketHeight, int bracketWidth, int lineThickness,
            int paintMode, int ipadX, int ipadY, int spaceX,
            int spaceY) {
        this.bracketHeight = bracketHeight;
        this.bracketWidth  = bracketWidth;
        this.lineThickness = lineThickness;
        this.paintMode     = paintMode;
        this.ipadX         = ipadX;
        this.ipadY         = ipadY;
        this.spaceX        = spaceX;
        this.spaceY        = spaceY;
    }

    /**
     * Creates a {@code BracketConstraints} object with all of its fields set
     * to their default value.
     */
    public BracketConstraints() {
        paintMode = CENTER;
    }

    /**
     * Computes the relative X-position of the given column. This implementation
     * adds some extra space at the start point of the X-axis,
     *
     * @param columnIndex the column index
     * @return the relative X-position of the given column
     */
    public int getX(int columnIndex, BracketConstraints constraints) {
        return ipadX + ((bracketWidth + spaceX) * columnIndex);
    }

    /**
     * {@inheritDoc}
     *
     * @param columnIndex the column index
     * @param rowIndex the cell's row index
     * @return {@inheritDoc}
     */
    public int getY(int columnIndex, int rowIndex, BracketConstraints constraints) {
        if (columnIndex == 0) {
            return ipadY + ((bracketHeight + spaceY) * rowIndex);
        } else {
            int pos  = rowIndex * 2;
            int yMin = getY(columnIndex - 1, pos, constraints);
            int yMax = getY(columnIndex - 1, pos + 1, constraints);

            return (yMax + yMin) / 2;
        }
    }
}
