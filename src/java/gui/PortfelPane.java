package gui;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import registry.Registry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PortfelPane extends JPanel {

    JLabel needGets, currentMonth;
    JPanel redPane, bluePane, orangePane, downPane;
    int titleWidth = 250;

    public PortfelPane() {
        setLayout(new BorderLayout(0, 0));

        JPanel centerPane = new JPanel(new FlowLayout(0, 0, 0)) {
            {
                setBackground(Color.BLACK);

                redPane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.RED.darker().darker());

                        JPanel centerPane = new JPanel(new GridLayout(1, 5, 0, 0)) {
                            {
                                setBackground(Color.DARK_GRAY.darker());
                                setBorder(new EmptyBorder(0, -1, -1, 0));

                                JPanel orangeCenterPane1 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Ветвь:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel(" Высокие технологии", JLabel.LEFT, Color.white));
                                        add(textLabel(" Информатика", JLabel.LEFT, Color.white));
                                        add(textLabel(" Российские акции", JLabel.LEFT, Color.white));
                                        add(textLabel(" Биотехнологии", JLabel.LEFT, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.RED.darker().darker());
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane2 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Вложено:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.RED.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane3 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("На сегодня:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.RED.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane4 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.RED.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane5 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль %:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.RED.darker().darker());
                                                setBorder(new EmptyBorder(1, 1, 2, 0));
                                                add(new JPanel(new BorderLayout(0, 0)) {
                                                    {
                                                        setBackground(Color.BLACK);
                                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                };

                                add(orangeCenterPane1);
                                add(orangeCenterPane2);
                                add(orangeCenterPane3);
                                add(orangeCenterPane4);
                                add(orangeCenterPane5);
                            }
                        };

                        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.RED.darker().darker());
                                setPreferredSize(new Dimension(titleWidth, 0));
                                add(textLabel("МТС", JLabel.CENTER, Color.RED, Registry.btnsFont7), BorderLayout.CENTER);
                                add(textLabel("ВСЕГО:", JLabel.CENTER, Color.RED, Registry.btnsFont8), BorderLayout.SOUTH);
                            }
                        };

                        add(titlePane, BorderLayout.WEST);
                        add(centerPane, BorderLayout.CENTER);
                    }
                };
                bluePane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.BLUE.darker().darker());

                        JPanel centerPane = new JPanel(new GridLayout(1, 5, 0, 0)) {
                            {
                                setBackground(Color.DARK_GRAY.darker());
                                setBorder(new EmptyBorder(0, -1, 0, 0));

                                JPanel orangeCenterPane1 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Ветвь:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel(" Акции+ETF", JLabel.LEFT, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLUE.darker().darker());
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane2 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Вложено:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLUE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane3 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("На сегодня:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLUE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane4 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLUE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane5 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль %:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLUE.darker().darker());
                                                setBorder(new EmptyBorder(1, 1, 1, 0));
                                                add(new JPanel(new BorderLayout(0, 0)) {
                                                    {
                                                        setBackground(Color.BLACK);
                                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                };

                                add(orangeCenterPane1);
                                add(orangeCenterPane2);
                                add(orangeCenterPane3);
                                add(orangeCenterPane4);
                                add(orangeCenterPane5);
                            }
                        };

                        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                                setPreferredSize(new Dimension(titleWidth, 0));
                                add(textLabel("ВТБ", JLabel.CENTER, new Color(0.8f, 0.8f, 1.0f), Registry.btnsFont7), BorderLayout.CENTER);
                                add(textLabel("ВСЕГО:", JLabel.CENTER, new Color(0.8f, 0.8f, 1.0f), Registry.btnsFont8), BorderLayout.SOUTH);
                            }
                        };

                        add(titlePane, BorderLayout.WEST);
                        add(centerPane, BorderLayout.CENTER);
                    }
                };
                orangePane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.ORANGE.darker().darker());

                        JPanel centerPane = new JPanel(new GridLayout(1, 5, 0, 0)) {
                            {
                                setBackground(Color.DARK_GRAY.darker());
                                setBorder(new EmptyBorder(0, -1, -1, 0));

                                JPanel orangeCenterPane1 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Ветвь:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel(" ETF", JLabel.LEFT, Color.white));
                                        add(textLabel(" Акции", JLabel.LEFT, Color.white));
                                        add(textLabel(" Детский портфель", JLabel.LEFT, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.ORANGE.darker().darker());
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane2 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Вложено:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.ORANGE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane3 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("На сегодня:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.ORANGE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane4 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.ORANGE.darker().darker());
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                    }
                                };
                                JPanel orangeCenterPane5 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                                    {
                                        setOpaque(false);

                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("Прибыль %:", JLabel.CENTER, Color.YELLOW));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.BLACK);
                                                add(textLabel("-", JLabel.CENTER, Color.white));
                                            }
                                        });
                                        add(new JPanel(new BorderLayout(0, 0)) {
                                            {
                                                setBackground(Color.ORANGE.darker().darker());
                                                setBorder(new EmptyBorder(1, 1, 2, 0));
                                                add(new JPanel(new BorderLayout(0, 0)) {
                                                    {
                                                        setBackground(Color.BLACK);
                                                        add(textLabel("-", JLabel.CENTER, Color.white));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                };

                                add(orangeCenterPane1);
                                add(orangeCenterPane2);
                                add(orangeCenterPane3);
                                add(orangeCenterPane4);
                                add(orangeCenterPane5);
                            }
                        };

                        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.ORANGE.darker().darker());
                                setPreferredSize(new Dimension(titleWidth, 0));
                                add(textLabel("Тинькофф", JLabel.CENTER, Color.ORANGE, Registry.btnsFont7), BorderLayout.CENTER);
                                add(textLabel("ВСЕГО:", JLabel.CENTER, Color.ORANGE, Registry.btnsFont8), BorderLayout.SOUTH);
                            }
                        };

                        add(titlePane, BorderLayout.WEST);
                        add(centerPane, BorderLayout.CENTER);
                    }
                };

                add(redPane);
                add(bluePane);
                add(orangePane);
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

                                        add(textLabel("ВСЕГО:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("ЦЕЛЬ:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("ОТКЛОНЕНИЕ:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("КОМПЕНСАНД:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("ДОСТУПНО П4%:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel(null, 0));
                                        add(textLabel("ДИВИДЕНДЫ/МЕС:", JLabel.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel02 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                    }
                                };

                                JPanel panel03 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("УДВОЕНИЕ ЧЕРЕЗ:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ МЕС РУБ:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ ГОД РУБ:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("ДИВИДЕНДЫ/ГОД:", JLabel.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel04 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                    }
                                };

                                JPanel panel05 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("СРЕДНЕСРОК:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНИЙ ВЗНОС:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("СРЕДНЕ В МЕС:", JLabel.RIGHT, Color.WHITE));
                                        add(textLabel("МАКС В МЕС:", JLabel.RIGHT, Color.WHITE));
                                    }
                                };

                                JPanel panel06 = new JPanel(new GridLayout(7, 1, 0, 0)) {
                                    {
                                        setBackground(Color.BLACK);

                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
                                        add(textLabel("-", JLabel.CENTER, Color.WHITE));
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
                                        setFont(Registry.btnsFont7);
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
                                setFont(Registry.btnsFont3);
                                setHorizontalAlignment(0);
                                setForeground(Color.RED);
                            }
                        };
                        currentMonth = new JLabel("МЕСЯЦ: na") {
                            {
                                setFont(Registry.btnsFont3);
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

    private Component textLabel(String text, int align) {
        return textLabel(text, null, align, null);
    }

    private Component textLabel(String text, int align, Color foreground) {
        return textLabel(text, null, align, foreground);
    }

    private Component textLabel(String text, int align, Color foreground, Font font) {
        return textLabel(text, null, align, foreground, font);
    }

    private Component textLabel(String text, String tooltip, int align, Color foreground) {
        return textLabel(text, tooltip, align, foreground, null);
    }

    private Component textLabel(String text, String tooltip, int align, Color foreground, Font font) {
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

    public void updatePanesDim() {
        redPane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.44f)));
        bluePane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.215f)));
        orangePane.setPreferredSize(new Dimension(getWidth(), (int) ((getHeight() - downPane.getHeight()) * 0.345f)));

        redPane.revalidate();
        bluePane.revalidate();
        orangePane.revalidate();
    }
}
