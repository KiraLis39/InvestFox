package gui;

import components.FOptionPane;
import components.ShareTableRow;
import components.TextTableRow;
import core.NetProcessor;
import dto.ResultShareDTO;
import registry.CostType;
import registry.Registry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TablePane extends JPanel {
    private NetProcessor netProc = new NetProcessor();
    private static JPanel contentTablePane;
    private static JToolBar toolBar;
    private static JLabel lMoney, gMoney;

    public TablePane() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.DARK_GRAY);

        toolBar = new JToolBar("Можно тягать!") {
            {
                setBorder(new EmptyBorder(0, 0, 1, 0));

//                moveUpBtn = new JButton("Поднять") {
//                    {
//                        setBackground(Color.RED);
//                        setForeground(Color.BLUE);
//                        setFont(Registry.btnsFont2);
//                        addActionListener(e -> getSelectedItem().moveSelectedUp());
//                    }
//                };

                JButton addShareBtn = new JButton("➕") {
                    {
                        setForeground(Color.GREEN.darker());
                        setFont(Registry.btnsFont1);
                        addActionListener(e -> {
                            String tickerInput = JOptionPane.showInputDialog(TablePane.this, "Тикер:", "Ввод тикера:", JOptionPane.INFORMATION_MESSAGE);
                            if (tickerInput != null && tickerInput.length() > 0) {
                                for (Component row : getRows()) {
                                    if (((ShareTableRow) row).getResultDto().getTICKER().equalsIgnoreCase(tickerInput)) {
                                        new FOptionPane("Отказ:", "Такое уже есть.", null, null, true);
                                        return;
                                    }
                                }

                                try {
                                    Future<ResultShareDTO> fut = netProc.checkTicket(tickerInput);
                                    System.out.println("Scanning " + tickerInput + "...");
                                    while (!fut.isDone()) {
                                        try {
                                            Thread.sleep(300);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }
                                    }
                                    addShare(fut.get());
                                } catch (ExecutionException executionException) {
                                    executionException.printStackTrace();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        });
                    }
                };

//                removeShareBtn = new JButton("- трек") {
//                    {
//                        setForeground(Color.RED);
//                        setFont(Registry.btnsFont2);
//                        addActionListener(e -> removeRow());
//                    }
//                };

//                moveDownBtn = new JButton("Опустить") {
//                    {
//                        setForeground(Color.BLUE);
//                        setFont(Registry.btnsFont2);
//                        addActionListener(e -> getSelectedItem().moveSelectedDown());
//                    }
//                };

                JButton saveBtn = new JButton("\uD83D\uDCBE") {
                    {
                        setFont(Registry.btnsFont1);
                        addActionListener(e -> {
                            try {
                                NetProcessor.save();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            new FOptionPane("Сохранено!", "Список сохранен.");
                        });
                    }
                };

                JButton updAllBtn = new JButton("\uD83D\uDD04") {
                    {
                        setForeground(Color.ORANGE);
                        setFont(Registry.btnsFont1);
                        addActionListener(e -> {

                            ExecutorService es = Executors.newFixedThreadPool(4);
                            long was = System.currentTimeMillis();

                            for (Component row : getRows()) {
                                es.execute(() -> {
                                    try {
                                        ShareTableRow nextRow = ((ShareTableRow) row);
                                        Future<ResultShareDTO> fut = netProc.checkTicket(nextRow.getResultDto().getTICKER());
                                        System.out.println("TablePane: calculating " + nextRow.getResultDto().getTICKER());
                                        while (!fut.isDone()) {
                                            Thread.sleep(500);
                                        }
                                        System.out.println("TablePane: calculating " + nextRow.getResultDto().getTICKER() + " done!");
                                        ResultShareDTO data = fut.get(10, TimeUnit.SECONDS);
                                        if (data != null) {
                                            nextRow.updateColumns(data);
                                        }
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                });
                            }
                            es.shutdown();

                            System.out.println("UPDATE TIME PAST: " + (System.currentTimeMillis() - was) + " ms.\n");
                        });
                    }
                };

//                add(moveUpBtn);
                add(addShareBtn);
                add(updAllBtn);
                add(new JSeparator(1));
                add(saveBtn);
//                add(removeBtn);
//                add(moveDownBtn);
            }
        };

        contentTablePane = new JPanel(new GridLayout(300, 1, 0, 3)) {
            {
                setBackground(Color.GRAY);

                add(new TextTableRow("<html>Индекс</html>", "<html>Сектор</html>", "<html>Эмитент</html>",
                        "<html>Тикер</html>", "<html>Цена</html>", "<html>Тип цены</html>", "<html>Лот</html>",
                        "<html>Цена за лот</html>", "<html>Дивиденды (<font color=\"#0F0\" b>&#37;</font>)</html>",
                        "<html>Дивиденды (<font color=\"#FF0\" b>&#128181;</font>)</html>", "<html>Куплено шт.</html>", "<html>Стоимость</html>",
                        "<html>Прибыль/год</html>", "<html>Комментарий</html>", "<html>Р/Е</html>"
                ));
            }
        };

        JScrollPane scroll = new JScrollPane(contentTablePane) {
            {
                getVerticalScrollBar().setUnitIncrement(24);
            }
        };

        JPanel resultPane = new JPanel(new GridLayout(1, 15, 0, 0)) {
            {
                setBackground(Color.DARK_GRAY.darker());
                setPreferredSize(new Dimension(0, 27));
                setBorder(new EmptyBorder(0, -6, 0, 6));

                add(new JSeparator(1));
                add(new JSeparator(1));
                add(new JSeparator(1));

                add(new JSeparator(1));
                add(new JSeparator(1));
                add(new JSeparator(1));

                add(new JSeparator(1));
                add(new JSeparator(1));
                add(new JSeparator(1));

                add(new JSeparator(1));
                add(new JSeparator(1));
                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    lMoney = new JLabel() {{
                        setForeground(Color.RED);
                        setHorizontalAlignment(0);
                        setFont(Registry.btnsFont6);
                    }};
                    add(lMoney, BorderLayout.CENTER);
                }}); // 12

                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    gMoney = new JLabel() {{
                        setForeground(Color.GREEN);
                        setHorizontalAlignment(0);
                        setFont(Registry.btnsFont6);
                    }};
                    add(gMoney, BorderLayout.CENTER);
                }}); // 13
                add(new JSeparator(1));
                add(new JSeparator(1));
            }
        };

        add(toolBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(resultPane, BorderLayout.SOUTH);
    }

    public void addShares(ArrayList<ResultShareDTO> loading) {
        for (ResultShareDTO resultShareDTO : loading) {
            addShare(resultShareDTO);
        }
    }

    public void addShare(ResultShareDTO dto) {
        contentTablePane.add(new ShareTableRow(dto));
        updateResults();
    }

    public void updateResults() {
        lMoney.setText(String.format("%.2f ", calcLostMoney()) + CostType.RUB.value());
        gMoney.setText(String.format("%.2f ", calcGetMoney()) + CostType.RUB.value());
    }

    private Double calcGetMoney() {
        double result = 0;
        for (Component row : getRows()) {
            double sharePay = ((ShareTableRow) row).getResultDto().getCOST() / 100D * ((ShareTableRow) row).getResultDto().getDIVIDEND();
            result += sharePay * ((ShareTableRow) row).getResultDto().getCOUNT() / 0.87D; // -13%
        }
        return result;
    }

    private Double calcLostMoney() {
        double result = 0;
        for (Component row : getRows()) {
            result += ((ShareTableRow) row).getResultDto().getCOST() * ((ShareTableRow) row).getResultDto().getCOUNT();
        }
        return result;
    }

    public List<Component> getRows() {
        Component[] arr = contentTablePane.getComponents();
        return Arrays.stream(arr).filter(c -> c instanceof ShareTableRow).collect(Collectors.toList());
    }

    public static void clearRows() {
        for (Component component : contentTablePane.getComponents()) {
            if (component instanceof ShareTableRow) {
                contentTablePane.remove(component);
            }
        }
    }
}
