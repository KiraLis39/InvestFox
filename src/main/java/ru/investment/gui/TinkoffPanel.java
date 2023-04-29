package ru.investment.gui;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.investment.dto.BrokerDTO;
import ru.investment.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Paths;
import java.util.ArrayList;

import static ru.investment.gui.MyFields.getFTF;
import static ru.investment.gui.MyFields.textLabel;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class TinkoffPanel extends AbstractBroker implements KeyListener {
    private static KeyListener kList;
    private transient BrokerDTO dto;
    private JTextField tf01, tf02, tf03;
    private JTextField tf11, tf12, tf13;
    private JLabel itog01, itog02;

    public TinkoffPanel(LayoutManager layout, PortfelPane parent) {
        super(layout);
        setName("tkf");
        dto = loadDto(Paths.get(Constant.BROKERS_DIR + getName() + ".ru.investment.dto"), getName());
        kList = this;

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
                                add(textLabel("Ветвь:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(textLabel(" ETF", SwingConstants.LEFT, Color.white));
                        add(textLabel(" Акции", SwingConstants.LEFT, Color.white));
                        add(textLabel(" Детский портфель", SwingConstants.LEFT, Color.white));
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

                        tf01 = getFTF(String.format("%.0f р.", getDtoInputListValue(0)), Color.RED, kList);
                        tf02 = getFTF(String.format("%.0f р.", getDtoInputListValue(1)), Color.RED, kList);
                        tf03 = getFTF(String.format("%.0f р.", getDtoInputListValue(2)), Color.RED, kList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf01);
                        add(tf02);
                        add(tf03);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.ORANGE.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : dto.getInputList()) {
                                    sum += aFloat;
                                }
                                itog01 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.RED, Constant.btnsFont3);
                                add(itog01);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = getFTF(String.format("%.0f р.", getDtoOutputListValue(0)), kList);
                        tf12 = getFTF(String.format("%.0f р.", getDtoOutputListValue(1)), kList);
                        tf13 = getFTF(String.format("%.0f р.", getDtoOutputListValue(2)), kList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf11);
                        add(tf12);
                        add(tf13);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.ORANGE.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : dto.getOutputList()) {
                                    sum += aFloat;
                                }
                                itog02 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.WHITE, Constant.btnsFont3);
                                add(itog02);
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
                                add(textLabel("Прибыль:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(textLabel("-", SwingConstants.CENTER, Color.white));
                        add(textLabel("-", SwingConstants.CENTER, Color.white));
                        add(textLabel("-", SwingConstants.CENTER, Color.white));
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.ORANGE.darker().darker());
                                add(textLabel("-", SwingConstants.CENTER, Color.white));
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
                                add(textLabel("Прибыль %:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("-", SwingConstants.CENTER, Color.white));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("-", SwingConstants.CENTER, Color.white));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("-", SwingConstants.CENTER, Color.white));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.ORANGE.darker().darker());
                                setBorder(new EmptyBorder(1, 1, 2, 0));
                                add(new JPanel(new BorderLayout(0, 0)) {
                                    {
                                        setBackground(Color.BLACK);
                                        add(textLabel("-", SwingConstants.CENTER, Color.white));
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

            private float getDtoInputListValue(int index) {
                return dto.getInputList().isEmpty() ? 0 : dto.getInputList().get(index);
            }

            private float getDtoOutputListValue(int index) {
                return dto.getOutputList().isEmpty() ? 0 : dto.getOutputList().get(index);
            }
        };

        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(Color.ORANGE.darker().darker());
                setPreferredSize(new Dimension(parent.getTitleWidth(), 0));
                add(textLabel("Тинькофф", SwingConstants.CENTER, Color.ORANGE, Constant.btnsFont7), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", SwingConstants.CENTER, Color.ORANGE, Constant.btnsFont8), BorderLayout.SOUTH);
            }
        };

        add(titlePane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
    }

    void preSave() {
        if (dto == null) {
            dto = new BrokerDTO();
            dto.setName(getName());
        }

        dto.setInputList(new ArrayList<>(3) {{
            add(Float.parseFloat(tf01.getText().replace(" р.", "")));
            add(Float.parseFloat(tf02.getText().replace(" р.", "")));
            add(Float.parseFloat(tf03.getText().replace(" р.", "")));
        }});
        dto.setOutputList(new ArrayList<>(3) {{
            add(Float.parseFloat(tf11.getText().replace(" р.", "")));
            add(Float.parseFloat(tf12.getText().replace(" р.", "")));
            add(Float.parseFloat(tf13.getText().replace(" р.", "")));
        }});

        saveDto(dto);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            float sum = 0f;
            sum += Float.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
            sum += Float.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText());
            sum += Float.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText());
            itog01.setText(String.format("%,.0f р.", sum));

            sum = 0f;
            sum += Float.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
            sum += Float.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText());
            sum += Float.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText());
            itog02.setText(String.format("%,.0f р.", sum));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }
}
