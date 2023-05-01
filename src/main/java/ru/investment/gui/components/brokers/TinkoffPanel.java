package ru.investment.gui.components.brokers;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.investment.config.constants.Constant;
import ru.investment.entity.BrokerDataList;
import ru.investment.entity.dto.BrokerDTO;
import ru.investment.gui.BrokersPane;
import ru.investment.gui.abstracts.AbstractBroker;
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

import static ru.investment.gui.components.MyFields.textLabel;

@Slf4j
@Component
@EqualsAndHashCode(callSuper = false)
public class TinkoffPanel extends AbstractBroker implements KeyListener {
    private static @ToString.Exclude KeyListener kList;
    private static @ToString.Exclude JTextField tf01, tf02, tf03;
    private static @ToString.Exclude JTextField tf11, tf12, tf13;
    private transient @ToString.Exclude BrokerService brokerService;
    private transient @ToString.Exclude BrokersPane brokersPane;
    private @ToString.Exclude JLabel itog01, itog02;
    private transient BrokerDTO dto;

    public TinkoffPanel() {
        setName("tkf");
        setLayout(new BorderLayout(0, 0));
        kList = this;

        setBackground(Color.ORANGE.darker().darker());
    }

    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @PostConstruct
    void postInit() {
        Optional<BrokerDTO> found = brokerService.findBrokerByName(getName());
        found.ifPresent(brokerDTO -> dto = brokerDTO);
        if (dto == null) {
            dto = BrokerDTO.builder()
                    .name(getName())
                    .build();
        }

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

                        tf01 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(0)), Color.RED, kList);
                        tf02 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(1)), Color.RED, kList);
                        tf03 = MyFields.getFTF(String.format("%.0f р.", getDtoInputListValue(2)), Color.RED, kList);

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
                                for (Float aFloat : dto.getData().inputs) {
                                    sum += aFloat;
                                }
                                itog01 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.RED, Constant.fontTableSumRow);
                                add(itog01);
                            }
                        });
                    }
                };
                JPanel orangeCenterPane3 = new JPanel(new GridLayout(5, 1, 0, 0)) {
                    {
                        setOpaque(false);

                        tf11 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(0)), kList);
                        tf12 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(1)), kList);
                        tf13 = MyFields.getFTF(String.format("%.0f р.", getDtoOutputListValue(2)), kList);

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
                                for (Float aFloat : dto.getData().outputs) {
                                    sum += aFloat;
                                }
                                itog02 = textLabel(String.format("%,.0f р.", sum), SwingConstants.CENTER, Color.WHITE, Constant.fontTableSumRow);
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
                return dto.getData().inputs.isEmpty() ? 0 : dto.getData().inputs.get(index);
            }

            private float getDtoOutputListValue(int index) {
                return dto.getData().outputs.isEmpty() ? 0 : dto.getData().outputs.get(index);
            }
        };

        JPanel titlePane = new JPanel(new BorderLayout(0, 0)) {
            {
                setBackground(Color.ORANGE.darker().darker());
                setPreferredSize(new Dimension(Constant.TITLE_WIDTH, 0));
                add(textLabel("Тинькофф", SwingConstants.CENTER, Color.ORANGE, Constant.fontEmitentLabel), BorderLayout.CENTER);
                add(textLabel("ВСЕГО:", SwingConstants.CENTER, Color.ORANGE, Constant.fontFinalSum), BorderLayout.SOUTH);
            }
        };

        add(titlePane, BorderLayout.WEST);
        add(centerPane, BorderLayout.CENTER);
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
            sum += UniversalNumberParser.parseFloat(tf01.getText().isBlank() ? "0" : tf01.getText());
            sum += UniversalNumberParser.parseFloat(tf02.getText().isBlank() ? "0" : tf02.getText());
            sum += UniversalNumberParser.parseFloat(tf03.getText().isBlank() ? "0" : tf03.getText());
            itog01.setText(String.format("%,.0f р.", sum));

            sum = 0f;
            sum += UniversalNumberParser.parseFloat(tf11.getText().isBlank() ? "0" : tf11.getText());
            sum += UniversalNumberParser.parseFloat(tf12.getText().isBlank() ? "0" : tf12.getText());
            sum += UniversalNumberParser.parseFloat(tf13.getText().isBlank() ? "0" : tf13.getText());
            itog02.setText(String.format("%,.0f р.", sum));
        } catch (Exception ew) {
            log.error("Caution: {}", ew.getMessage());
        }
    }

    @Override
    public BrokerDTO getSavingData() {
        BrokerDataList data = BrokerDataList.builder()
                .inputs(List.of(
                        UniversalNumberParser.parseFloat(tf01.getText()),
                        UniversalNumberParser.parseFloat(tf02.getText()),
                        UniversalNumberParser.parseFloat(tf03.getText())
                ))
                .outputs(List.of(
                        UniversalNumberParser.parseFloat(tf11.getText()),
                        UniversalNumberParser.parseFloat(tf12.getText()),
                        UniversalNumberParser.parseFloat(tf13.getText())
                ))
                .build();

        return BrokerDTO.builder()
                .name(getName())
                .data(data)
                .build();
    }

    @Override
    public String toString() {
        return "TinkoffPanel{" +
                "dto=" + dto +
                ", itog01=" + itog01 +
                ", itog02=" + itog02 +
                '}';
    }
}
