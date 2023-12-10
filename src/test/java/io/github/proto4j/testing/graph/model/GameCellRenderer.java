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

package io.github.proto4j.testing.graph.model; //@date 15.01.2023

import io.github.proto4j.graph.BracketCellRenderer;
import io.github.proto4j.graph.JBracketView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameCellRenderer extends JPanel
        implements BracketCellRenderer<Game> {

    private final TeamCell home, guest;

    public GameCellRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(true);
        home  = new TeamCell();
        guest = new TeamCell();
        installComponents();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setBorder(new LineBorder(getBackground().darker(), 1, true));
    }

    private void installComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.gridy   = 0;
        gbc.gridx   = 0;

        add(home, gbc);

        JLabel holder = new JLabel() {
            @Override
            public void updateUI() {
                super.updateUI();
                setBackground(UIManager.getColor("Panel.background").darker());
            }
        };
        holder.setOpaque(true);
        holder.setPreferredSize(new Dimension(-1, 1));

        gbc.gridy   = 1;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        add(holder, gbc);

        gbc.gridy   = 2;
        gbc.weighty = 1.0;
        gbc.fill    = GridBagConstraints.BOTH;
        add(guest, gbc);
    }

    @Override
    public Component getBracketCellRendererComponent(
            JBracketView<? extends Game> bracketView, Game value,
            int columnIndex, int rowIndex) {

        if (value != null) {
            applyTeamInfo(value.getHome(), home);
            applyTeamInfo(value.getGuest(), guest);
        }
        else {
            applyTeamInfo(null, home);
            applyTeamInfo(null, guest);
        }

        return this;
    }


    private void applyTeamInfo(String team, TeamCell cell) {
        cell.setForwarded(false);
        cell.setName("TBA");
        cell.setGoals("0");
        cell.setForwardIcon(null);

        if (team != null) {
            cell.setName(team);
        } else {
            cell.setTeamIcon((Icon) null);
            cell.setGoals(" ");
        }
    }

    private static class TeamCell extends JPanel {
        public static final String FORWARDED_COLOR     = "6A8659";
        public static final String NON_FORWARDED_COLOR = "E05555";

        private final JLabel teamIcon;
        private final JLabel forwardIcon;
        private final JLabel name;
        private final JLabel goals;

        private boolean forwarded;

        private String goalsInfo;

        public TeamCell() {
            teamIcon = new JLabel("T"); // place your icon here
            teamIcon.setHorizontalAlignment(SwingConstants.CENTER);
            teamIcon.setPreferredSize(new Dimension(20, -1));

            forwardIcon = new JLabel();
            forwardIcon.setHorizontalAlignment(SwingConstants.CENTER);
            forwardIcon.setPreferredSize(new Dimension(20, -1));

            name  = new JLabel();
            goals = new JLabel();
            goals.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel center = new JPanel(new BorderLayout());
            center.setOpaque(false);
            center.add(name, BorderLayout.WEST);
            center.add(goals, BorderLayout.EAST);

            setLayout(new BorderLayout(5, 0));
            add(center, BorderLayout.CENTER);
            add(forwardIcon, BorderLayout.EAST);
            add(teamIcon, BorderLayout.WEST);

            setTeamIcon((Icon) null);
            setForwarded(false);
        }

        public void setGoals(String goalsInfo) {
            this.goalsInfo = goalsInfo;
            goals.setForeground(UIManager.getColor("Label.foreground"));
            goals.setText(String.format("<html><span style=\"color:%s;\">%s</span></html>",
                                        isForwarded() ? FORWARDED_COLOR : NON_FORWARDED_COLOR,
                                        goalsInfo));
        }

        public boolean isForwarded() {
            return forwarded;
        }

        public void setForwarded(boolean forwarded) {
            this.forwarded = forwarded;
            setGoals(goalsInfo == null ? "TBD" : goalsInfo);
        }

        public void setForwardIcon(Icon forwardIcon) {
            this.forwardIcon.setIcon(forwardIcon);
        }

        @Override
        public void setName(String name) {
            this.name.setText(name);
        }

        public void setTeamIcon(Icon icon) {
            teamIcon.setIcon(icon);

        }
    }

}
