package gui;

import core.NetProcessor;
import door.MainClass;
import dto.ResultShareDTO;
import dto.ShareDTO;
import fox.InputAction;
import fox.Out;
import registry.Registry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class InvestFrame extends JFrame implements WindowListener, ComponentListener {
    private static JPanel midPane;
    private static JTextField ticketField;
    private static JLabel titleLabel, recomLabel, sectorLabel, lotLabel, costLabel, lotCostLabel, divLabel, payDateLabel,
            usdValueLabel, eurValueLabel;
    private static TablePane tablePane;
    private static PortfelPane portfelPane;
    private static final NetProcessor netProc = new NetProcessor();
    private static ImageIcon ico_01, ico_02, ico_03, ico_04;
    private static Thread valuteThread;
    private static JTabbedPane tabPane;


    public InvestFrame() {
        setTitle("Invest Fox 2022 v." + Registry.version);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 768));
        setPreferredSize(new Dimension(1600, 800));
        setResizable(true);

        preInit();

        tabPane = new JTabbedPane(JTabbedPane.BOTTOM, 0) {
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
                                        setFont(Registry.btnsFont4);
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
                                                    try {
                                                        runScan();
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

                                JButton updateButton = new JButton("??????????????????????") {
                                    {
//                                        setOpaque(false);
//                                        setBackground(Color.WHITE);
//                                        setForeground(Color.BLACK);
                                        setFocusPainted(false);
                                        setFont(Registry.btnsFont4);
                                        addActionListener(e -> {
                                            try {
                                                runScan();
                                            } catch (ExecutionException exec) {
                                                exec.printStackTrace();
                                            } catch (InterruptedException intEx) {
                                                intEx.printStackTrace();
                                            }
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

                        midPane = new JPanel(new GridLayout(2, 0, 0, 0)) {
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
                portfelPane = new PortfelPane();

                addTab("????????????", ico_01, basePane, "???????????? ???????????????????? ??????????");
                addTab("????????????", ico_02, tablePane, "???????????? ???? ?????????????? ????????????????");
                addTab("????????????????", ico_03, portfelPane, "?????????????????? ????????????????");
                addTab("???????? ", ico_04, new JLabel("NA"), "?????? ????????");

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

    public static PortfelPane getPortfel() {
        return portfelPane;
    }

    private void preInit() {
        try {
            valuteThread = new Thread(() -> netProc.loadValutes());
            valuteThread.start(); // ???????????????? ???????? ??????????

            loadIcons(); // ???????????????????? ???????????? ????????????????????
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postInit() {
        try {
            valuteThread.join(); // ???????? ?????????? ?????????? ???????????? ?????????? ?? ???????????????????? ????????:
            usdValueLabel.setText("<html><p style=\"color:#8F8\"><b>USD: </b></p>" + netProc.getUSDValue());
            eurValueLabel.setText("<html><p style=\"color:#88F\"><b>EUR: </b></p>" + netProc.getEURValue());
            NetProcessor.loadTable(tablePane);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputAction.add("tabedPane", tablePane);
        InputAction.set("tabedPane", "showSearchDialog", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.showSearchDialog();
            }
        });
        InputAction.set("tabedPane", "showNextSearch", KeyEvent.VK_F3, 0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablePane.showNextSearched();
            }
        });
    }

    private void loadIcons() throws IOException {
        ico_01 = new ImageIcon(ImageIO.read(new File("./resources/pic/scan.png")));
        ico_02 = new ImageIcon(ImageIO.read(new File("./resources/pic/table.png")));
        ico_03 = new ImageIcon(ImageIO.read(new File("./resources/pic/portfel.png")));
        ico_04 = new ImageIcon(ImageIO.read(new File("./resources/pic/plan.png")));
    }

    public static List<Component> getTableRows() {
        return tablePane.getRows();
    }

    public static TablePane getTablePane() {
        return tablePane;
    }

    // ?????? ?????????????? ???? ???????????? ????????????:
    private void runScan() throws ExecutionException, InterruptedException {
        clearPanel();
        System.out.println("Scanning " + ticketField.getText().toUpperCase().trim() + "...");

        CompletableFuture<ResultShareDTO> fut = netProc.checkTicket(ticketField.getText().toUpperCase().trim(), true)
//                .exceptionally(throwable -> null)
                .handle((r, ex) -> {
                    if (r != null) {
                        return r;
                    } else {
                        Out.Print(InvestFrame.class, Out.LEVEL.WARN, "Problem: " + ex);
                        return null;
                    }
                });
        while (!fut.isDone()) {
            Thread.yield();
        }
        if (fut.get() != null) {
            updateDownPanel(fut.get());
        }
    }

    private synchronized void updateDownPanel(ResultShareDTO result) {
        titleLabel.setText(String.format("<html>?????????????????? ???? ????????????: <font color=\"#FFF\"><b>'%s'", result.getTICKER()));
        sectorLabel.setText(String.format("<html>????????????: <font color=\"#FFF\"><b>%s", result.getSECTOR()));
        lotLabel.setText(String.format("<html>??????: <font color=\"#FFF\"><b>%s", result.getLOT_SIZE() + " ????."));
        costLabel.setText(String.format("<html>????????: <font color=\"#FFF\"><b>%.2f (%s)", result.getCOST(), result.getCOST_TYPE()).replace("[", "").replace("]", ""));
        lotCostLabel.setText(String.format("<html>???????? ???? ??????: <font color=\"#FFF\"><b>%.2f (%s)", result.getLOT_COST(), result.getCOST_TYPE()));
        divLabel.setText(String.format("<html>??????????????????: <font color=\"#FFF\"><b>%.2f ", result.getDIVIDEND()).replace("[", "").replace("]", "") + "%");
        payDateLabel.setText(String.format("<html>???????? ??????????????: <font color=\"#FFF\"><b>%s", result.getPAY_DATE() == null ? "" : result.getPAY_DATE().toLocalDate()));
        recomLabel.setText(String.format("<html>????????????????????????: <font color=\"#FFF\"><b>%s", result.getRECOMMENDATION()));
    }

    public static void clearPanel() {
        midPane.removeAll();
    }

    public static void addPanel(ShareDTO dto) {
        midPane.add(new DataPanel(dto));
        midPane.revalidate();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        MainClass.exit();
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
        SwingUtilities.invokeLater(() -> {
            portfelPane.updatePanesDim();
        });
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }


    private static class DataPanel extends JPanel {
        private final ShareDTO dto;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GRAY.darker());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 3, 3);
            g.setColor(Color.WHITE);
            g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 3, 3);
        }

        public DataPanel(ShareDTO dto) {
            this.dto = dto;

            setBorder(new EmptyBorder(9, 6, 0, 3));

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
                        add(new JLabel(dto.getLotSize() > -1 ? dto.getLotSize() + " ????." : "NA"));
                        add(new JLabel(dto.getDividendList() + (dto.getDividendList().size() == 0 ? "" : "%")));
                        add(new JLabel(dto.getPayDate() != null ? dto.getPayDate().toString() : "NA"));
                        add(new JLabel(dto.getRecommendation() != null ? Arrays.toString(dto.getRecommendation().toArray()) : "NA"));
                    }
                };

                add(labelPane);
                add(valuesPane);
            }
        }

        public ShareDTO getDTO() {
            return dto;
        }
    }
}
