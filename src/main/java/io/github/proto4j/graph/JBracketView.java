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

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

/**
 * A {@code JBracketView} is used to display two-dimensional tables of cells,
 * whereby each column can have a variable amount of rows. A separate model,
 * {@code BracketModel}, maintains the contents of this bracket view.
 * <p>
 * To get a basic understanding of how to use this view to display brackets
 * in different forms, just take a look at the {@code github.proto4j.testing.graph}
 * package and its classes. There will be a brief introduction of hwo to
 * structure the brackets and its model.
 * <p>
 * The computing of each cell's position is done by a {@code BracketLocator}.
 * By default, the {@code BracketConstraints} object is used as the locator
 * if none has been set. It contains a default algorithm to compute the position
 * of each cell according to its row and column.
 * <p>
 * {@code JBracketView} stores a set of attributes in a {@code BracketConstraints}
 * object. By now, the following attributes are supported:
 * <ul>
 *     <li>{@code ipadX}: The initial X-axis space (EmptyBorder.left and right)</li>
 *     <li>{@code ipadY}: The initial Y-axis space (EmptyBorder.top and bottom)</li>
 *     <li>{@code bracketHeight}: The height for each cell</li>
 *     <li>{@code bracketWidth}: The width for each cell</li>
 *     <li>{@code spaceX}: The horizontal space between two cells</li>
 *     <li>{@code spaceY}: The initial vertical space between two cells</li>
 *     <li>{@code lineThickness}: The line width</li>
 *     <li>{@code paintMode}: The line drawing strategy</li>
 * </ul>
 * <p>
 * {@code JBracketView} doesn't implement scrolling directly. To create a view
 * that scrolls, make it the viewport view of a {@code JScrollPane}. For example:
 * <pre>
 *     JScrollPane scrollPane = new JScrollPane(myView);
 *     //or
 *     JScrollPane scrollPane = new JScrollPane();
 *     scrollPane.getViewPort().setView(myView);
 * </pre>
 * <p>
 * In order to paint all brackets efficiently, there is a {@code BracketCellRenderer}
 * that is used as a "rubber stamp" to paint the cells. The default bracket
 * model uses a {@code LinkedList} to store the columns and per column a
 * synchronized {@code ArrayList} to store the value of each cell.
 * <p>
 * Currently, there are three different line drawing methods supported by this
 * {@code JBracketView}. For more information about the line painting, see
 * the {@code BracketUtil} documentation.
 * <p>
 * <b>Important:</b> Eventing and editing will be supported in future versions.
 *
 * @param <T> the type of the elements of this bracket view
 * @see BracketModel
 * @see BracketUtil
 * @see BracketCellRenderer
 * @see BracketLocator
 * @see BracketLinePainter
 */
