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
public class MtsPanel extends AbstractBroker implements KeyListener {
    private static final int sumWasConst = 18703;
    private static KeyListener keyListener;
    private JTextField tf01, tf02, tf03, tf04;
    private JTextField tf11, tf12, tf13, tf14;
    private BrokerDTO dto;
    private JLabel itog01, itog02, itog3;

    public MtsPanel(LayoutManager layout, PortfelPane parent) {
        super(layout);
        setName("mts");
        dto = loadDto(Paths.get(Constant.BROKERS_DIR + getName() + ".ru.investment.dto"), getName());
        keyListener = this;

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
                                add(textLabel("Ветвь:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(textLabel(" Высокие технологии", SwingConstants.LEFT, Color.white));
                        add(textLabel(" Информатика", SwingConstants.LEFT, Color.white));
                        add(textLabel(" Российские акции", SwingConstants.LEFT, Color.white));
                        add(textLabel(" Биотехнологии", SwingConstants.LEFT, Color.white));
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

                        tf01 = getFTF(String.format("%.0f р.", getDtoInputListValue(0)), Color.RED, keyListener);
                        tf02 = getFTF(String.format("%.0f р.", getDtoInputListValue(1)), Color.RED, keyListener);
                        tf03 = getFTF(String.format("%.0f р.", getDtoInputListValue(2)), Color.RED, keyListener);
                        tf04 = getFTF(String.format("%.0f р.", getDtoInputListValue(3)), Color.RED, keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf01);
                        add(tf02);
                        add(tf03);
                        add(tf04);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.RED.darker().darker());
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
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = getFTF(String.format("%.0f р.", getDtoOutputListValue(0)), keyListener);
                        tf12 = getFTF(String.format("%.0f р.", getDtoOutputListValue(1)), keyListener);
                        tf13 = getFTF(String.format("%.0f р.", getDtoOutputListValue(2)), keyListener);
                        tf14 = getFTF(String.format("%.0f р.", getDtoOutputListValue(3)), keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf11);
                        add(tf12);
                        add(tf13);
                        add(tf14);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.RED.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : dto.getOutputList()) {
                                    sum += aFloat;
                                }
                                itog02 = textLabel(String.format("%,.0f р.", sum + sumWasConst), SwingConstants.CENTER, Color.white, Constant.btnsFont3);
                                add(itog02);
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
                                add(textLabel("Прибыль:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = getDtoOutputListValue(0) - getDtoInputListValue(0);
                                add(textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = getDtoOutputListValue(1) - getDtoInputListValue(1);
                                add(textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = getDtoOutputListValue(2) - getDtoInputListValue(2);
                                add(textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = getDtoOutputListValue(3) + sumWasConst - getDtoInputListValue(3);
                                add(textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.RED.darker().darker());
                                Float itog1 = 0f;
                                itog1 += Float.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText().replace(" р.", ""));
                                itog1 += Float.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText().replace(" р.", ""));
                                itog1 += Float.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText().replace(" р.", ""));
                                itog1 += Float.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText().replace(" р.", ""));

                                Float itog2 = 0f;
                                itog2 += Float.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText().replace(" р.", ""));
                                itog2 += Float.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText().replace(" р.", ""));
                                itog2 += Float.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText().replace(" р.", ""));
                                itog2 += Float.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText().replace(" р.", ""));

                                Float sum = itog2 + sumWasConst - itog1;
                                itog3 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.btnsFont3);
                                add(itog3);
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
                                setBackground(Color.BLACK);
                                add(textLabel("-", SwingConstants.CENTER, Color.white));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.RED.darker().darker());
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
                return !dto.getInputList().isEmpty() ? dto.getInputList().get(index) : 0;
            }

            private float getDtoOutputListValue(int index) {
                return !dto.getOutputList().isEmpty() ? dto.getOutputList().get(index) : 0;
            }
        };

        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(Color.RED.darker().darker());
                setPreferredSize(new Dimension(parent.getTitleWidth(), 0));
                add(textLabel("МТС", null, SwingConstants.CENTER, Color.RED, Constant.btnsFont7), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", null, SwingConstants.CENTER, Color.RED, Constant.btnsFont8), BorderLayout.SOUTH);
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

        dto.setInputList(new ArrayList<>(4) {{
            add(Float.parseFloat(tf01.getText().replace(" р.", "")));
            add(Float.parseFloat(tf02.getText().replace(" р.", "")));
            add(Float.parseFloat(tf03.getText().replace(" р.", "")));
            add(Float.parseFloat(tf04.getText().replace(" р.", "")));
        }});
        dto.setOutputList(new ArrayList<>(4) {{
            add(Float.parseFloat(tf11.getText().replace(" р.", "")));
            add(Float.parseFloat(tf12.getText().replace(" р.", "")));
            add(Float.parseFloat(tf13.getText().replace(" р.", "")));
            add(Float.parseFloat(tf14.getText().replace(" р.", "")));
        }});

        saveDto(dto);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            float sum1 = 0f, sum2 = 0f;

            sum1 += Float.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText().replace(" р.", ""));

            sum2 += Float.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText().replace(" р.", ""));

            itog01.setText(String.format("%,.0f р.", sum1));
            itog02.setText(String.format("%,.0f р.", sum2 + sumWasConst));

            itog3.setText(String.format("%,.0f р.", (sum2 + sumWasConst - sum1)));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }
}
