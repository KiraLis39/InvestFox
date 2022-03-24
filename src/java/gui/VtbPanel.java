package gui;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dto.BrokerDTO;
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

@ToString
@NoArgsConstructor
@JsonSerialize
public class VtbPanel extends AbstractBroker implements KeyListener {
    private BrokerDTO dto;

    private JTextField tf01;
    private JTextField tf11;
    private JLabel itog01, itog02;
    private KeyListener keyListener;

    public VtbPanel(LayoutManager layout, PortfelPane parent) {
        super(layout);
        setName("vtb");
        dto = loadDto(Paths.get("./brokers/" + getName() + ".dto"), getName());
        keyListener = this;

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

                        tf01 = getFTF(String.format("%.0f р.", dto.getInputList().get(0)), Color.RED, keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", JLabel.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf01);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
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
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(3, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = getFTF(String.format("%.0f р.", dto.getOutputList().get(0)), keyListener);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", JLabel.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf11);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : dto.getOutputList()) {
                                    sum += aFloat;
                                }
                                itog02 = textLabel(String.format("%,.0f р.", sum), JLabel.CENTER, Color.white, Registry.btnsFont3);
                                add(itog02);
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
                setPreferredSize(new Dimension(parent.getTitleWidth(), 0));
                add(textLabel("ВТБ", JLabel.CENTER, new Color(0.8f, 0.8f, 1.0f), Registry.btnsFont7), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", JLabel.CENTER, new Color(0.8f, 0.8f, 1.0f), Registry.btnsFont8), BorderLayout.SOUTH);
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

        dto.setInputList(new ArrayList<>(1) {{
            add(Float.parseFloat(tf01.getText().replace(" р.", "")));
        }});
        dto.setOutputList(new ArrayList<>(1) {{
            add(Float.parseFloat(tf11.getText().replace(" р.", "")));
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
            Float sum = Float.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
            itog01.setText(String.format("%,.0f р.", sum));

            sum = Float.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
            itog02.setText(String.format("%,.0f р.", sum));
        } catch (Exception ew) {
            System.err.println("Caution: " + ew.getMessage());
        }
    }
}
