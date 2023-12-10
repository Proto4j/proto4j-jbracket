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

package io.github.proto4j.testing.graph; //@date 15.01.2023

import io.github.proto4j.graph.BracketConstraints;
import io.github.proto4j.graph.BracketLocator;
import io.github.proto4j.graph.BracketModel;
import io.github.proto4j.graph.JBracketView;
import io.github.proto4j.testing.graph.model.Game;import io.github.proto4j.testing.graph.model.GameCellRenderer;

import javax.swing.*;

public class BracketModelExample {

    public static void main(String[] args) {
        BracketModel<Game> model = new BracketModel<>() {
            @Override
            public int getColumnCount() {return 4;}

            @Override
            public int getRowCount(int columnIndex) {return 4;}

            @Override
            public Game getValueAt(int columnIndex, int rowIndex) {
                return new Game("Team " + columnIndex + ":" + rowIndex,
                                "Team " + columnIndex + ".5:" + rowIndex + ".5");
            }

            @Override
            public void setValueAt(Game value, int columnIndex, int rowIndex) {}
        };

        JBracketView<Game> view = new JBracketView<>(model);
        view.setCellRenderer(new GameCellRenderer());
        view.setLocator(BracketLocator.asGrid());

        // we don't need a line renderer
        BracketConstraints bc = view.getConstraints();
        bc.bracketHeight = 75;
        bc.bracketWidth = 150;
        bc.spaceY = 40;
        bc.spaceX = 50;

        // Put the view into a JScrollPane
        JScrollPane scrollPane = new JScrollPane(view);
        JFrame      mainFrame  = new JFrame("BracketViewDemo");

        mainFrame.setContentPane(scrollPane);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

}
