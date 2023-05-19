package ru.investment.gui;

import fox.components.FOptionPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import ru.investment.NetProcessor;
import ru.investment.ShareCollectedDTO;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.config.constants.Constant;
import ru.investment.enums.CostType;
import ru.investment.gui.components.ShareTableRow;
import ru.investment.gui.components.TextTableRow;
import ru.investment.service.ShareService;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@org.springframework.stereotype.Component
public class TablePane extends JPanel {
    private static ExecutorService es;
    private static JPanel contentTablePane;
    private static JLabel lMoney, gMoney, sCount, shBye;
    private static JScrollPane scroll;
    private static int searchedIndex;
    private final BrokersPane brokersPane;
    private final ShareService shareService;
    private NetProcessor netProcessor;
    private InvestFrame investFrame;
    private ArrayList<Component> searchResultList;

    public static void clearRows() {
        for (Component component : contentTablePane.getComponents()) {
            if (component instanceof ShareTableRow) {
                contentTablePane.remove(component);
            }
        }
    }

    @Autowired
    public void setInvestFrame(@Lazy InvestFrame investFrame) {
        this.investFrame = investFrame;
    }

    @Autowired
    public void setNetProcessor(@Lazy NetProcessor netProcessor) {
        this.netProcessor = netProcessor;
    }

