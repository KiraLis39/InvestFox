package gui;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dto.BrokerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import registry.Registry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static gui.MyFields.getFTF;
import static gui.MyFields.textLabel;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@JsonSerialize
public class MtsPanel extends AbstractBroker implements KeyListener {
    private static KeyListener keyListener;
    private final int sumWasConst = 18703;
    private JTextField tf01, tf02, tf03, tf04;
    private JTextField tf11, tf12, tf13, tf14;
    private BrokerDTO dto;
    private JLabel itog01, itog02, itog3;

    public MtsPanel(LayoutManager layout, PortfelPane parent) {
        super(layout);
        setName("mts");
        dto = loadDto(Paths.get("./brokers/" + getName() + ".dto"), getName());
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

                        tf01 = getFTF(String.format("%.0f р.", dto.getInputList().get(0)), Color.RED, keyListener);
                        tf02 = getFTF(String.format("%.0f р.", dto.getInputList().get(1)), Color.RED, keyListener);
                        tf03 = getFTF(String.format("%.0f р.", dto.getInputList().get(2)), Color.RED, keyListener);
                        tf04 = getFTF(String.format("%.0f р.", dto.getInputList().get(3)), Color.RED, keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", JLabel.CENTER, Color.YELLOW));
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
                                itog01 = textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, Color.RED, Registry.btnsFont3);
                                add(itog01);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = getFTF(String.format("%.0f р.", dto.getOutputList().get(0)), keyListener);
                        tf12 = getFTF(String.format("%.0f р.", dto.getOutputList().get(1)), keyListener);
                        tf13 = getFTF(String.format("%.0f р.", dto.getOutputList().get(2)), keyListener);
                        tf14 = getFTF(String.format("%.0f р.", dto.getOutputList().get(3)), keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", JLabel.CENTER, Color.YELLOW));
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
                                itog02 = textLabel(String.format("%,.0f р.", sum + sumWasConst), JLabel.CENTER, Color.white, Registry.btnsFont3);
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
                                add(textLabel("Прибыль:", JLabel.CENTER, Color.YELLOW));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = dto.getOutputList().get(0) - dto.getInputList().get(0);
                                add(textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Registry.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = dto.getOutputList().get(1) - dto.getInputList().get(1);
                                add(textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Registry.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = dto.getOutputList().get(2) - dto.getInputList().get(2);
                                add(textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Registry.btnsFont6));
                            }
                        });
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setOpaque(false);
                                Float sum = dto.getOutputList().get(3) + sumWasConst - dto.getInputList().get(3);
                                add(textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Registry.btnsFont6));
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
                                itog3 = textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Registry.btnsFont3);
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
                setPreferredSize(new Dimension(parent.getTitleWidth(), 0));
                add(textLabel("МТС", null, JLabel.CENTER, Color.RED, Registry.btnsFont7), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", null, JLabel.CENTER, Color.RED, Registry.btnsFont8), BorderLayout.SOUTH);
            }
        };

        add(titlePane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
    }

    void preSave() throws IOException {
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
            Float sum1 = 0f;
            sum1 += Float.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText().replace(" р.", ""));
            sum1 += Float.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText().replace(" р.", ""));
            itog01.setText(String.format("%,.0f р.", sum1));

            Float sum2 = 0f;
            sum2 += Float.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText().replace(" р.", ""));
            sum2 += Float.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText().replace(" р.", ""));
            itog02.setText(String.format("%,.0f р.", sum2 + sumWasConst));

            itog3.setText(String.format("%,.0f р.", (sum2 + sumWasConst - sum1)));
        } catch (Exception ew) {
            System.err.println("Caution: " + ew.getMessage());
        }
    }
}
