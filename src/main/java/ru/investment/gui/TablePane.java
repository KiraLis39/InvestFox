package ru.investment.gui;

import lombok.extern.slf4j.Slf4j;
import ru.investment.components.ShareTableRow;
import ru.investment.components.TextTableRow;
import ru.investment.components.other.FOptionPane;
import ru.investment.core.NetProcessor;
import ru.investment.dto.ResultShareDTO;
import ru.investment.enums.CostType;
import ru.investment.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class TablePane extends JPanel {
    private static JPanel contentTablePane;
    private static JLabel lMoney, gMoney, sCount, shBye;
    private static JScrollPane scroll;
    private static int searchedIndex;
    private final NetProcessor netProc = new NetProcessor();
    private ArrayList<Component> searchResultList;


    public TablePane() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.DARK_GRAY);

        //                moveUpBtn = new JButton("Поднять") {
        //                    {
        //                        setBackground(Color.RED);
        //                        setForeground(Color.BLUE);
        //                        setFont(Registry.btnsFont2);
        //                        addActionListener(e -> getSelectedItem().moveSelectedUp());
        //                    }
        //                };
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
        // TODO: how weak ui
        //                add(moveUpBtn);
        //                add(removeBtn);
        //                add(moveDownBtn);
        JToolBar toolBar = new JToolBar("Можно тягать!") {
            {
                setOrientation(1);
                setBackground(Color.BLACK);
                setBorder(new EmptyBorder(0, 0, 0, 0));

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
                        setToolTipText("Добавить строку/акцию");
                        setForeground(Color.GREEN.darker());
                        setFont(Constant.btnsFont1);
                        addActionListener(e -> {
                            String tickerInput = JOptionPane.showInputDialog(TablePane.this, "Тикер:", "Ввод тикера:", JOptionPane.INFORMATION_MESSAGE);
                            if (tickerInput != null && tickerInput.length() > 0) {
                                for (Component row : getRows()) {
                                    if (((ShareTableRow) row).getResultDto().getTicker().equalsIgnoreCase(tickerInput)) {
                                        new FOptionPane("Отказ:", "Такое уже есть.", null, null);
                                        return;
                                    }
                                }

                                try {
                                    Future<ResultShareDTO> fut = netProc.checkTicket(tickerInput, false).exceptionally(throwable -> null);
                                    log.info("Scanning " + tickerInput + "...");
                                    while (!fut.isDone()) {
                                        Thread.yield();
                                    }
                                    if (fut.get() != null) {
                                        addShare(fut.get());
                                    } else {
                                        new FOptionPane("Провал!", "Не было найдено никакой информации.");
                                    }
                                } catch (ExecutionException | InterruptedException ex) {
                                    log.error("Exception here: {}", ex.getMessage());
                                } finally {
                                    scroll.revalidate();
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

                JButton searchBtn = new JButton("\uD83D\uDD0E") {
                    {
                        setForeground(Color.BLUE);
                        setFont(Constant.btnsFont1);
                        addActionListener(e -> showSearchDialog());
                    }
                };

                JButton saveBtn = new JButton("\uD83D\uDCBE") {
                    {
                        setToolTipText("Сохранить таблицу на диск");
                        setFont(Constant.btnsFont1);
                        addActionListener(e -> {
                            try {
                                NetProcessor.saveTable();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            new FOptionPane("Сохранено!", "Список сохранен.");
                        });
                    }
                };

                JButton downloadBtn = new JButton("⮟") {
                    {
                        setToolTipText("Загрузить данные из сети");
                        setForeground(Color.BLUE);
                        setFont(Constant.btnsFont1);
                        addActionListener(e -> {
                            ExecutorService es = Executors.newWorkStealingPool();
                            long was = System.currentTimeMillis();

                            for (Component row : getRows()) {
                                ShareTableRow nextRow = ((ShareTableRow) row);
                                log.info("TablePane: calculating " + nextRow.getResultDto().getTicker());
                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        ResultShareDTO data = netProc.checkTicket(nextRow.getResultDto().getTicker(), false)
                                                .handle((r, ex) -> {
                                                    if (r != null) {
                                                        return r;
                                                    } else {
                                                        log.warn("Problem: {}", ex.getMessage());
                                                        return null;
                                                    }
                                                }).get();
                                        if (data != null) {
                                            nextRow.updateColumns(data);
                                            log.info("TablePane: calculating " + nextRow.getResultDto().getTicker() + " done!");
                                        }
                                        return data;
                                    } catch (InterruptedException | ExecutionException ex) {
                                        log.error("Exception here: {}", ex.getMessage());
                                    }
                                    return null;
                                }, es);
                            }

                            try {
                                es.shutdown();
                                while (!es.awaitTermination(1, TimeUnit.SECONDS)) {
                                    // TODO: how weak ui
                                }
                            } catch (Exception e1) {
                                log.error("Exception here: {}", e1.getMessage());
                            } finally {
                                Long pass = System.currentTimeMillis() - was;
                                log.error(String.format(
                                        "%n=== UPDATE TIME PAST: %d min, %d sec ===%n",
                                        TimeUnit.MILLISECONDS.toMinutes(pass),
                                        TimeUnit.MILLISECONDS.toSeconds(pass) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pass))
                                ));
                                try {
                                    netProc.reload();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                };

                JButton updAllBtn = new JButton("\uD83D\uDD04") {
                    {
                        setToolTipText("Перерисовать стиль таблицы");
                        setForeground(Color.ORANGE);
                        setFont(Constant.btnsFont1);
                        addActionListener(e -> reloadTableStyle());
                    }
                };

//                add(moveUpBtn);
                add(addShareBtn);
                add(downloadBtn);
                add(updAllBtn);
                add(new JSeparator(1));
                add(saveBtn);
                add(new JSeparator(1));
                add(searchBtn);
//                add(removeBtn);
//                add(moveDownBtn);
            }
        };

        JPanel midPane = new JPanel(new BorderLayout()) {
            {
                JPanel upPane = new JPanel(new BorderLayout()) {
                    {
                        setOpaque(false);

                        add(new TextTableRow("<html>Индекс</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 0));
                        }}, BorderLayout.WEST);

                        add(new TextTableRow("<html>Сектор</html>", "<html>Эмитент</html>",
                                "<html>Тикер</html>", "<html>Цена</html>", "<html>Тип цены</html>", "<html>Лот</html>",
                                "<html>Рублей за лот</html>", "<html>Дивиденды (<font color=\"#0F0\" b>&#37;</font>)</html>",
                                "<html>Дивиденды (<font color=\"#FF0\" b>&#128181;</font>)</html>", "<html>Куплено шт.</html>", "<html>Стоимость</html>",
                                "<html>Прибыль/год</html>", "<html>Комментарий</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 16));
                        }}, BorderLayout.CENTER);

                        add(new TextTableRow("<html>P/E</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 36));
                        }}, BorderLayout.EAST);
                    }
                };

                contentTablePane = new JPanel(new GridLayout(230, 1, 0, 1)) {
                    {
                        setBackground(Color.DARK_GRAY.darker());
                    }
                };

                scroll = new JScrollPane(contentTablePane) {
                    {
                        setBorder(null);
                        getVerticalScrollBar().setUnitIncrement(24);
                    }
                };

                add(upPane, BorderLayout.NORTH);
                add(scroll, BorderLayout.CENTER);
            }
        };

        JPanel resultPane = new JPanel(new GridLayout(1, 15, 0, 0)) {
            {
//                setOpaque(false);
                setBackground(Color.DARK_GRAY.darker());
                setPreferredSize(new Dimension(0, 26));
                setBorder(new EmptyBorder(0, 0, 0, 0));

                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    sCount = new JLabel() {{
                        setForeground(Color.GRAY);
                        setHorizontalAlignment(0);
                        setFont(Constant.btnsFont6);
                    }};
                    add(sCount, BorderLayout.CENTER);
                }}); // 12
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
                    shBye = new JLabel() {{
                        setForeground(Color.ORANGE);
                        setHorizontalAlignment(0);
                        setFont(Constant.btnsFont6);
                    }};
                    add(shBye, BorderLayout.CENTER);
                }});
                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    lMoney = new JLabel() {{
                        setForeground(Color.RED);
                        setHorizontalAlignment(0);
                        setFont(Constant.btnsFont6);
                    }};
                    add(lMoney, BorderLayout.CENTER);
                }}); // 12

                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    gMoney = new JLabel() {{
                        setForeground(Color.GREEN);
                        setHorizontalAlignment(0);
                        setFont(Constant.btnsFont6);
                    }};
                    add(gMoney, BorderLayout.CENTER);
                }}); // 13
                add(new JSeparator(1));
                add(new JSeparator(1));
            }
        };

        add(toolBar, BorderLayout.EAST);
        add(midPane, BorderLayout.CENTER);
        add(resultPane, BorderLayout.SOUTH);
    }

    public static void clearRows() {
        for (Component component : contentTablePane.getComponents()) {
            if (component instanceof ShareTableRow) {
                contentTablePane.remove(component);
            }
        }
    }

    private void reloadTableStyle() {
        Arrays.stream(contentTablePane.getComponents()).filter(ShareTableRow.class::isInstance).forEach(c -> ((ShareTableRow) c).validateRowStyle());
    }

    public void addShares(List<ResultShareDTO> loading) {
        for (ResultShareDTO resultShareDTO : loading) {
            addShare(resultShareDTO);
        }
    }

    public void addShare(ResultShareDTO dto) {
        contentTablePane.add(new ShareTableRow(dto));
        updateResults();
    }

    public void updateResults() {
        lMoney.setText(String.format("%,.2f ", calcLostMoney()) + CostType.RUB.value());
        gMoney.setText(String.format("%,.2f ", calcGetMoney()) + CostType.RUB.value());
        sCount.setText("Count: " + getRows().size());
        shBye.setText(calcSharesBayedCount() + " шт.");
    }

    private int calcSharesBayedCount() {
        int count = 0;
        for (ShareTableRow row : getRows().toArray(new ShareTableRow[0])) {
            String tmp = ((JTextField) row.getColumnNamed("COUNT")).getText();
            if (!tmp.isEmpty()) {
                count += Integer.parseInt(tmp);
            }
        }
        return count;
    }

    private Double calcGetMoney() {
        double result = 0;
        for (Component row : getRows()) {
            double sharePay = ((ShareTableRow) row).getResultDto().getCost() / 100D * ((ShareTableRow) row).getResultDto().getDividend();
            result += sharePay * ((ShareTableRow) row).getResultDto().getCount() / 0.87D; // -13%
        }
        return result;
    }

    private Double calcLostMoney() {
        double result = 0;
        for (Component row : getRows()) {
            result += ((ShareTableRow) row).getResultDto().getCost() * ((ShareTableRow) row).getResultDto().getCount();
        }
        return result;
    }

    public List<Component> getRows() {
        return Arrays.stream(contentTablePane.getComponents()).filter(ShareTableRow.class::isInstance).toList();
    }

    public void showSearchDialog() {
        searchedIndex = 0;
        searchResultList = new ArrayList<>();
        String searchResult = JOptionPane.showInternalInputDialog(scroll,
                null, null, JOptionPane.QUESTION_MESSAGE);
        if (searchResult == null || searchResult.isEmpty()) {
            return;
        }
        for (Component shareTableRow : getRows()) {
            for (Component rowComp : ((JPanel) shareTableRow).getComponents()) {
                if (rowComp instanceof JPanel) {
                    for (Component comp : ((JPanel) rowComp).getComponents()) {
                        searchInto(comp, searchResult, searchResultList);
                    }
                    continue;
                }
                searchInto(rowComp, searchResult, searchResultList);
            }
        }
        if (searchResultList.size() > 0) {
            showNextSearched();
        }
    }

    void showNextSearched() {
        if (searchResultList.size() == 0) {
            return;
        }

        scroll.getViewport().setViewPosition(new Point(0, 0));
        searchResultList.get(searchedIndex).requestFocusInWindow();

        Rectangle sElem = new Rectangle(searchResultList.get(searchedIndex).getLocationOnScreen());
        Double pos = sElem.getCenterY() - scroll.getViewport().getViewRect().getCenterY();
        scroll.getViewport().setViewPosition(new Point(0, pos < 0 ? 0 : pos.intValue()));
        if (searchedIndex + 1 == searchResultList.size()) {
            searchedIndex = 0;
        } else {
            searchedIndex++;
        }
    }

    private void searchInto(Component comp, String searchResult, ArrayList<Component> resultList) {
        if (comp.getName().contains(searchResult)) {
            resultList.add(comp);
        }
        if (comp instanceof JTextArea) {
            if (((JTextArea) comp).getText() != null && ((JTextArea) comp).getText().contains(searchResult)) {
                resultList.add(comp);
            }
        }
        if (comp instanceof JTextField) {
            if (((JTextField) comp).getText() != null && ((JTextField) comp).getText().contains(searchResult)) {
                resultList.add(comp);
            }
        }
        if (comp instanceof JLabel) {
            if (((JLabel) comp).getText() != null && ((JLabel) comp).getText().contains(searchResult)) {
                resultList.add(comp);
            }
        }
    }
}
