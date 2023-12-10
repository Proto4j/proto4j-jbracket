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

/**
 * <b>Proto4j-JBracket</b>
 * <p>
 * A small and simple Swing-Component to create dynamic tournament bracket
 * views. A view usually consists of the following main components:
 * <ul>
 *     <li>{@code BracketModel}: data model</li>
 *     <li>{@code BracketCellRenderer}: cell renderer</li>
 *     <li>{@code BracketLinePainter}: line painter</li>
 * </ul>
 * These components must be provided to use the {@code JBracketView}
 * correctly. By default, the view can be created as follows:
 * <pre>
 * {@code
 *     JBracketView<T> view = new JBracketView<>(new DefaultBracketModel<>());
 *     view.setLinePainter(new DefaultBracketLinePainter());
 *
 *     // to use the view in a JScrollPane, just add it to its viewport
 *     JScrollPane scrollPane = new JScrollPane(view);
 * }
 * </pre>
 * Whereby the {@code T} can be replaced by any element type that should be
 * stored. At least the {@code BracketCellRenderer} should support displaying
 * the elements' type.
 *
 * @see io.github.proto4j.graph.JBracketView
 **/
package io.github.proto4j.graph;