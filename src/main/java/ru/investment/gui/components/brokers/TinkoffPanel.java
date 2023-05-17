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
public class TinkoffPanel extends AbstractBroker implements KeyListener {
    private static @ToString.Exclude KeyListener kList;
    private static @ToString.Exclude JTextField inputRowField01, inputRowField02, inputRowField03;
    private static @ToString.Exclude JTextField todayRowField01, todayRowField02, todayRowField03;
    private final Color tinkColor = new Color(142, 74, 10);
    private transient @ToString.Exclude BrokerService brokerService;
    private @ToString.Exclude MyFields.SumPanel itogRowPane01, itogRowPane02, itogRowPane03;
    private @ToString.Exclude JLabel sumLabel01, sumLabel02, sumLabel03;
    private DohodPercentPane dohodPercentPane;

    public TinkoffPanel() {
        setName("tkf");
        setLayout(new BorderLayout(0, 0));
        kList = this;

        setBackground(tinkColor);
    }

    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostConstruct
    void postInit() {
        Optional<BrokerDTO> found = brokerService.findBrokerByName(getName());
        found.ifPresent(this::setDto);
        if (getDto() == null) {
            setDto(BrokerDTO.builder()
                    .name(getName())
                    .build());
        }

        JPanel centerPane = new JPanel(new GridLayout(1, 5, 0, 0)) {
            {
                setBackground(Color.DARK_GRAY.darker());
                setBorder(new EmptyBorder(0, -1, -1, 0));

                JPanel branchPanel = new JPanel(new GridLayout(5, 1, 0, 0)) {
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
                                setBackground(tinkColor);
                            }
                        });
                    }
                };
                JPanel inputPanel = new JPanel(new GridLayout(5, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        inputRowField01 = MyFields.getFTF(String.valueOf(getDtoInputListValue(0)), Color.RED, kList);
                        inputRowField02 = MyFields.getFTF(String.valueOf(getDtoInputListValue(1)), Color.RED, kList);
                        inputRowField03 = MyFields.getFTF(String.valueOf(getDtoInputListValue(2)), Color.RED, kList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Вложено:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(inputRowField01);
                        add(inputRowField02);
                        add(inputRowField03);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(tinkColor);
                                Float sum = 0f;
                                for (Float aFloat : getDto().getData().inputs) {
                                    sum += aFloat;
                                }
                                sumLabel01 = textLabel(String.valueOf(sum), SwingConstants.CENTER, Color.RED, Constant.fontTableSumRow);
                                add(sumLabel01);
                            }
                        });
                    }
                };
                JPanel todayPanel = new JPanel(new GridLayout(5, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        todayRowField01 = MyFields.getFTF(String.valueOf(getDtoOutputListValue(0)), kList);
                        todayRowField02 = MyFields.getFTF(String.valueOf(getDtoOutputListValue(1)), kList);
                        todayRowField03 = MyFields.getFTF(String.valueOf(getDtoOutputListValue(2)), kList);

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("На сегодня:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(todayRowField01);
                        add(todayRowField02);
                        add(todayRowField03);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(tinkColor);
                                Float sum = 0f;
                                for (Float aFloat : getDto().getData().outputs) {
                                    sum += aFloat;
                                }
                                sumLabel02 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.WHITE, Constant.fontTableSumRow);
                                add(sumLabel02);
                            }
                        });
                    }
                };
                JPanel dohodPane = new JPanel(new GridLayout(5, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        itogRowPane01 = MyFields.getJPWT(String.valueOf(getDtoOutputListValue(0) - getDtoInputListValue(0)));
                        itogRowPane02 = MyFields.getJPWT(String.valueOf(getDtoOutputListValue(1) - getDtoInputListValue(1)));
                        itogRowPane03 = MyFields.getJPWT(String.valueOf(getDtoOutputListValue(2) - getDtoInputListValue(2)));

                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                add(textLabel("Прибыль:", SwingConstants.CENTER, Color.YELLOW));
                            }
                        });
                        add(itogRowPane01);
                        add(itogRowPane02);
                        add(itogRowPane03);
                        add(new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(tinkColor);
                                float itog1 = getDtoInputListValue(0);
                                itog1 += getDtoInputListValue(1);
                                itog1 += getDtoInputListValue(2);

                                float itog2 = getDtoOutputListValue(0);
                                itog2 += getDtoOutputListValue(1);
                                itog2 += getDtoOutputListValue(2);

                                float sum = itog2 - itog1;
                                sumLabel03 = textLabel(String.valueOf(sum),
                                        SwingConstants.CENTER, sum > 0 ? Color.GREEN : Color.RED, Constant.fontTableSumRow);
                                add(sumLabel03);
                            }
                        });
                    }
                };

                dohodPercentPane = new DohodPercentPane(5,
                        new LinkedHashMap<>() {
                            {
                                put(0, Map.of(itogRowPane01.getText(), inputRowField01.getText()));
                                put(1, Map.of(itogRowPane02.getText(), inputRowField02.getText()));
                                put(2, Map.of(itogRowPane03.getText(), inputRowField03.getText()));
                            }
                        }, tinkColor);

                add(branchPanel);
                add(inputPanel);
                add(todayPanel);
                add(dohodPane);
                add(dohodPercentPane);
            }

            private float getDtoInputListValue(int index) {
                return getDto().getData().inputs.isEmpty() ? 0 : getDto().getData().inputs.get(index);
            }

            private float getDtoOutputListValue(int index) {
                return getDto().getData().outputs.isEmpty() ? 0 : getDto().getData().outputs.get(index);
            }
        };

        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(tinkColor);
                setPreferredSize(new Dimension(Constant.TITLE_WIDTH, 0));
                add(textLabel("Тинькофф", SwingConstants.CENTER, Color.ORANGE, Constant.fontEmitentLabel), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", SwingConstants.CENTER, Color.ORANGE, Constant.fontFinalSum), BorderLayout.SOUTH);
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

            // вложено:
            float sum001 = UniversalNumberParser.parseFloat(inputRowField01.getText().isBlank() ? "0" : inputRowField01.getText());
            float sum011 = UniversalNumberParser.parseFloat(inputRowField02.getText().isBlank() ? "0" : inputRowField02.getText());
            float sum111 = UniversalNumberParser.parseFloat(inputRowField03.getText().isBlank() ? "0" : inputRowField03.getText());
            sumLabel01.setText(String.format("%,.0f р.", (sum001 + sum011 + sum111)));

            float sum002 = UniversalNumberParser.parseFloat(todayRowField01.getText().isBlank() ? "0" : todayRowField01.getText());
            float sum022 = UniversalNumberParser.parseFloat(todayRowField02.getText().isBlank() ? "0" : todayRowField02.getText());
            float sum222 = UniversalNumberParser.parseFloat(todayRowField03.getText().isBlank() ? "0" : todayRowField03.getText());
            sumLabel02.setText(String.format("%,.0f р.", (sum002 + sum022 + sum222)));

            float sum = sum002 - sum001;
            itogRowPane01.setSum(sum);
            dohodPercentPane.getItogPersentRowLabel(0).setSum(sum / sum001 * 100);

            sum = sum022 - sum011;
            itogRowPane02.setSum(sum);
            dohodPercentPane.getItogPersentRowLabel(1).setSum(sum / sum011 * 100);

            sum = sum222 - sum111;
            itogRowPane03.setSum(sum);
            dohodPercentPane.getItogPersentRowLabel(2).setSum(sum / sum111 * 100);

            sumLabel03.setText(String.format("%,.0f р.", ((sum002 + sum022 + sum222) - (sum001 + sum011 + sum111))));

            dohodPercentPane.getItogPersentRowLabel(0).setSum(UniversalNumberParser.parseFloat(sumLabel03.getText()) / (sum001 + sum011 + sum111) * 100);
            dohodPercentPane.getItogPercentLabel().setText(String.format("%s %%", Math.round(UniversalNumberParser.parseFloat(sumLabel03.getText()) / (sum001 + sum011 + sum111) * 100)));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }

    @Override
    public BrokerDTO getSavingData() {
        BrokerDataList data = BrokerDataList.builder()
                .inputs(List.of(
                        UniversalNumberParser.parseFloat(inputRowField01.getText()),
                        UniversalNumberParser.parseFloat(inputRowField02.getText()),
                        UniversalNumberParser.parseFloat(inputRowField03.getText())
                ))
                .outputs(List.of(
                        UniversalNumberParser.parseFloat(todayRowField01.getText()),
                        UniversalNumberParser.parseFloat(todayRowField02.getText()),
                        UniversalNumberParser.parseFloat(todayRowField03.getText())
                ))
                .build();

        return BrokerDTO.builder()
                .name(getName())
                .data(data)
                .build();
    }

    public void reload() {
        inputRowField01.setText(String.valueOf(getDto().getData().inputs.get(0)));
        inputRowField02.setText(String.valueOf(getDto().getData().inputs.get(1)));
        inputRowField03.setText(String.valueOf(getDto().getData().inputs.get(2)));

        todayRowField01.setText(String.valueOf(getDto().getData().outputs.get(0)));
        todayRowField02.setText(String.valueOf(getDto().getData().outputs.get(1)));
        todayRowField03.setText(String.valueOf(getDto().getData().outputs.get(2)));
    }

    // other:
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }
}
