package components;

import core.NetProcessor;
import gui.InvestFrame;
import gui.TablePane;
import registry.Registry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Comparator;

public class TextTableRow extends JPanel implements Comparator<ShareTableRow> {

    public TextTableRow(String... values) {
        setLayout(new GridLayout(1, values.length, 0, 0));
        setAlignmentY(JPanel.CENTER_ALIGNMENT);
        setBackground(Color.BLACK);

        for (String value : values) {
            add(new JLabel(value) {{
                setFocusable(true);
                setFont(Registry.btnsFont4);
                setHorizontalAlignment(JLabel.CENTER);
                setVerticalAlignment(JLabel.CENTER);
                setForeground(Color.GRAY);

                if (value.contains("Индекс")) {
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                NetProcessor.save();
                                TablePane.clearRows();
                                NetProcessor.load(InvestFrame.getTablePane());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            setForeground(Color.YELLOW);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            setForeground(Color.ORANGE);
                        }
                    });
                }
            }});
        }
    }

    @Override
    public int compare(ShareTableRow o1, ShareTableRow o2) {
        return 0;
    }
}
