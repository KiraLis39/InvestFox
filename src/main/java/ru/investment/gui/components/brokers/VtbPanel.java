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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.investment.gui.components.MyFields.textLabel;

@Slf4j
@Component
@EqualsAndHashCode(callSuper = false)
public class VtbPanel extends AbstractBroker implements KeyListener {
    private static @ToString.Exclude JTextField tf01, tf11;
    private final transient @ToString.Exclude KeyListener keyList;
    private transient @ToString.Exclude BrokerService brokerService;
    private @ToString.Exclude JLabel inputtedSumLabel, todaySumLabel, itogSumLabel;
    private @ToString.Exclude MyFields.SumPanel itogRowPanel;
    private DohodPercentPane dohodPercentPane;

    public VtbPanel() {
        setName("vtb");
        setLayout(new BorderLayout(0, 0));
        keyList = this;

        setBackground(Color.BLUE.darker().darker());
    }

    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostConstruct
    public void postInit() {
        Optional<BrokerDTO> found = brokerService.findBrokerByName(getName());
        found.ifPresent(this::setDto);
        if (getDto() == null) {
            setDto(BrokerDTO.builder()
                    .name(getName())
                    .build());
        }

        JPanel centerPane = new JPanel() {
            {
                setBackground(Color.DARK_GRAY.darker());
                setLayout(new GridLayout(1, 5, 0, 0));
                setBorder(new EmptyBorder(0, -1, 0, 0));

                JPanel branchPanel = new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new GridLayout(3, 1, 0, 0));

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Ветвь:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(textLabel(" Акции+ETF", SwingConstants.LEFT, Color.white));
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                            }
                        });
                    }
                };
                JPanel inputPanel = new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new GridLayout(3, 1, 0, 0));

                        tf01 = MyFields.getFTF(String.format("%,.0f р.", getDtoInputListValue(0)), Color.RED, keyList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf01);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : getDto().getData().getInputs()) {
                                    sum += aFloat;
                                }
                                inputtedSumLabel = textLabel(String.format("%,.0f р.", sum),
                                        SwingConstants.CENTER, Color.RED, Constant.getFontTableSumRow());
                                add(inputtedSumLabel);
                            }
                        });
                    }
                };
                JPanel todayPanel = new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new GridLayout(3, 1, 0, 0));

                        tf11 = MyFields.getFTF(String.format("%,.0f р.", getDtoOutputListValue(0)), keyList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(tf11);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                                Float sum = 0f;
                                for (Float aFloat : getDto().getData().getOutputs()) {
                                    sum += aFloat;
                                }
                                todaySumLabel = textLabel(String.format("%,.0f р.", sum),
                                        SwingConstants.CENTER, Color.white, Constant.getFontTableSumRow());
                                add(todaySumLabel);
                            }
                        });
                    }
                };
                JPanel dohodPane = new JPanel() {
                    {
                        setOpaque(false);
                        setLayout(new GridLayout(3, 1, 0, 0));

                        itogRowPanel = MyFields.getJPWT(String.format("%,.0f р.", getDtoOutputListValue(0) - getDtoInputListValue(0)));

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Прибыль:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(itogRowPanel);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLUE.darker().darker());
                                float itog1 = 0f;
                                itog1 += UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());

                                float itog2 = 0f;
                                itog2 += UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());

                                float sum = itog2 - itog1;
                                itogSumLabel = textLabel(String.format("%,.0f р.", sum),
                                        SwingConstants.CENTER, sum >= 0 ? Color.GREEN : Color.RED, Constant.getFontTableSumRow());
                                add(itogSumLabel);
                            }
                        });
                    }
                };
                dohodPercentPane = new DohodPercentPane(3,
                        new LinkedHashMap<>() {
                            {
                                put(0, Map.of(itogSumLabel.getText(), String.valueOf(getDtoInputListValue(0))));
                            }
                        }, Color.BLUE.darker());

                add(branchPanel);
                add(inputPanel);
                add(todayPanel);
                add(dohodPane);
                add(dohodPercentPane);
            }

            private float getDtoInputListValue(int index) {
                return getDto().getData().getInputs().isEmpty() ? 0 : getDto().getData().getInputs().get(index);
            }

            private float getDtoOutputListValue(int index) {
                return getDto().getData().getOutputs().isEmpty() ? 0 : getDto().getData().getOutputs().get(index);
            }
        };

        JPanel titlePane = new JPanel() {
            {
                setLayout(new BorderLayout(0, 0));
                setBackground(Color.BLUE.darker().darker());
                setPreferredSize(new Dimension(Constant.TITLE_WIDTH, 0));
                add(textLabel("ВТБ", SwingConstants.CENTER, new Color(0.8f, 0.8f, 1.0f), Constant.FONT_EMITENT_LABEL), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", SwingConstants.CENTER, new Color(0.8f, 0.8f, 1.0f), Constant.FONT_FINAL_SUM), BorderLayout.SOUTH);
            }
        };

        add(titlePane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateBrokerValues();
    }

    private void updateBrokerValues() {
        try {
            float sum1 = UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
            inputtedSumLabel.setText(String.format("%,.0f р.", sum1));

            float sum2 = UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
            todaySumLabel.setText(String.format("%,.0f р.", sum2));

            itogRowPanel.setSum(UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText())
                    - UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText()));

            itogSumLabel.setText(String.format("%,.0f р.", (sum2 - sum1)));

            dohodPercentPane.getItogPersentRowLabel(0).setSum(UniversalNumberParser.parseFloat(itogSumLabel.getText()) / sum1 * 100);
            dohodPercentPane.getItogPercentLabel()
                    .setText(String.format("%s %%", Math.round(UniversalNumberParser.parseFloat(itogSumLabel.getText()) / sum1 * 100)));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }

    @Override
    public BrokerDTO getSavingData() {
        BrokerDataList data = BrokerDataList.builder()
                .inputs(List.of(UniversalNumberParser.parseFloat(tf01.getText())))
                .outputs(List.of(UniversalNumberParser.parseFloat(tf11.getText())))
                .build();

        return BrokerDTO.builder()
                .name(getName())
                .data(data)
                .build();
    }

    public void reload() {
        tf01.setText(String.valueOf(getDto().getData().getInputs().get(0)));
        tf11.setText(String.valueOf(getDto().getData().getOutputs().get(0)));
    }
}
