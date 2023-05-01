package ru.investment.gui.components;

import javax.swing.*;
import java.awt.*;

import static ru.investment.gui.components.MyFields.textLabel;

public class EmptyRowPane extends JPanel {
    public EmptyRowPane() {
        setLayout(new GridLayout(7, 1, 0, 0));

        setBackground(Color.BLACK);

        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
    }
}
