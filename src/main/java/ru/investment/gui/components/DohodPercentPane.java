package ru.investment.gui.components;

import ru.investment.config.constants.Constant;
import ru.investment.utils.UniversalNumberParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.investment.gui.components.MyFields.textLabel;

public class DohodPercentPane extends JPanel {
    private JLabel itogPercentLabel;

    public DohodPercentPane(int rowCount, Map<Integer, Map<String, String>> fields, Color background) {
        setOpaque(false);
        setBackground(Color.BLACK);
        setLayout(new GridLayout(rowCount, 1, 0, 0));

        // subscribe header:
        add(new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(Color.BLACK);
                add(textLabel("Прибыль %:", SwingConstants.CENTER, Color.YELLOW));
            }
        });

        // med sums:
        for (Map.Entry<Integer, Map<String, String>> entry : fields.entrySet()) {
            add(MyFields.getJPWT(String.format("%s %%",
                    UniversalNumberParser.parseFloat(entry.getValue().keySet().toArray(new String[0])[0]) /
                            UniversalNumberParser.parseFloat(entry.getValue().values().toArray(new String[0])[0]) * 100)
            ));
        }

        // down sum row cell:
        add(new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(background);
                setBorder(new EmptyBorder(1, 1, 1, 0));
                add(new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.BLACK);

                        float itogSum = 0;
                        float inputSum = 0;
                        for (Map<String, String> value : fields.values()) {
                            itogSum += value.keySet().stream().map(UniversalNumberParser::parseFloat).findFirst().get();
                            inputSum += value.values().stream().map(UniversalNumberParser::parseFloat).findFirst().get();
                        }

                        int sum = Math.round(itogSum / inputSum * 100);
                        itogPercentLabel = textLabel(sum + "%",
                                SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.getFontTableSumRow());
                        add(itogPercentLabel);
                    }
                });
            }
        });
    }

    public MyFields.SumPanel getItogPersentRowLabel(int index) {
        List<MyFields.SumPanel> sPanes = Arrays.stream(getComponents()).filter(c -> c instanceof MyFields.SumPanel).map(c -> ((MyFields.SumPanel) c)).toList();
        return sPanes.get(index);
    }

    public JLabel getItogPercentLabel() {
        return itogPercentLabel;
    }
}
