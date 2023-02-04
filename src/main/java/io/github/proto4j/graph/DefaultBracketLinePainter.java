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
 * Default implementation of a simple {@code BracketLinePainter}.
 *
 * @see BracketUtil
 */
public class DefaultBracketLinePainter
        implements BracketLinePainter {

    /**
     * Draws lines according to the current {@code paintMode} set in the
     * {@code BracketConstraints} of the given {@code JBracketView}.
     *
     * @param view the {@code JBracketView} we're painting
     * @param gs the graphics context
     * @param column the current column index
     * @param row the cells row index
     */
    @Override
    public void paintLine(JBracketView<?> view, Graphics gs,
                          int column, int row) {
        BracketConstraints bc = view.getConstraints();
        if (bc != null) {
            switch (bc.paintMode) {
                case BracketConstraints.BELOW: {
                    BracketUtil.paintLineBelow(view, gs, column, row);
                    break;
                }
                case BracketConstraints.ABOVE: {
                    BracketUtil.paintLineAbove(view, gs, column, row);
                    break;
                }
                case BracketConstraints.CENTER: {
                    BracketUtil.paintLineCentered(view, gs, column, row);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected mode: " + bc.paintMode);
            }
        }
    }

}
