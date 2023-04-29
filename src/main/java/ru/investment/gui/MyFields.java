package ru.investment.gui;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonSerialize
public class MyFields implements Serializable {

    public static JLabel textLabel(String text, int align) {
        return textLabel(text, null, align, null);
    }

    public static JLabel textLabel(String text, int align, Color foreground) {
        return textLabel(text, null, align, foreground, null);
    }

    public static JLabel textLabel(String text, int align, Color foreground, Font font) {
        return textLabel(text, null, align, foreground, font);
    }

    public static JLabel textLabel(String text, String tooltip, int align, Color foreground) {
        return textLabel(text, tooltip, align, foreground, null);
    }

    public static JLabel textLabel(String text, String tooltip, int align, Color foreground, Font font) {
        return new JLabel(text) {
            {
                setHorizontalAlignment(align);
                if (tooltip != null) {
                    setToolTipText("Ежегодная безопасная выемка 4%");
                }
                if (foreground != null) {
                    setForeground(foreground);
                }
                if (font != null) {
                    setFont(font);
                }
            }
        };
    }

    static JTextField getFTF(String value) {
        return getFTF(value, null, null);
    }

    static JTextField getFTF(String value, KeyListener listener) {
        return getFTF(value, null, listener);
    }

    static JTextField getFTF(String value, Color foreground, KeyListener listener) {
        return new JTextField(value) {
            {
                setHorizontalAlignment(0);
                setBackground(Color.DARK_GRAY);
                setForeground(foreground == null ? Color.WHITE : foreground);
                setBorder(BorderFactory.createLoweredSoftBevelBorder());
                addKeyListener(listener);
                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        selectAll();
                    }
                });
            }
        };
    }
}
