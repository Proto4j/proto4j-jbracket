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
import io.github.proto4j.graph.DefaultBracketLinePainter;
import io.github.proto4j.graph.JBracketView;
import io.github.proto4j.testing.graph.model.Game;import io.github.proto4j.testing.graph.model.GameCellRenderer;

import javax.swing.*;
import java.awt.*;

public class BracketViewExample {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Create initial games that will mark the initial row count.
        Game[] games = new Game[4];
        for (int i = 0; i < games.length; i++) {
            games[i] = new Game("Team " + i, "Team " + i + ".5");
        }

        // by calling the constructor with the initial row count,
        // all needed columns are generated on its own.
        // Create a new bracket view that automatically inserts our
        // games and creates the needed columns for elimination brackets.
        // (true indicates that this view should display an elimination
        // tournament view).
        JBracketView<Game> view = new JBracketView<>(games, true);
        view.setCellRenderer(new GameCellRenderer());
        view.setLineRenderer(new DefaultBracketLinePainter());

        // Additional attributes of this view can be set via the
        // BracketConstraints object.
        BracketConstraints bc = view.getConstraints();
        bc.bracketHeight = 75;
        bc.bracketWidth  = 150;
        bc.lineThickness = 1;
        bc.spaceY        = 40;
        bc.spaceX        = 50;
        bc.paintMode     = BracketConstraints.CENTER;

        view.setLineColor(Color.black.brighter());

        // Put the view into a JScrollPane
        JScrollPane scrollPane = new JScrollPane(view);
        JFrame      mainFrame  = new JFrame("BracketViewDemo");

        mainFrame.setContentPane(scrollPane);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
