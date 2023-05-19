package ru.investment.gui.components;

import ru.investment.config.constants.Constant;
import ru.investment.service.ShareService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Comparator;

public class TextTableRow extends JPanel implements Comparator<ShareTableRow> {
    private final ShareService shareService;

    public TextTableRow(ShareService shareService, String... values) {
        this.shareService = shareService;
        setLayout(new GridLayout(1, values.length, 0, 0));
        setAlignmentY(Component.CENTER_ALIGNMENT);
        setBackground(Color.BLACK);

        for (String value : values) {
            add(new JLabel(value) {{
                setFocusable(true);
                setFont(Constant.fontPrimaryHeaders);
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                setForeground(Color.GRAY);

                if (value.contains("Индекс")) {
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                TextTableRow.this.shareService.reload();
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
