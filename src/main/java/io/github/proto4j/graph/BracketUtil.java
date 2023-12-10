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
 * Small collection of methods to simplify the painting of lines in a
 * {@code JBracketView}.
 *
 * @see JBracketView
 */
public final class BracketUtil {

    // instance creation not allowed
    private BracketUtil() {}

    /**
     * Paints a line above next bracket.
     * <p>
     * The following view should illustrate how this utility method will paint
     * the bracket line:
     * <pre>
     * +-----------+
     * | Bracket 1 +------------+
     * +-----------+            |
     *                    +-----+-----+
     *                    | Bracket 3 |
     *                    +-----+-----+
     * +-----------+            |
     * | Bracket 2 +------------+
     * +-----------+
     * </pre>
     * There will be always two lines painted either above or below the bracket
     * in the next column. The first line starts at the following point:
     * <pre>
     *     x := bracket1.x + view.bracketWidth
     *     y := bracket1.y + (view.bracketHeight / 2)
     * </pre>
     *
     * @param view the bracket view component
     * @param gs the graphics context to paint in
     * @param column the current column
     * @param row the current row
     */
    public static void paintLineAbove(JBracketView<?> view, Graphics gs, int column, int row) {
        BracketConstraints constraints = view.getConstraints();
        BracketLocator locator = view.getLocator();
        if (constraints == null || view.getModel().getColumnCount() - 1 == column) return;

        int baseX = locator.getX(column, constraints) + constraints.bracketWidth;
        int baseY = locator.getY(column, row, constraints) + (constraints.bracketHeight / 2);

        int width = constraints.spaceX + (constraints.bracketWidth / 2);

        int endY   = locator.getY(column + 1, row / 2, constraints);
        int height = endY - baseY;

        gs.setColor(view.getLineColor());
        gs.fillRect(baseX, baseY, width, constraints.lineThickness);
        if (row % 2 == 0) {
            gs.fillRect(baseX + width, baseY, constraints.lineThickness, height + constraints.lineThickness);
        } else {
            // correct the starting Y coordinate:
            // Y-------+ -> The Y coordinate should be at the bottom left
            // |Bracket|    edge of the bracket.
            // +-------+
            endY += constraints.bracketHeight;

            height = Math.max(height, baseY - endY);
            gs.fillRect(baseX + width, endY, constraints.lineThickness, height + constraints.lineThickness);
        }
    }

    /**
     * Paints a centered line to the next bracket (below the current).
     * <p>
     * The following view should illustrate how this utility method will paint
     * the bracket line:
     * <pre>
     * +-----------+
     * | Bracket 1 |
     * +-----+-----+
     *       |            +-----------+
     *       +------------+ Bracket 3 |
     *       |            +-----------+
     * +-----+-----+
     * | Bracket 2 |
     * +-----------+
     * </pre>
     * Note that only brackets with even row indices will be used to paint these
     * lines.
     *
     * @param view the bracket view component
     * @param gs the graphics context to paint in
     * @param column the current column
     * @param row the current row
     */
    public static void paintLineBelow(JBracketView<?> view, Graphics gs, int column, int row) {
        BracketConstraints constraints = view.getConstraints();
        BracketLocator locator = view.getLocator();
        if (constraints == null || locator == null) return;

        if (row % 2 != 0 || view.getModel().getColumnCount() - 1 == column) return;

        int baseX = locator.getX(column, constraints) + (constraints.bracketWidth / 2);
        int baseY = locator.getY(column, row, constraints) + constraints.bracketHeight;

        int endY   = locator.getY(column, row + 1, constraints);
        int height = endY - baseY;
        int width  = (constraints.bracketWidth / 2) + constraints.spaceX;
        int midY   = (baseY + endY) / 2;

        gs.setColor(view.getLineColor());
        gs.fillRect(baseX, baseY, constraints.lineThickness, height);
        gs.fillRect(baseX, midY, width, constraints.lineThickness);
    }

    /**
     * Paints a centered line to the next bracket.
     * <p>
     * The following view should illustrate how this utility method will paint
     * the bracket line:
     * <pre>
     * +-----------+
     * | Bracket 1 +--+
     * +-----------+  |
     *                |   +-----------+
     *                +---+ Bracket 3 |
     *                |   +-----------+
     * +-----------+  |
     * | Bracket 2 +--+
     * +-----------+
     * </pre>
     *
     * @param view the bracket view component
     * @param gs the graphics context to paint in
     * @param column the current column
     * @param row the current row
     */
    public static void paintLineCentered(JBracketView<?> view, Graphics gs, int column, int row) {
        BracketConstraints constraints = view.getConstraints();
        BracketLocator locator = view.getLocator();
        if (constraints == null || (row % 2 != 0)
                || view.getModel().getColumnCount() - 1 == column) {
            return;
        }

        int baseX = locator.getX(column, constraints) + constraints.bracketWidth;
        int baseY = locator.getY(column, row, constraints) + (constraints.bracketHeight / 2);

        int endY = locator.getY(column, row + 1, constraints) + (constraints.bracketHeight / 2);

        int width  = constraints.spaceX / 2;
        int height = (endY - baseY) + constraints.lineThickness;

        gs.setColor(view.getLineColor());
        gs.fillRect(baseX, baseY, width, constraints.lineThickness);
        gs.fillRect(baseX, endY, width, constraints.lineThickness);

        gs.fillRect(baseX + width, baseY, constraints.lineThickness, height);

        gs.fillRect(baseX + width, (endY + baseY) / 2, width, constraints.lineThickness);
    }
}