    @PostConstruct
    public void init() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);

        JToolBar toolBar = new JToolBar("Можно тягать!") {
            {
                setOrientation(SwingConstants.VERTICAL);
                setBackground(Color.BLACK);
                setBorder(null);

                JButton addShareBtn = new JButton("➕") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Добавить строку/акцию");
                        setForeground(Color.GREEN.darker());
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            String tickerInput = JOptionPane.showInputDialog(TablePane.this, "Тикер:", "Ввод тикера:", JOptionPane.INFORMATION_MESSAGE);
                            if (tickerInput != null && tickerInput.length() > 0) {
                                for (ShareTableRow row : getRows()) {
                                    if (row.getResultDto().getTicker().equalsIgnoreCase(tickerInput)) {
                                        new FOptionPane().buildFOptionPane("Отказ:", "Такое уже есть.");
                                        return;
                                    }
                                }

                                try {
                                    Future<ShareCollectedDTO> fut = netProcessor
                                            .checkTicket(tickerInput, false).exceptionally(throwable -> null);
                                    log.info("Scanning " + tickerInput + "...");
                                    while (!fut.isDone()) {
                                        Thread.yield();
                                    }
                                    if (fut.get() != null) {
                                        addShare(fut.get());
                                    } else {
                                        new FOptionPane().buildFOptionPane("Провал!", "Не было найдено никакой информации.");
                                    }
                                } catch (Exception ex) {
                                    log.error("Exception here: {}", ex.getMessage());
                                } finally {
                                    scroll.revalidate();
                                }
                            }
                        });
                    }
                };

                JButton downloadBtn = new JButton("\uD83C\uDF0F") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Перепарсить данные из сети");
                        setForeground(Color.CYAN);
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            if (es == null) {
                                es = Executors.newWorkStealingPool();
                            }
                            long was = System.currentTimeMillis();

                            for (ShareTableRow nextRow : getRows()) {
                                log.info("TablePane: calculating " + nextRow.getResultDto().getTicker());

                                CompletableFuture.supplyAsync(() -> {
                                    try {
                                        ShareCollectedDTO data = netProcessor
                                                .checkTicket(nextRow.getResultDto().getTicker(), false)
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
                                        return null;
                                    } catch (Exception ex) {
                                        log.error("Exception here: {}", ex.getMessage());
                                        new FOptionPane().buildFOptionPane("Ошибка!",
                                                "Ошибка: " + (ex.getCause() == null ? ex.getMessage() : ex.getCause()));
                                    }
                                    return null;
                                }, es);
                            }

                            try {
                                es.shutdown();
                                while (!es.awaitTermination(1_500, TimeUnit.MILLISECONDS)) {
                                    log.info("Await into termination...");
                                    Thread.yield();
                                }
                            } catch (Exception e1) {
                                log.error("Exception here: {}", e1.getMessage());
                            } finally {
                                Long pass = System.currentTimeMillis() - was;
                                log.warn(String.format(
                                        "%n=== UPDATE TIME PAST: %d min, %d sec ===%n%n%n",
                                        TimeUnit.MILLISECONDS.toMinutes(pass),
                                        TimeUnit.MILLISECONDS.toSeconds(pass) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pass))
                                ));
                                try {
                                    shareService.reload();
                                } catch (IOException ex) {
                                    log.error("Exception here: {}", ex.getMessage());
                                }
                            }
                        });
                    }
                };

                JButton saveBtn = new JButton("⦽") {
                    {
                        setFocusPainted(false);
                        setForeground(Color.ORANGE);
                        setToolTipText("Сохранить таблицу в БД");
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            try {
                                shareService.saveTable(getRows());
                                new FOptionPane().buildFOptionPane("Сохранено!", "Список сохранен.");
                            } catch (Exception ex) {
                                log.error("Ошибка сохранения: {}", ex.getMessage());
                                new FOptionPane().buildFOptionPane("Ошибка!", "Ошибка сохранения: " + (ex.getCause() == null ? ex.getMessage() : ex.getCause()));
                            }
                        });
                    }
                };

                JButton loadBtn = new JButton("⧬") {
                    {
                        setFocusPainted(false);
                        setForeground(Color.BLUE);
                        setToolTipText("Загрузить таблицу из БД");
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            try {
                                shareService.loadTable();
                                new FOptionPane().buildFOptionPane("Готово!", "Список загружен из базы данных");
                            } catch (Exception ex) {
                                log.error("Ошибка загрузки данных: {}", ex.getMessage());
                                new FOptionPane().buildFOptionPane("Ошибка!", "Ошибка загрузки данных: " + (ex.getCause() == null ? ex.getMessage() : ex.getCause()));
                            }
                        });
                    }
                };

                JButton searchBtn = new JButton("\uD83D\uDD0E") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Поиск по тексту");
                        setForeground(Color.WHITE);
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> showSearchDialog());
                    }
                };

                JButton exportBtn = new JButton("\uD83D\uDCE4") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Экспорт данных в локальную папку");
                        setForeground(Color.ORANGE);
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            try {
                                exportTable();
                                brokersPane.exportBrokers();
                                new FOptionPane().buildFOptionPane("Готово!", "Список выгружен на диск");
                            } catch (Exception ex) {
                                log.error("Ошибка записи данных: {}", ex.getMessage());
                                new FOptionPane().buildFOptionPane("Ошибка!", "Ошибка записи данных: " + (ex.getCause() == null ? ex.getMessage() : ex.getCause()));
                            }
                        });
                    }
                };

                JButton importBtn = new JButton("\uD83D\uDCE5") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Импорт данных из локальной папки");
                        setForeground(Color.BLUE);
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> {
                            try {
                                importTable();
                                importBrokers();
                                new FOptionPane().buildFOptionPane("Готово!", "Список загружен из локальной директории");
                            } catch (Exception ex) {
                                log.error("Ошибка загрузки данных: {}", ex.getMessage());
                                new FOptionPane().buildFOptionPane("Ошибка!", "Ошибка загрузки данных: " + (ex.getCause() == null ? ex.getMessage() : ex.getCause()));
                            }
                        });
                    }
                };

                JButton repaintTable = new JButton("\uD83D\uDD04") {
                    {
                        setFocusPainted(false);
                        setToolTipText("Перерисовать стиль таблицы");
                        setForeground(Color.WHITE);
                        setFont(Constant.fontSidePanel);
                        addActionListener(e -> reloadTableStyle());
                    }
                };

                add(addShareBtn);
                add(downloadBtn);
                add(new JSeparator(SwingConstants.HORIZONTAL));
                add(saveBtn);
                add(loadBtn);
                add(new JSeparator(SwingConstants.HORIZONTAL));
                add(searchBtn);
                add(new JSeparator(SwingConstants.HORIZONTAL));
                add(exportBtn);
                add(importBtn);
                add(new JSeparator(SwingConstants.HORIZONTAL));
                add(repaintTable);
            }
        };

        JPanel midPane = new JPanel(new BorderLayout()) {
            {
                JPanel upPane = new JPanel(new BorderLayout()) {
                    {
                        setOpaque(false);

                        add(new TextTableRow(shareService, "<html>Индекс</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 0));
                        }}, BorderLayout.WEST);

                        add(new TextTableRow(shareService, "<html>Сектор</html>", "<html>Эмитент</html>",
                                "<html>Тикер</html>", "<html>Цена</html>", "<html>Тип цены</html>", "<html>Лот</html>",
                                "<html>Рублей за лот</html>", "<html>Дивиденды (<font color=\"#0F0\" b>&#37;</font>)</html>",
                                "<html>Дивиденды (<font color=\"#FF0\" b>&#128181;</font>)</html>", "<html>Куплено шт.</html>", "<html>Стоимость</html>",
                                "<html>Прибыль/год</html>", "<html>Комментарий</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 12));
                        }}, BorderLayout.CENTER);

                        add(new TextTableRow(shareService, "<html>P/E</html>"
                        ) {{
                            setBorder(new EmptyBorder(0, 0, 0, 30));
                        }}, BorderLayout.EAST);
                    }
                };

                contentTablePane = new JPanel(new GridLayout(198, 1, 0, 1)) {
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
                setBackground(Color.DARK_GRAY.darker());
                setPreferredSize(new Dimension(0, 26));
                setBorder(new EmptyBorder(0, 0, 0, 0));

                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    sCount = new JLabel() {{
                        setForeground(Color.GRAY);
                        setHorizontalAlignment(0);
                        setFont(Constant.fontTableSum);
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
                        setFont(Constant.fontTableSum);
                    }};
                    add(shBye, BorderLayout.CENTER);
                }});
                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    lMoney = new JLabel() {{
                        setForeground(Color.RED);
                        setHorizontalAlignment(0);
                        setFont(Constant.fontTableSum);
                    }};
                    add(lMoney, BorderLayout.CENTER);
                }}); // 12

                add(new JPanel(new BorderLayout(0, 0)) {{
                    setOpaque(false);

                    add(new JSeparator(1), BorderLayout.WEST);
                    gMoney = new JLabel() {{
                        setForeground(Color.GREEN);
                        setHorizontalAlignment(0);
                        setFont(Constant.fontTableSum);
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
        Arrays.stream(contentTablePane.getComponents()).filter(ShareTableRow.class::isInstance).forEach(c -> ((ShareTableRow) c).validateRowStyle());
    }

    public void addShares(List<ShareCollectedDTO> loading) {
        contentTablePane.removeAll();
        for (ShareCollectedDTO shareCollectedDTO : loading) {
            addShare(shareCollectedDTO);
        }
    }

    public void addShare(ShareCollectedDTO dto) {
        contentTablePane.add(new ShareTableRow(investFrame, dto));
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
        for (ShareTableRow row : getRows()) {
            double sharePay = row.getResultDto().getCost() / 100D * row.getResultDto().getDividend();
            result += sharePay * row.getResultDto().getCount() / 0.87D; // -13%
        }
        return result;
    }

    private Double calcLostMoney() {
        double result = 0;
        for (ShareTableRow row : getRows()) {
            result += row.getResultDto().getCost() * row.getResultDto().getCount();
        }
        return result;
    }

    public List<ShareTableRow> getRows() {
        Stream<Component> rowStream = Arrays.stream(contentTablePane.getComponents()).filter(ShareTableRow.class::isInstance);
        return rowStream.map(el -> (ShareTableRow) el).toList();
    }

    public void showSearchDialog() {
        searchedIndex = 0;
        searchResultList = new ArrayList<>();
        String searchResult = JOptionPane.showInternalInputDialog(scroll,
                null, null, JOptionPane.QUESTION_MESSAGE);
        if (searchResult == null || searchResult.isEmpty()) {
            return;
        }
        for (ShareTableRow row : getRows()) {
            for (Component rowComp : row.getComponents()) {
                if (rowComp instanceof JPanel pane) {
                    for (Component comp : pane.getComponents()) {
                        searchInto(comp, searchResult, searchResultList);
                    }
                    continue;
                }
                searchInto(rowComp, searchResult, searchResultList);
            }
        }
        if (!searchResultList.isEmpty()) {
            showNextSearched();
        }
    }

    void showNextSearched() {
        if (searchResultList.isEmpty()) {
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
        if (comp instanceof JTextArea area) {
            if (area.getText() != null && area.getText().contains(searchResult)) {
                resultList.add(area);
            }
        }
        if (comp instanceof JTextField area) {
            if (area.getText() != null && area.getText().contains(searchResult)) {
                resultList.add(area);
            }
        }
        if (comp instanceof JLabel label) {
            if (label.getText() != null && label.getText().contains(searchResult)) {
                resultList.add(label);
            }
        }
    }

    public int exportTable() throws IOException {
        if (!new File(Constant.SHARES_DIR).exists()) {
            Files.createDirectory(Paths.get(Constant.SHARES_DIR));
        }

        int fails = 0;
        log.info("Saving shares..");
        for (ShareTableRow row : getRows()) {
            try {
                ObjectMapperConfig.getMapper().writeValue(
                        new File(Constant.SHARES_DIR + row.getResultDto().getTicker() + Constant.BROKER_SAVE_POSTFIX),
                        row.getResultDto());
            } catch (IOException e) {
                fails++;
                e.printStackTrace();
            }
        }

        if (fails > 0) {
            log.error("Не удалось сохранить акций: {}", fails);
        }
        return fails;
    }

    public void importTable() throws IOException {
        if (!new File(Constant.SHARES_DIR).exists()) {
            throw new NotFoundException("Требуемая директория не найдена");
        }

        File[] shares = Paths.get(Constant.SHARES_DIR).toFile().listFiles();
        ArrayList<ShareCollectedDTO> loading = new ArrayList<>();
        assert shares != null;
        for (File share : shares) {
            loading.add(ObjectMapperConfig.getMapper().readValue(share, ShareCollectedDTO.class));
        }
        Collections.sort(loading);
        addShares(loading);
    }

    public void importBrokers() {
        brokersPane.importBrokers();
    }
}
