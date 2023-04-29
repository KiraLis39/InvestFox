package ru.investment.gui;

import lombok.Data;
import ru.investment.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static ru.investment.gui.MyFields.textLabel;

@Data
public class PortfelPane extends JPanel {
    private final int TITLE_WIDTH = 250;
    private JLabel needGets, currentMonth;
    private JPanel downPane;
    private MtsPanel mtsPane;
    private VtbPanel vtbPane;
    private TinkoffPanel tinkPane;

    public PortfelPane() {
        setLayout(new BorderLayout(0, 0));

        JPanel centerPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            {
                setBackground(Color.BLACK);

                mtsPane = new MtsPanel(new BorderLayout(0, 0), PortfelPane.this);
                vtbPane = new VtbPanel(new BorderLayout(0, 0), PortfelPane.this);
                tinkPane = new TinkoffPanel(new BorderLayout(0, 0), PortfelPane.this);

                add(mtsPane);
                add(vtbPane);
                add(tinkPane);
            }
        };

        downPane = new JPanel(new BorderLayout(0, 0)) {
            {
                setOpaque(false);

                JPanel itogPane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setOpaque(false);

                        JPanel downDataPane = new JPanel(new GridLayout(1, 6, 0, 0)) {
                            {
                                setBackground(Color.BLACK);

                                JPanel panel01 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("ВСЕГО:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("ЦЕЛЬ:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("ОТКЛОНЕНИЕ:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("КОМПЕНСАНД:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("ДОСТУПНО П4%:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel(null, 0));
                                        add(textLabel("ДИВИДЕНДЫ/МЕС:", SwingConstants.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel02 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                    }
                                };

                                JPanel panel03 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("УДВОЕНИЕ ЧЕРЕЗ:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ МЕС РУБ:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ ГОД РУБ:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("ДИВИДЕНДЫ/ГОД:", SwingConstants.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel04 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                    }
                                };

                                JPanel panel05 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("СРЕДНЕСРОК:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНИЙ ВЗНОС:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ В МЕС:", SwingConstants.RIGHT, Color.WHITE));
                                        add(textLabel("МАКС В МЕС:", SwingConstants.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel06 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                        add(textLabel("-", SwingConstants.CENTER, Color.WHITE));
                                    }
                                };

                                add(panel01);
                                add(panel02);
                                add(panel03);
                                add(panel04);
                                add(panel05);
                                add(panel06);
                            }
                        };

                        JPanel itogPane = new JPanel(new BorderLayout()) {
                            {
                                setPreferredSize(new Dimension(250, 0));
                                setBackground(Color.DARK_GRAY.darker().darker());

                                add(new JLabel("ИТОГИ:") {
                                    {
                                        setFont(Constant.btnsFont7);
                                        setForeground(Color.WHITE);
                                        setHorizontalAlignment(0);
                                    }
                                }, BorderLayout.CENTER);
                            }
                        };

                        add(downDataPane, BorderLayout.CENTER);
                        add(itogPane, BorderLayout.WEST);
                    }
                };

                JPanel statusPane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.DARK_GRAY.darker().darker());
                        setBorder(new EmptyBorder(3, 0, 3, 16));

                        needGets = new JLabel("ОТСТАВАНИЕ ОТ ПЛАНА: na") {
                            {
                                setFont(Constant.btnsFont3);
                                setHorizontalAlignment(0);
                                setForeground(Color.RED);
                            }
                        };
                        currentMonth = new JLabel("МЕСЯЦ: na") {
                            {
                                setFont(Constant.btnsFont3);
                                setForeground(Color.WHITE);
                            }
                        };

                        add(needGets, BorderLayout.CENTER);
                        add(currentMonth, BorderLayout.EAST);
                    }
                };

                add(itogPane, BorderLayout.CENTER);
                add(statusPane, BorderLayout.SOUTH);
            }
        };

        add(centerPane, BorderLayout.CENTER);
        add(downPane, BorderLayout.SOUTH);
    }

    public void updatePanesDim() {
        mtsPane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.44f)));
        vtbPane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.215f)));
        tinkPane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.345f)));

        mtsPane.revalidate();
        vtbPane.revalidate();
        tinkPane.revalidate();
    }

    public void saveBrokers() {
        mtsPane.preSave();
        vtbPane.preSave();
        tinkPane.preSave();
    }

    public int getTitleWidth() {
        return this.TITLE_WIDTH;
    }
}
