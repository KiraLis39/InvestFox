package gui;

import core.NetProcessor;
import dto.ResultShareDTO;
import dto.ShareDTO;
import registry.Registry;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InvestFrame extends JFrame {
    private static JPanel midPane;
    private static JTextField ticketField;
    private static JLabel titleLabel, recomLabel, sectorLabel, lotLabel, costLabel, lotCostLabel, divLabel, payDateLabel;
    private static TablePane tablePane;

    private NetProcessor netProc = new NetProcessor();

    public InvestFrame() {
        setTitle("Invest Fox 2022 v." + Registry.version);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 768));
        setPreferredSize(new Dimension(1920, 768));
        setResizable(true);

        JTabbedPane tabPane = new JTabbedPane(JTabbedPane.BOTTOM, 0) {
            {
                setBackground(Color.DARK_GRAY);
                getContentPane().setBackground(Color.DARK_GRAY);

                JPanel basePane = new JPanel(new BorderLayout(0, 0)) {
                    {
                        setBackground(Color.DARK_GRAY);

                        JPanel upPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
                            {
                                setBackground(Color.BLACK);
                                setBorder(new EmptyBorder(0, 3, 0, 0));

                                add(new JLabel("Тикер:") {{setForeground(Color.WHITE);}});
                                ticketField = new JTextField() {
                                    {
                                        setColumns(8);
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
                                                    try {runScan();
                                                    } catch (ExecutionException executionException) {
                                                        executionException.printStackTrace();
                                                    } catch (InterruptedException interruptedException) {
                                                        interruptedException.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void keyReleased(KeyEvent e) {
                                                setText(getText().toUpperCase());
                                            }
                                        });
                                    }
                                };

                                JButton updateButton = new JButton("check") {
                                    {
                                        addActionListener(e -> {
                                            try {runScan();
                                            } catch (ExecutionException executionException) {
                                                executionException.printStackTrace();
                                            } catch (InterruptedException interruptedException) {
                                                interruptedException.printStackTrace();
                                            }
                                        });
                                    }
                                };

                                add(ticketField);
                                add(updateButton);
                            }
                        };

                        midPane = new JPanel(new GridLayout(2, 0, 0, 0)) {
                            {
                                setBackground(Color.DARK_GRAY);
                            }
                        };

                        JPanel downPane = new JPanel(new BorderLayout(0, 0)) {
                            {
                                setBackground(Color.PINK.darker());
                                setBorder(new EmptyBorder(3, 6, 3, 6));

                                JPanel upPane = new JPanel() {
                                    {
                                        setOpaque(false);

                                        titleLabel = new JLabel() {
                                            {

                                            }
                                        };

                                        add(titleLabel);
                                    }
                                };
                                JPanel midPane = new JPanel(new GridLayout(6, 2, 6, 3)) {
                                    {
                                        setOpaque(false);

                                        sectorLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
                                            }
                                        };
                                        lotLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
                                            }
                                        };
                                        costLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
                                            }
                                        };
                                        lotCostLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
                                            }
                                        };
                                        divLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
                                            }
                                        };
                                        payDateLabel = new JLabel() {
                                            {
                                                setHorizontalAlignment(JLabel.CENTER);
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

                                        recomLabel = new JLabel() {
                                            {

                                            }
                                        };

                                        add(recomLabel);
                                    }
                                };

                                add(upPane, BorderLayout.NORTH);
                                add(midPane, BorderLayout.CENTER);
                                add(botPane, BorderLayout.SOUTH);
                            }
                        };

                        add(upPane, BorderLayout.NORTH);
                        add(midPane, BorderLayout.CENTER);
                        add(downPane, BorderLayout.SOUTH);
                    }
                };

                tablePane = new TablePane();

                addTab("Анализ", null, basePane, "Анализ конкретных акций");
                addTab("Сводка", null, tablePane, "Сводка по текущей ситуации");
                addTab("Портфель", null, null, "Состояние рынка и портфеля");
                addTab("План", null, null, "Мой план");
            }
        };
        add(tabPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    NetProcessor.save();
                    System.exit(0);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            NetProcessor.load(tablePane);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ticketField.setText("MRKP");
    }

    public static List<Component> getTableRows() {
        return tablePane.getRows();
    }

    public static TablePane getTablePane() {
        return tablePane;
    }

    // при нажатии на кнопку поиска:
    private void runScan() throws ExecutionException, InterruptedException {
        Future<ResultShareDTO> fut = netProc.checkTicket(ticketField.getText().toUpperCase().trim());
        System.out.println("Scanning " + ticketField.getText().toUpperCase().trim() + "...");
        while(!fut.isDone()) {
            Thread.sleep(300);
        }
        updateDownPanel(fut.get());
    }

    private synchronized void updateDownPanel(ResultShareDTO result) {
        titleLabel.setText(String.format("<html>Обобщение по тикету: <font color=\"#FFF\"><b>'%s'", result.getTICKER()));
        sectorLabel.setText(String.format("<html>Сектор: <font color=\"#FFF\"><b>%s", result.getSECTOR()));
        lotLabel.setText(String.format("<html>Лот: <font color=\"#FFF\"><b>%s", result.getLOT_SIZE() + " шт."));
        costLabel.setText(String.format("<html>Цена: <font color=\"#FFF\"><b>%.2f (%s)", result.getCOST(), result.getCOST_TYPE()).replace("[", "").replace("]", ""));
        lotCostLabel.setText(String.format("<html>Цена за лот: <font color=\"#FFF\"><b>%.2f (%s)", result.getLOT_COST(), result.getCOST_TYPE()));
        divLabel.setText(String.format("<html>Дивиденды: <font color=\"#FFF\"><b>%.2f ", result.getDIVIDEND()).replace("[", "").replace("]", "") + "%");
        payDateLabel.setText(String.format("<html>Дата выплаты: <font color=\"#FFF\"><b>%s", result.getPAY_DATE() == null ? "" : result.getPAY_DATE().toLocalDate()));
        recomLabel.setText(String.format("<html>Рекомендация: <font color=\"#FFF\"><b>%s", result.getRECOMMENDATION()));
    }

    public static void clearPanel() {
        midPane.removeAll();
    }

    public static void updatePanel(ShareDTO dto) {
        midPane.add(new DataPanel(dto));
        midPane.revalidate();
    }

    private static ArrayList<DataPanel> getDataPanels() {
        ArrayList<DataPanel> dpList = new ArrayList<>();
        for (Component component : midPane.getComponents()) {
            if (component instanceof DataPanel) {
                dpList.add((DataPanel) component);
            }
        }
        return dpList;
    }

    private static class DataPanel extends JPanel {
        private final ShareDTO dto;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
        }

        public DataPanel(ShareDTO dto) {
            this.dto = dto;

            setBorder(new EmptyBorder(6, 3, 0, 3));
            setBackground(Color.GRAY);

            if (dto == null) {
                setLayout(new BorderLayout());
                add(new JLabel("= NA ="));
            } else {
                setLayout(new FlowLayout(0, 3, 0));

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
                        add(new JLabel(dto.getTicket()));
                        add(new JLabel(dto.getCoastList() + (dto.getCostType() == null ? "" : " " + dto.getCostType())));
                        add(new JLabel(dto.getLotSize() + " шт."));
                        add(new JLabel(dto.getDividendList() + (dto.getDividendList().size() == 0 ? "" : "%")));
                        add(new JLabel(dto.getPayDate() != null ? dto.getPayDate().toString() : "NA"));
                        add(new JLabel(dto.getRecommendation() != null ? Arrays.toString(dto.getRecommendation().toArray()) : "NA"));
                    }
                };

                add(labelPane);
                add(valuesPane);
            }
        }

        public ShareDTO getDTO() {return dto;}
    }
}