public class JBracketView<T> extends JComponent
        implements Scrollable, Accessible {

    /**
     * The data model used to store the value of each cell separately. Each
     * column can contain a different amount of rows.
     *
     * @see BracketModel
     */
    private BracketModel<T> dataModel;

    /**
     * The cell renderer used to paint all brackets/cells of this view.
     *
     * @see BracketCellRenderer
     */
    private BracketCellRenderer<? super T> cellRenderer;

    /**
     * The line painter used to draw the lines between the brackets/cells. Note
     * that there will be no lines if no {@code BracketLinePainter} has been
     * set.
     *
     * @see BracketLinePainter
     */
    private BracketLinePainter lineRenderer;

    /**
     * All attributes of this {@code JBracketView} are stored in this separate
     * object. This object will also be used as the {@code BracketLocator} if
     * none has been set via {@link JBracketView#setLocator(BracketLocator)}.
     *
     * @see BracketConstraints
     * @see BracketLocator
     */
    private BracketConstraints constraints;

    /**
     * To be able to paint the cells maintained by this {@code JBracketView},
     * an extra rendering component is used.
     */
    private CellRendererPane rendererPane;

    /**
     * The {@code BracketLocator} is used to compute the relative position of
     * each bracket/cell.
     *
     * @see BracketLocator
     */
    private BracketLocator locator;

    /**
     * The line color used to paint the lines between the brackets/cells.
     */
    private Color lineColor;

    /**
     * Creates an empty view with no model, no cell renderer and no line
     * painter. The {@code BracketConstraints} object is created regardless.
     */
    public JBracketView() {
        setLayout(null);
        createFields();
    }

    /**
     * Creates a new {@code JBracketView} with the given data model.
     *
     * @param dataModel the mode to use
     */
    public JBracketView(BracketModel<T> dataModel) {
        this();
        setModel(dataModel);
    }

    /**
     * Creates a new {@code JBracketView} with the given initial row count. This
     * constructor then calculates how much columns are needed for a simple
     * elimination model.
     * <p>
     * For instance, the initial row count of {@code 4} will produce the following
     * view with the option of elimination enabled:
     * <pre>
     * +---------+
     * | Bracket |
     * +---------+
     *               +---------+
     *               | Bracket |
     *               +---------+
     * +---------+
     * | Bracket |
     * +---------+
     *                            +---------+
     *                            | Bracket |
     *                            +---------+
     * +---------+
     * | Bracket |
     * +---------+
     *               +---------+
     *               | Bracket |
     *               +---------+
     * +---------+
     * | Bracket |
     * +---------+
     * </pre>
     * The lines between these brackets would be painted be the current
     * {@code BracketLinePainter} if one has been set.
     *
     * @param initialRowCount the row count of the first column
     * @param elimination tells the constructor whether the row count of
     *         each column should be decreased
     * @throws IllegalArgumentException if {@code elimination} is enabled and
     *                                  the row count is not a multiple of {@code 2}.
     */
    public JBracketView(final int initialRowCount, boolean elimination)
            throws IllegalArgumentException {
        this();
        int size = initialRowCount;
        if (size % 2 != 0 && elimination) {
            throw new IllegalArgumentException("RowCount has to be a multiple of 2");
        }

        int columnCount = 1;
        if (size > 1 && elimination) {
            do {
                columnCount++;
            } while ((size /= 2) != 1);
        }

        setModel(new DefaultBracketModel<>(columnCount));
        for (int i = 0; i < columnCount; i++) {
            int rowCount = initialRowCount / (i == 0 ? 1 : 2 * i);
            for (int j = 0; j < rowCount; j++) {
                dataModel.setValueAt(null, i, j);
            }
        }
    }

    /**
     * Creates a new {@code JBracketView} with the given initial (or full) data.
     * Note that all columns and rows needed for this view are created via
     * the {@link JBracketView#JBracketView(int, boolean)} constructor.
     * <p>
     * All values stored in the given array will be inserted into the current
     * data model.
     *
     * @param elements the initial elements
     * @param elimination tells the constructor whether the row count of
     *         each column should be decreased
     * @throws IllegalArgumentException if {@code elimination} is enabled and
     *                                  the row count is not a multiple of {@code 2}
     *                                  or the given array is {@code null}.
     */
    public JBracketView(final T[] elements, boolean elimination) throws IllegalArgumentException {
        this(elements == null ? -1 : elements.length, elimination);

        if (elements == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }

        int idx = 0;
        for (int i = 0; i < dataModel.getColumnCount(); i++) {
            int rowCount = dataModel.getRowCount(i);
            for (int j = 0; j < rowCount; j++) {
                if (idx < elements.length) {
                    dataModel.setValueAt(elements[idx++], i, j);
                }
            }
        }
    }

    /**
     * Creates a new {@code JBracketView} with its values aligned as a simple
     * two-dimensional grid.
     * <p>
     * <b>Warning: </b> in order to align all brackets/cells in a grid, this
     * constructor sets a new {@code BracketLocator} by calling
     * {@code BracketLocator.asGrid()}.
     *
     * @param elements the elements to add
     * @param columns the amount of columns to create
     * @param rows the amount of rows per column
     * @throws IllegalArgumentException if any of the given arguments does not
     *                                  match its rules.
     * @see BracketLocator#asGrid()
     */
    public JBracketView(final T[] elements, final int columns, final int rows)
            throws IllegalArgumentException {
        this();
        if (elements == null) {
            throw new IllegalArgumentException("Elements must be not null");
        }
        if (columns < 1) {
            throw new IllegalArgumentException("ColumnCount has to be > 0");
        }
        if (rows < 1) {
            throw new IllegalArgumentException("RowCount has to be > 0");
        }

        setModel(new DefaultBracketModel<>(columns));
        setLocator(BracketLocator.asGrid());
        int idx = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (idx < elements.length) {
                    dataModel.setValueAt(elements[idx++], i, j);
                } else {
                    dataModel.setValueAt(null, i, j);
                }
            }
        }
    }

    /**
     * Returns the data model that holds the list of items displayed
     * by the <code>JBracketView</code> component.
     *
     * @return the <code>BracketModel</code> that provides the displayed
     *         list of items
     * @see #setModel
     */
    public BracketModel<T> getModel() {
        return dataModel;
    }

    /**
     * Sets the data model of this view.
     *
     * @param dataModel the data model to use
     */
    public void setModel(BracketModel<T> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * Returns the object responsible for painting brackets.
     *
     * @return the value of the {@code cellRenderer} property
     * @see #setCellRenderer
     */
    public BracketCellRenderer<? super T> getCellRenderer() {
        return cellRenderer;
    }

    /**
     * Sets the delegate that is used to paint each bracket in the view.
     * The job of a cell renderer is discussed in detail in the
     * <a href="#renderer">class level documentation</a>.
     *
     * @param cellRenderer the <code>ListCellRenderer</code>
     *         that paints list cells
     * @see #getCellRenderer
     */
    public void setCellRenderer(BracketCellRenderer<? super T> cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    /**
     * Returns the painter that draws lines between the brackets/cells according
     * to the current painting mode.
     *
     * @return the current line painter
     */
    public BracketLinePainter getLinePainter() {
        return lineRenderer;
    }

    /**
     * Sets a new {@code BracketLinePainter}.
     *
     * @param lineRenderer a new painter or {@code null} if none should be used.
     */
    public void setLineRenderer(BracketLinePainter lineRenderer) {
        this.lineRenderer = lineRenderer;
    }

    /**
     * Returns the current {@code BracketLocator} or the {@code BracketConstraints}
     * if no locator has been set.
     *
     * @return the current {@code BracketLocator}
     */
    public BracketLocator getLocator() {
        return locator == null ? constraints : locator;
    }

    /**
     * Sets a new {@code BracketLocator}.
     *
     * @param locator a new locator or {@code null} if the default one
     *         should be used.
     */
    public void setLocator(BracketLocator locator) {
        this.locator = locator;
    }

    /**
     * {@inheritDoc}
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        if (dataModel != null && dataModel.getColumnCount() > 0) {
            paintBrackets(g);

            int height = constraints.ipadX
                    + (dataModel.getRowCount(0) * (constraints.bracketHeight + constraints.spaceY));
            int width = constraints.ipadY
                    + ((dataModel.getColumnCount()) * (constraints.bracketWidth + constraints.spaceX));
            setPreferredSize(new Dimension(width, height));
        }
    }

    /**
     * Paints all brackets stored inside this view.
     *
     * @param g the Graphics context in which to paint
     */
    public synchronized void paintBrackets(Graphics g) {
        if (dataModel == null || dataModel.getColumnCount() == 0) {
            return;
        }

        int x, y;
        for (int i = 0; i < dataModel.getColumnCount(); i++) {
            x = getLocator().getX(i, constraints);

            for (int j = 0; j < dataModel.getRowCount(i); j++) {
                T value = dataModel.getValueAt(i, j);
                y = getLocator().getY(i, j, constraints);

                if (cellRenderer != null) {
                    Component component = getCellRenderer().getBracketCellRendererComponent(
                            this, value, i, j);

                    rendererPane.paintComponent(g, component, this, x, y,
                                                constraints.bracketWidth, constraints.bracketHeight,
                                                true);
                }
                if (lineRenderer != null) {
                    getLinePainter().paintLine(this, g, i, j);
                }
            }
        }
        rendererPane.removeAll();
    }

    /**
     * Returns the amount of brackets/cells in this view.
     *
     * @return the amount of brackets/cells
     */
    public int getBracketCount() {
        int count = 0;
        for (int i = 0; i < dataModel.getColumnCount(); i++) {
            count += dataModel.getRowCount(i);
        }
        return count;
    }

    /**
     * Returns the object that stores all mutable attributes of this view.
     *
     * @return the {@code BracketConstraints} of this view
     */
    public BracketConstraints getConstraints() {
        return constraints;
    }

    /**
     * Sets new {@code BracketConstraints}. Note that the new constraints will
     * be used if they are not {@code null}.
     *
     * @param constraints the new constraints to use
     */
    public void setConstraints(BracketConstraints constraints) {
        if (constraints != null) {
            this.constraints = constraints;
        }

    }

    /**
     * Returns the line color.
     *
     * @return the line color.
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Sets a new line color (must be non-null).
     *
     * @param lineColor the new line color
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Returns the next {@code BracketModel} position that matches the given
     * {@code Predicate}. Note that the iteration starts at columnCount - 2
     * and moves backwards to 0.
     * <p>
     * The returned point can be used to get the value of the next bracket/
     * cell:
     * <pre>
     * {@code
     *  BracketModel<Integer> model = view.getModel();
     *  Point next = view.getNextPosition(x -> x == 10);
     *
     *  Integer nextValue = mode.getValueAt(next.x, next.y);
     * }
     * </pre>
     *
     * @param predicate the filter to use
     * @return the model position for the filtered cell
     */
    public Point getNextPosition(Predicate<? super T> predicate) {
        for (int i = dataModel.getColumnCount() - 2; i >= 0; i--) {
            for (int j = 0; j < dataModel.getRowCount(i); j++) {
                T value = dataModel.getValueAt(i, j);
                if (predicate.test(value)) {
                    return new Point(i + 1, j / 2);
                }
            }
        }
        return null;
    }

    /*
     * --- The Scrollable Implementation ---
     */

    /**
     * Computes the size of viewport.
     *
     * @return the preferredSize of a <code>JViewport</code> whose view
     *         is this <code>Scrollable</code>
     * @see JViewport#getPreferredSize
     */
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Insets insets = getInsets();
        // delta value to be able to paint the current Border
        int dX = insets.left + insets.right;
        int dY = insets.top + insets.bottom;

        Dimension preferredSize = getPreferredSize();
        preferredSize.width += dX;
        preferredSize.height += dY;
        return preferredSize;
    }

    /**
     * Returns the distance to scroll to expose the next or previous row (for
     * vertical scrolling) or column (for horizontal scrolling).
     * <p>
     *
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants#VERTICAL or SwingConstants#HORIZONTAL.
     * @param direction Less than zero to scroll up/left, greater than zero for
     *         down/right.
     * @return The "unit" increment for scrolling in the specified direction.
     *         This value should always be positive.
     * @see JScrollBar#setUnitIncrement
     */
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation, int direction) {
        if (direction == SwingConstants.VERTICAL) {
            return constraints.bracketWidth + constraints.spaceX;
        }
        return constraints.bracketHeight + constraints.spaceY;
    }

    /**
     * For horizontal scrolling orientation, returns visibleRect.width.
     *
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left, greater than zero for down/right.
     * @return The "block" increment for scrolling in the specified direction.
     *         This value should always be positive.
     * @see JScrollBar#setBlockIncrement
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return visibleRect.height;
        }
        return visibleRect.width;
    }

    /**
     * Don't track the viewport's width. This allows horizontal
     * scrolling if the JViewport is itself embedded in a JScrollPane.
     *
     * @return True if a viewport should force the Scrollable width to
     *         match its own.
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /**
     * Don't track the viewport's height. This allows vertical
     * scrolling if the JViewport is itself embedded in a JScrollPane.
     *
     * @return True if a viewport should force the Scrollable height to
     *         match its own.
     */
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    private void createFields() {
        constraints  = new BracketConstraints();
        rendererPane = new CellRendererPane();

        add(rendererPane);
    }

}


