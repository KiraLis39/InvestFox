package ru.investment.gui;

import fox.components.FOptionPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.investment.NetProcessor;
import ru.investment.ShareCollectedDTO;
import ru.investment.config.ApplicationProperties;
import ru.investment.config.constants.Constant;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.service.ShareService;
import ru.investment.service.VaultService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Component
public class InvestFrame extends JFrame implements WindowListener, ComponentListener {
    private final transient ApplicationProperties props; // @Bean
    private final transient NetProcessor netProc; // @Bean
    private final transient VaultService vaultService; // @Bean
    private final transient ShareService shareService; // @Bean
    private final BrokersPane brokersPane; // @Bean
    private final TablePane tablePane; // @Bean
    private JPanel baseMidPane;
    private JTextField ticketField;
    private JLabel titleLabel, recomLabel, sectorLabel, lotLabel, costLabel, lotCostLabel,
            divLabel, payDateLabel, usdValueLabel, eurValueLabel;
    private ImageIcon ico01, ico02, ico03, ico04;
    private transient Thread valuteThread;

    public void addPanel(ShareDTO dto) {
        baseMidPane.add(new DataPanel(dto));
        baseMidPane.revalidate();
    }

    @Autowired
    public void init() {
        setTitle("Invest Fox 2023 v." + props.getVersion());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 768));
        setPreferredSize(new Dimension(1600, 800));
        setResizable(true);

        preInit();

        JTabbedPane tabPane = new JTabbedPane(SwingConstants.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT) {
            {
                setBackground(Color.BLACK);
                getContentPane().setBackground(Color.BLACK);
                setBorder(new EmptyBorder(0, 0, 6, 0));

                JPanel basePane = new JPanel(new BorderLayout(0, 1)) {
                    {
                        JPanel upPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)) {
                            {
                                setBackground(Color.BLACK);
                                setBorder(new EmptyBorder(0, 1, 0, 0));

                                ticketField = new JTextField() {
                                    {
                                        setFont(Constant.fontPrimaryHeaders);
                                        setColumns(6);
                                        setHorizontalAlignment(0);
                                        setAlignmentY(1);
                                        addFocusListener(new FocusAdapter() {
                                            @Override
                                            public void focusGained(FocusEvent e) {
                                                selectAll();
                                            }
                                        });
                                        addKeyListener(new KeyAdapter() {
                                            @Override
                                            public void keyPressed(KeyEvent e) {
                                                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                                    runScan();
                                                }
                                            }

                                            @Override
                                            public void keyReleased(KeyEvent e) {
                                                setText(getText().toUpperCase());
                                            }
                                        });
                                    }
                                };

                                JButton updateButton = new JButton("Сканировать") {
                                    {
                                        setFocusPainted(false);
                                        setFont(Constant.fontPrimaryHeaders);
                                        addActionListener(e -> {
                                            runScan();
                                        });
                                    }
                                };

                                JPanel valutePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)) {
                                    {
                                        setOpaque(false);
                                        setBorder(BorderFactory.createSoftBevelBorder(1));

                                        usdValueLabel = new JLabel("USD: ") {
                                            {
                                                setForeground(Color.WHITE);
                                            }
                                        };
                                        eurValueLabel = new JLabel("EUR: ") {
                                            {
                                                setForeground(Color.WHITE);
                                            }
                                        };

                                        add(usdValueLabel);
                                        add(eurValueLabel);
                                    }
                                };

                                add(ticketField);
                                add(updateButton);
                                add(valutePane);
                            }
                        };

                        baseMidPane = new JPanel(new GridLayout(2, 0, 0, 0)) {
                            {
                                setBackground(Color.DARK_GRAY.darker());
                            }
                        };

                        JPanel downPane = new JPanel(new BorderLayout(0, 0)) {
                            {
                                setPreferredSize(new Dimension(0, 170));
                                setBackground(Color.PINK.darker());
                                setBorder(new EmptyBorder(3, 6, 3, 6));

                                JPanel upPane = new JPanel() {
                                    {
                                        setOpaque(false);

                                        titleLabel = new JLabel();

                                        add(titleLabel);
                                    }
                                };
                                JPanel midPane = new JPanel(new GridLayout(6, 2, 6, 3)) {
                                    {
                                        setOpaque(false);

                                        sectorLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };
                                        lotLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };
                                        costLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };
                                        lotCostLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };
                                        divLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };
                                        payDateLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(SwingConstants.CENTER);
                                            }
                                        };

                                        add(sectorLabel);
                                        add(lotLabel);
                                        add(costLabel);
                                        add(lotCostLabel);
                                        add(divLabel);
                                        add(payDateLabel);
                                    }
                                };
                                JPanel botPane = new JPanel() {
                                    {
                                        setOpaque(false);

                                        recomLabel = new JLabel();

                                        add(recomLabel);
                                    }
                                };

                                add(upPane, BorderLayout.NORTH);
                                add(midPane, BorderLayout.CENTER);
                                add(botPane, BorderLayout.SOUTH);
                            }
                        };

                        add(upPane, BorderLayout.NORTH);
                        add(baseMidPane, BorderLayout.CENTER);
                        add(downPane, BorderLayout.SOUTH);
                    }
                };

                addTab("Анализ", ico01, basePane, "Анализ конкретных акций");
                addTab("Сводка", ico02, tablePane, "Сводка по текущей ситуации");
                addTab("Портфель", ico03, brokersPane, "Состояние портфеля");
                addTab("План ", ico04, new JLabel("NA"), "Мой план");

                setBackgroundAt(0, new Color(255, 200, 200));
                setBackgroundAt(1, new Color(200, 200, 255));
                setBackgroundAt(2, new Color(190, 255, 200));
                setBackgroundAt(3, new Color(255, 255, 175));
            }
        };
        add(tabPane);
        ticketField.setText("RASP");

        addWindowListener(this);
        addComponentListener(this);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        postInit();
    }

    public BrokersPane getPortfel() {
        return brokersPane;
    }

    public TablePane getTablePane() {
        return tablePane;
    }

    private void preInit() {
        try {
            valuteThread = new Thread(vaultService::loadVaults);
            valuteThread.start(); // получаем курс валют

            loadIcons(); // подгружаем иконки приложения
        } catch (IOException e) {
            log.error("Exception here: {}", e.getMessage());
        }
    }

    private void postInit() {
        try {
            valuteThread.join(); // ждем конца сбора данных валют и отображаем ниже:
            usdValueLabel.setText("<html><p style=\"color:#8F8\"><b>USD: </b></p>" + vaultService.getUSDValue());
            eurValueLabel.setText("<html><p style=\"color:#88F\"><b>EUR: </b></p>" + vaultService.getEURValue());
            shareService.loadTable();
        } catch (InterruptedException | IOException e) {
            log.error("Exception here: {}", e.getMessage());
        }

        Constant.inAc.add("tabedPane", tablePane);
        Constant.inAc.set("tabedPane", "showSearchDialog", KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.showSearchDialog();
            }
        });
        Constant.inAc.set("tabedPane", "showNextSearch", KeyEvent.VK_F3, 0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.showNextSearched();
            }
        });
    }

    private void loadIcons() throws IOException {
        InputStream strRes = getClass().getResourceAsStream("/img/scan.png");
        if (strRes != null) {
            ico01 = new ImageIcon(ImageIO.read(strRes));
        }

        strRes = getClass().getResourceAsStream("/img/table.png");
        if (strRes != null) {
            ico02 = new ImageIcon(ImageIO.read(strRes));
        }

        strRes = getClass().getResourceAsStream("/img/portfel.png");
        if (strRes != null) {
            ico03 = new ImageIcon(ImageIO.read(strRes));
        }

        strRes = getClass().getResourceAsStream("/img/plan.png");
        if (strRes != null) {
            ico04 = new ImageIcon(ImageIO.read(strRes));
        }
    }

    // при нажатии на кнопку поиска:
    private void runScan() {
        try {
            baseMidPane.removeAll();
            netProc.runScan(ticketField.getText());
        } catch (Exception ex) {
            log.warn("Exception here: {}", ex.getMessage());
            new FOptionPane().buildFOptionPane("Error:", "Сканирование провалилось: " + ex.getMessage());
        }
    }

    public synchronized void updateDownPanel(ShareCollectedDTO result) {
        titleLabel.setText(String.format("<html>Обобщение по тикету: <font color=\"#FFF\"><b>'%s'", result.getTicker()));

        sectorLabel.setText(String.format("<html>Сектор: <font color=\"#FFF\"><b>%s",
                result.getSectors().isEmpty() ? "=NA=" : result.getSectors()));

        lotLabel.setText(String.format("<html>Лот: <font color=\"#FFF\"><b>%s", result.getLotSize() + " шт."));

        costLabel.setText(String.format("<html>Цена: <font color=\"#FFF\"><b>%.2f (%s)",
                result.getCost(), result.getCostType()).replace("[", "").replace("]", ""));

        lotCostLabel.setText(String.format("<html>Цена за лот: <font color=\"#FFF\"><b>%.2f (%s)",
                result.getLotCost(), result.getCostType()));

        divLabel.setText(String.format("<html>Дивиденды: <font color=\"#FFF\"><b>%.2f ",
                result.getDividend()).replace("[", "").replace("]", "") + "%");

        payDateLabel.setText(String.format("<html>Дата выплаты: <font color=\"#FFF\"><b>%s",
                result.getNextPayDate() == null ? "" : result.getNextPayDate().toLocalDate()));

        recomLabel.setText(String.format("<html>Рекомендация: <font color=\"#FFF\"><b>%s", result.getRecommendation()));
    }

    @Override
    public void windowClosing(WindowEvent e) {
        netProc.exit();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        SwingUtilities.invokeLater(brokersPane::updatePanesDim);
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    private static class DataPanel extends JPanel {
        public DataPanel(ShareDTO dto) {

            setBorder(new EmptyBorder(9, 6, 0, 3));

            if (dto == null) {
                setLayout(new BorderLayout());
                add(new JLabel("= NA ="));
            } else {
                setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));

                JPanel labelPane = new JPanel(new GridLayout(0, 1, 0, 3)) {
                    {
                        setOpaque(false);

                        add(new JLabel("Source:"));
                        add(new JLabel("Sector:"));
                        add(new JLabel("Name:"));
                        add(new JLabel("Ticket:"));
                        add(new JLabel("Cost:"));
                        add(new JLabel("Lot:"));
                        add(new JLabel("Dividend:"));
                        add(new JLabel("Pay date:"));
                        add(new JLabel("Recommendation:"));
                    }
                };

                JPanel valuesPane = new JPanel(new GridLayout(0, 1, 0, 3)) {
                    {
                        setOpaque(false);

                        add(new JLabel(dto.getSource()));
                        add(new JLabel(dto.getSector()));
                        add(new JLabel(dto.getName()));
                        add(new JLabel(dto.getTicker()));
                        add(new JLabel(dto.getCoasts() + (dto.getCostType() == null ? "" : " " + dto.getCostType())));
                        add(new JLabel(dto.getLotSize() > -1 ? dto.getLotSize() + " шт." : "NA"));
                        add(new JLabel(dto.getDividendsStringized()));
                        add(new JLabel(dto.getPayDate() != null ? dto.getPayDate().toString() : "NA"));
                        add(new JLabel(dto.getRecomendations() != null ? Arrays.toString(dto.getRecomendations().toArray()) : "NA"));
                    }
                };

                add(labelPane);
                add(valuesPane);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GRAY.darker());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 3, 3);
            g.setColor(Color.WHITE);
            g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 3, 3);
        }
    }
}
