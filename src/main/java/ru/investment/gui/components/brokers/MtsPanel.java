package ru.investment.gui.components.brokers;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.investment.config.constants.Constant;
import ru.investment.entity.BrokerDataList;
import ru.investment.entity.dto.BrokerDTO;
import ru.investment.gui.abstracts.AbstractBroker;
import ru.investment.gui.components.DohodPercentPane;
import ru.investment.gui.components.MyFields;
import ru.investment.service.BrokerService;
import ru.investment.utils.UniversalNumberParser;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.investment.gui.components.MyFields.textLabel;

@Slf4j
@Component
@EqualsAndHashCode(callSuper = false)
public class MtsPanel extends AbstractBroker implements KeyListener {
    private static @ToString.Exclude KeyListener keyList;
    private static @ToString.Exclude JTextField tf01, tf02, tf03, tf04;
    private static @ToString.Exclude JTextField tf11, tf12, tf13, tf14;
    private final UUID uuid = UUID.randomUUID();
    private transient @ToString.Exclude BrokerService brokerService;
    private @ToString.Exclude JLabel itog01, itog02, itog3;
    private @ToString.Exclude MyFields.SumPanel p01, p02, p03, p04;
    private DohodPercentPane dohodPercentPane;

    private Color mtsColor = new Color(87, 15, 10);

    public MtsPanel() {
        setName("mts");
        setLayout(new BorderLayout(0, 0));
        keyList = this;

        setBackground(mtsColor);
    }

    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostConstruct
    public void postInit() {
        Optional<BrokerDTO> found = brokerService.findBrokerByName(getName());
        found.ifPresent(brokerDTO -> setDto(brokerDTO));
        if (getDto() == null) {
            setDto(BrokerDTO.builder()
                    .name(getName())
                    .build());
        }

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
                                setBackground(mtsColor);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane2 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf01 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(0)), Color.RED, keyList);
                        tf02 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(1)), Color.RED, keyList);
                        tf03 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(2)), Color.RED, keyList);
                        tf04 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(3)), Color.RED, keyList);

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
                                setBackground(mtsColor);
                                Float sum = 0f;
                                for (Float aFloat : getDto().getData().inputs) {
                                    sum += aFloat;
                                }
                                itog01 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.RED, Constant.fontTableSumRow);
                                add(itog01);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(0)), keyList);
                        tf12 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(1)), keyList);
                        tf13 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(2)), keyList);
                        tf14 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(3)), keyList);

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
                                setBackground(mtsColor);
                                float sum = 0f;
                                for (Float aFloat : getDto().getData().outputs) {
                                    sum += aFloat;
                                }
                                itog02 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.white, Constant.fontTableSumRow);
                                add(itog02);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane4 = new JPanel(new GridLayout(6, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        p01 = MyFields.getJPWT(String.format("%.0f р.", getDtoOutputListValue(0) - getDtoInputListValue(0)));
                        p02 = MyFields.getJPWT(String.format("%.0f р.", getDtoOutputListValue(1) - getDtoInputListValue(1)));
                        p03 = MyFields.getJPWT(String.format("%.0f р.", getDtoOutputListValue(2) - getDtoInputListValue(2)));
                        p04 = MyFields.getJPWT(String.format("%.0f р.", getDtoOutputListValue(3) - getDtoInputListValue(3)));
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Прибыль:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(p01);
                        add(p02);
                        add(p03);
                        add(p04);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(mtsColor);
                                float itog1 = 0f;
                                itog1 += UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
                                itog1 += UniversalNumberParser.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText());
                                itog1 += UniversalNumberParser.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText());
                                itog1 += UniversalNumberParser.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText());

                                float itog2 = 0f;
                                itog2 += UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
                                itog2 += UniversalNumberParser.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText());
                                itog2 += UniversalNumberParser.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText());
                                itog2 += UniversalNumberParser.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText());

                                float sum = itog2 - itog1;
                                itog3 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.fontTableSumRow);
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
                                setBackground(mtsColor);
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
                return !getDto().getData().inputs.isEmpty() ? getDto().getData().inputs.get(index) : 0;
            }

            private float getDtoOutputListValue(int index) {
                return !getDto().getData().outputs.isEmpty() ? getDto().getData().outputs.get(index) : 0;
            }
        };

        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(mtsColor);
                setPreferredSize(new Dimension(Constant.TITLE_WIDTH, 0));
                add(textLabel("МТС", null, SwingConstants.CENTER, Color.RED, Constant.fontEmitentLabel), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", null, SwingConstants.CENTER, Color.RED, Constant.fontFinalSum), BorderLayout.SOUTH);
            }
        };

        add(titlePane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateBrokerValues();
    }

    private void updateBrokerValues() {
        try {
            float sum1 = 0f, sum2 = 0f;

            sum1 += UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
            sum1 += UniversalNumberParser.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText());
            sum1 += UniversalNumberParser.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText());
            sum1 += UniversalNumberParser.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText());
            itog01.setText(String.format("%,.0f р.", sum1));

            sum2 += UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
            sum2 += UniversalNumberParser.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText());
            sum2 += UniversalNumberParser.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText());
            sum2 += UniversalNumberParser.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText());

            itog02.setText(String.format("%,.0f р.", sum2));

            p01.setSum(UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText()) -
                    UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText()));
            p02.setSum(UniversalNumberParser.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText()) -
                    UniversalNumberParser.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText()));
            p03.setSum(UniversalNumberParser.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText()) -
                    UniversalNumberParser.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText()));
            p04.setSum(UniversalNumberParser.parseFloat(tf14.getText().isBlank() ? "0" : tf14.getText()) -
                    UniversalNumberParser.parseFloat(tf04.getText().isBlank() ? "0" : tf04.getText()));

            itog3.setText(String.format("%,.0f р.", (sum2 - sum1)));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }

    @Override
    public BrokerDTO getSavingData() {
        BrokerDataList inps = BrokerDataList.builder()
                .inputs(
                        List.of(
                                UniversalNumberParser.parseFloat(tf01.getText()),
                                UniversalNumberParser.parseFloat(tf02.getText()),
                                UniversalNumberParser.parseFloat(tf03.getText()),
                                UniversalNumberParser.parseFloat(tf04.getText())
                        )
                )
                .outputs(
                        List.of(
                                UniversalNumberParser.parseFloat(tf11.getText()),
                                UniversalNumberParser.parseFloat(tf12.getText()),
                                UniversalNumberParser.parseFloat(tf13.getText()),
                                UniversalNumberParser.parseFloat(tf14.getText())
                        ))
                .build();

        return BrokerDTO.builder()
                .name(getName())
                .data(inps)
                .build();
    }

    public void reload() {
        tf01.setText(String.valueOf(getDto().getData().inputs.get(0)));
        tf02.setText(String.valueOf(getDto().getData().inputs.get(1)));
        tf03.setText(String.valueOf(getDto().getData().inputs.get(2)));
        tf04.setText(String.valueOf(getDto().getData().inputs.get(3)));

        tf11.setText(String.valueOf(getDto().getData().outputs.get(0)));
        tf12.setText(String.valueOf(getDto().getData().outputs.get(1)));
        tf13.setText(String.valueOf(getDto().getData().outputs.get(2)));
        tf14.setText(String.valueOf(getDto().getData().outputs.get(3)));
    }


    // other:
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }
}
