package ru.investment.gui.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.investment.config.constants.Constant;
import ru.investment.utils.UniversalNumberParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
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

    public static JTextField getFTF(String value, KeyListener listener) {
        return getFTF(value, null, listener);
    }

    public static JTextField getFTF(String value, Color foreground, KeyListener listener) {
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

    public static SumPanel getJPWT(String value) {
        return getJPWT(value, null);
    }

    public static SumPanel getJPWT(String value, Color foreground) {
        return new SumPanel(value, foreground);
    }

    public static class SumPanel extends JPanel {
        float was = 0, sum;
        String text;
        JLabel label;

        public SumPanel(String value, Color foreground) {
            setAlignmentX(SwingConstants.CENTER);
            setBackground(Color.DARK_GRAY);
            setForeground(foreground == null ? Color.WHITE : foreground);
            setBorder(BorderFactory.createLoweredSoftBevelBorder());

            setOpaque(false);
            if (!value.endsWith("%")) {
                sum = UniversalNumberParser.parseFloat(value);
                text = String.format("%,.0f р.", sum);
                label = textLabel(text,
                        SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.FONT_TABLE_SUM);
            } else {
                sum = Math.round(UniversalNumberParser.parseFloat(value));
                text = String.format("%s %%", Math.round(sum));
                label = textLabel(text,
                        SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.FONT_TABLE_SUM);
            }
            add(label);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (was != sum) {
                was = sum;
                label.setForeground(sum >= 0 ? Color.GREEN : Color.RED);
                label.setText(text);
            }
        }

        public void setSum(float sum) {
            this.sum = sum;
            text = text.endsWith("%") ? String.format("%s %%", Math.round(sum)) : String.format("%,.0f р.", sum);
            repaint();
        }

        public String getText() {
            return text;
        }
    }
}
