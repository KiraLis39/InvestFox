package gui;

import components.FOptionPane;
import components.ShareTableRow;
import components.TextTableRow;
import core.NetProcessor;
import dto.ResultShareDTO;
import fox.Out;
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
    private static JLabel lMoney, gMoney, sCount, shBye;
    private static JScrollPane scroll;
    private static ArrayList<Component> searchResultList;
    private static int searchedIndex;


    public TablePane() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.DARK_GRAY);

        toolBar = new JToolBar("Можно тягать!") {
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
                                    Future<ResultShareDTO> fut = netProc.checkTicket(tickerInput, false).exceptionally(throwable -> null);
                                    System.out.println("Scanning " + tickerInput + "...");
                                    while (!fut.isDone()) {
                                        Thread.yield();
                                    }
                                    if (fut.get() != null) {
                                        addShare(fut.get());
                                    } else {
                                        new FOptionPane("Провал!", "Не было найдено никакой информации.");
                                    }
                                } catch (ExecutionException executionException) {
                                    executionException.printStackTrace();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
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
                        setFont(Registry.btnsFont1);
                        addActionListener(e -> showSearchDialog());
                    }
                };

                JButton saveBtn = new JButton("\uD83D\uDCBE") {
                    {
                        setToolTipText("Сохранить таблицу на диск");
                        setFont(Registry.btnsFont1);
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
                        setFont(Registry.btnsFont1);
                        addActionListener(e -> {
                            ExecutorService es = Executors.newWorkStealingPool();
                            long was = System.currentTimeMillis();

                            for (Component row : getRows()) {
                                ShareTableRow nextRow = ((ShareTableRow) row);
                                System.out.println("TablePane: calculating " + nextRow.getResultDto().getTICKER());
                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        ResultShareDTO data = netProc.checkTicket(nextRow.getResultDto().getTICKER(), false)
                                                .handle((r, ex) -> {
                                                    if (r != null) {return r;
                                                    } else {
                                                        Out.Print(TablePane.class, Out.LEVEL.WARN, "Problem: " + ex);
                                                        return null;
                                                    }
                                                }).get();
                                        if (data != null) {
                                            nextRow.updateColumns(data);
                                            System.out.println("TablePane: calculating " + nextRow.getResultDto().getTICKER() + " done!");
                                        }
                                        return data;
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    } catch (ExecutionException executionException) {
                                        executionException.printStackTrace();
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
                                e1.printStackTrace();
                            } finally {
                                Long pass = System.currentTimeMillis() - was;
                                System.err.println(String.format(
                                        "\n=== UPDATE TIME PAST: %d min, %d sec ===\n",
                                        TimeUnit.MILLISECONDS.toMinutes(pass),
                                        TimeUnit.MILLISECONDS.toSeconds(pass) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pass))
                                ));
                                try {
                                    NetProcessor.reload();
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
                        setFont(Registry.btnsFont1);
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
                        ) {{setBorder(new EmptyBorder(0, 0, 0 ,0));}}, BorderLayout.WEST);

                        add(new TextTableRow("<html>Сектор</html>", "<html>Эмитент</html>",
                                "<html>Тикер</html>", "<html>Цена</html>", "<html>Тип цены</html>", "<html>Лот</html>",
                                "<html>Рублей за лот</html>", "<html>Дивиденды (<font color=\"#0F0\" b>&#37;</font>)</html>",
                                "<html>Дивиденды (<font color=\"#FF0\" b>&#128181;</font>)</html>", "<html>Куплено шт.</html>", "<html>Стоимость</html>",
                                "<html>Прибыль/год</html>", "<html>Комментарий</html>"
                        ) {{setBorder(new EmptyBorder(0, 0, 0 ,16));}}, BorderLayout.CENTER);

                        add(new TextTableRow("<html>P/E</html>"
                        ) {{setBorder(new EmptyBorder(0, 0, 0 ,36));}}, BorderLayout.EAST);
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
                        setFont(Registry.btnsFont6);
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
                        setFont(Registry.btnsFont6);
                    }};
                    add(shBye, BorderLayout.CENTER);
                }});
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

        add(toolBar, BorderLayout.EAST);
        add(midPane, BorderLayout.CENTER);
        add(resultPane, BorderLayout.SOUTH);
    }

    private void reloadTableStyle() {
        Arrays.stream(contentTablePane.getComponents()).filter(c -> c instanceof ShareTableRow).forEach(c -> ((ShareTableRow)c).validateRowStyle());
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
        return Arrays.stream(contentTablePane.getComponents()).filter(c -> c instanceof ShareTableRow).collect(Collectors.toList());
    }

    public static void clearRows() {
        for (Component component : contentTablePane.getComponents()) {
            if (component instanceof ShareTableRow) {
                contentTablePane.remove(component);
            }
        }
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
