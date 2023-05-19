package ru.investment.gui.components;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.investment.NetProcessor;
import ru.investment.ShareCollectedDTO;
import ru.investment.config.constants.Constant;
import ru.investment.enums.CostType;
import ru.investment.gui.InvestFrame;
import ru.investment.service.VaultService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;

import static ru.investment.enums.CostType.EUR;
import static ru.investment.enums.CostType.RUB;
import static ru.investment.enums.CostType.USD;

@Slf4j
@Data
@org.springframework.stereotype.Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class ShareTableRow extends JPanel implements Comparator<ShareTableRow> {
    private final InvestFrame investFrame;
    private transient NetProcessor netProc;
    private transient VaultService vaultService;
    private transient ShareCollectedDTO resultDto;
    private JPanel midPane, leftPane, rightPane;

    @Autowired
    public ShareTableRow(InvestFrame investFrame, ShareCollectedDTO resultDto) {
        this.investFrame = investFrame;
        this.resultDto = resultDto;

        if (resultDto == null) {
            throw new NullPointerException("ShareTableRow: resultDto is NULL!");
        }

        setPreferredSize(new Dimension(0, 35));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        leftPane = new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
                setBorder(null);
                setPreferredSize(new Dimension(45, 0));
            }
        };

        midPane = new JPanel(new GridLayout(1, 14, 1, 0)) {
            {
                setOpaque(false);
                setBorder(null);
//                setBackground(Color.RED);
            }
        };

        rightPane = new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(45, 0));
            }
        };

        add(leftPane, BorderLayout.WEST);
        add(midPane, BorderLayout.CENTER);
        add(rightPane, BorderLayout.EAST);

        loadColumns();
    }

    @Autowired
    public void setNetProc(NetProcessor netProc) {
        this.netProc = netProc;
    }

    @Autowired
    public void postInit() {
        investFrame.getTablePane().updateResults();
    }

    public void loadColumns() {
        double sharePay = resultDto.getCost() / 100D * resultDto.getDividend();
        int PE = (int) Math.round(resultDto.getCost() / sharePay);

        addSpinnerColumn("INDEX", resultDto.getIndex());

        addTextColumn("SECTOR",
                resultDto.getSectors().isEmpty() ? "=NA=" : resultDto.getSectors().toString()
                        .replace("[", "")
                        .replace("]", ""),
                resultDto.getSectors().isEmpty() ? "(not assigned)"
                        : "<html>".concat(resultDto.getSectors().stream().toList().toString()
                        .replace(",", "<br>")));

        addEditableColumn("NAME", resultDto.getShowedName(), "<html>" + resultDto.getName().replace(";", "<br>"));
        addEditableColumn("TICKER", resultDto.getTicker(), null, Color.WHITE, false);
        addTextColumn("COST", String.format("%,.2f", resultDto.getCost()), String.format("%,.5f", resultDto.getCost()));
        addTextColumn("COST_TYPE", resultDto.getCostType() == null ? "?" : resultDto.getCostType().value());
        addTextColumn("LOT_SIZE", resultDto.getLotSize() + "");

        double uniCost = resultDto.getLotCost();
        CostType costType = resultDto.getCostType();
        double lotCost = uniCost;
        double allCost = resultDto.getCost() * resultDto.getCount();
        double allSharePay = sharePay * resultDto.getCount();
        if (uniCost > 0 && !costType.equals(RUB)) {
            if (costType.value().equals(USD.value())) {
                lotCost = vaultService.getUSDValue() * uniCost;

                allCost = resultDto.getCost() * vaultService.getUSDValue() * resultDto.getCount();
                allSharePay = sharePay * vaultService.getUSDValue() * resultDto.getCount();
            } else if (costType.value().equals(EUR.value())) {
                lotCost = vaultService.getEURValue() * uniCost;

                allCost = resultDto.getCost() * vaultService.getEURValue() * resultDto.getCount();
                allSharePay = sharePay * vaultService.getUSDValue() * resultDto.getCount();
            } else {
                log.warn("\nFix it!");
            }
        }
        addTextColumn("LOT_COST", String.format("%,.2f", lotCost));

        addTextColumn("DIVIDEND", String.format("%.2f", resultDto.getDividend()), Color.GREEN);
        addTextColumn("SHARE_PAY", String.format("%.2f", sharePay), Color.YELLOW);
        addEditableColumn("COUNT", resultDto.getCount() > 0 ? resultDto.getCount() + "" : "");

        addTextColumn("ALL_COST", String.format("%,.2f", allCost), Color.RED);
        addTextColumn("ALL_PAY", String.format("%,.2f", allSharePay / 0.87D), Color.GREEN); // -13%

        addEditableColumn("COMMENT", resultDto.getComment(), resultDto.getComment());

        if (Double.valueOf(PE).isInfinite()) {
            addTextColumn("PE", "0", Color.RED);
        } else {
            addTextColumn("PE", String.valueOf(PE), Color.WHITE);
        }

        validateRowStyle();
    }

    public void updateColumns(ShareCollectedDTO updatedDto) {
        updatedDto.setIndex(resultDto.getIndex());
        updatedDto.setShowedName(resultDto.getShowedName());
        updatedDto.setCount(resultDto.getCount());
        this.resultDto = updatedDto;

        double sharePay = updatedDto.getCost() / 100D * updatedDto.getDividend();
        double restoreValue = updatedDto.getCost() / sharePay;

        JLabel cost = (JLabel) getColumnNamed("COST");
        cost.setText(String.format("%.2f", updatedDto.getCost()));

        JLabel lotSize = (JLabel) getColumnNamed("LOT_SIZE");
        lotSize.setText(updatedDto.getLotSize() + "");

        JLabel lotCost = (JLabel) getColumnNamed("LOT_COST");
        lotCost.setText(String.format("%.2f", updatedDto.getLotCost()));

        JLabel div = (JLabel) getColumnNamed("DIVIDEND");
        div.setText(String.format("%.2f", updatedDto.getDividend()));

        JLabel sPay = (JLabel) getColumnNamed("SHARE_PAY");
        sPay.setText(String.format("%.2f", sharePay));

        JTextField countField = (JTextField) getColumnNamed("COUNT");
        int count = Integer.parseInt(countField.getText().isBlank() ? "0" : countField.getText());
        JLabel allCost = (JLabel) getColumnNamed("ALL_COST");
        allCost.setText(String.format("%.2f", (updatedDto.getCost() * count)));

        double allSharePay = sharePay * count / 0.87D; // -13%
        JLabel allPay = (JLabel) getColumnNamed("ALL_PAY");
        allPay.setText(String.format("%.2f", allSharePay));

        validateRowStyle();
    }

    private void addSpinnerColumn(String name, short index) {
        leftPane.add(new JSpinner(new SpinnerNumberModel(index, -2, 6, 1)) {
            {
                setName(name);
                if (Constant.showTableCellsBorder) {
                    setBorder(BorderFactory.createCompoundBorder(
                            new EmptyBorder(3, 0, 3, 0),
                            BorderFactory.createLineBorder(Color.GREEN)
                    ));
                } else {
                    setBorder(new EmptyBorder(3, 0, 3, 0));
                }
                setFont(Constant.fontTableDataText);
//                getEditor().getComponent(0).setForeground(Color.WHITE);
                getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.BLACK);
                ((DefaultEditor) getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
                addChangeListener(e ->
                        {
                            getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.GRAY);
                            resultDto.setIndex(Short.parseShort(getValue().toString()));
                            validateRowStyle();
                        }
                );
            }
        });
    }

    private void addEditableColumn(String name, String text) {
        addEditableColumn(name, text, null, null, true);
    }

    private void addEditableColumn(String name, String text, String tooltip) {
        addEditableColumn(name, text, tooltip, null, true);
    }

    private void addEditableColumn(String name, String text, String tooltip, Color color) {
        addEditableColumn(name, text, tooltip, color, true);
    }

    private void addEditableColumn(String name, String text, String tooltip, Color color, boolean editable) {
        midPane.add(new JTextField(text) {
            {
                setName(name);
                if (Constant.showTableCellsBorder) {
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.GREEN),
                            new EmptyBorder(3, 3, 0, 3)
                    ));
                } else {
                    setBorder(new EmptyBorder(3, 3, 0, 3));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(Constant.fontTableDataText);
                setBackground(Color.DARK_GRAY.darker());
                setForeground(color == null ? Color.WHITE : color);

                if (tooltip != null) {
                    setToolTipText(tooltip);
                }
                if (!editable) {
                    setEditable(false);
                }

                if (getName().equals("COUNT")) {
                    addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            resultDto.setCount(getText().isBlank() ? Integer.parseInt("0") : Integer.parseInt(getText()));
                            JLabel allCost = (JLabel) getColumnNamed("ALL_COST");
                            allCost.setText(String.format("%.2f", resultDto.getCost() * resultDto.getCount()));

                            JLabel allPay = (JLabel) getColumnNamed("ALL_PAY");
                            allPay.setText(String.format("%.2f", (resultDto.getCost() / 100D * resultDto.getDividend()) * resultDto.getCount() / 0.87D)); // -13%

                            if (resultDto.getCount() > 0 && (int) ((JSpinner) getColumnNamed("INDEX")).getValue() > 0) {
                                ((JSpinner) getColumnNamed("INDEX")).setValue(0);
                            }
                        }
                    });
                }

                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getName().equals("COMMENT")) {
                            setToolTipText(getText());
                            resultDto.setComment(getText());
                        }

                        if (getName().equals("NAME")) {
                            resultDto.setShowedName(getText());
                        }

                        if (getName().equals("INDEX")) {
                            resultDto.setIndex(Short.parseShort(getText()));
                        }
                    }
                });

                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            transferFocus();
                        }
                    }
                });
            }
        });
    }

    public Component getColumnNamed(String name) {
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel pane) {
                for (Component comp2 : pane.getComponents()) {
                    if (comp2.getName().equals(name)) {
                        return comp2;
                    }
                }
                continue;
            }
            if (comp.getName().equals(name)) {
                return comp;
            }
        }
        return null;
    }

    private void addTextColumn(String name, String text) {
        addTextColumn(name, text, null, null);
    }

    private void addTextColumn(String name, String text, String tooltip) {
        addTextColumn(name, text, tooltip, null);
    }

    private void addTextColumn(String name, String text, Color defaultColor) {
        addTextColumn(name, text, null, defaultColor);
    }

    private void addTextColumn(String name, String text, String tooltip, Color foreground) {
        JComponent pane = name.equals("PE") ? rightPane : midPane;
        pane.add(new JLabel(text) {{
            setName(name);
            setForeground(foreground == null ? Color.WHITE : foreground);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(Constant.fontTableDataText);
            if (Constant.showTableCellsBorder) {
                setBorder(BorderFactory.createLineBorder(Color.GREEN));
            }
            if (tooltip != null) {
                setToolTipText(tooltip);
            }
        }});
    }

    @Override
    public int compare(ShareTableRow o1, ShareTableRow o2) {
        JSpinner tmp = ((JSpinner) o1.getColumnNamed("INDEX"));
        short index1 = (short) tmp.getValue();

        tmp = ((JSpinner) o2.getColumnNamed("INDEX"));
        short index2 = (short) tmp.getValue();

        return Short.compare(index1, index2);
    }

    public void validateRowStyle() {
        boolean peIsInfinite = Double.valueOf(resultDto.getCost() / (resultDto.getCost() / 100D * resultDto.getDividend())).isInfinite();
        ((JLabel) getColumnNamed("PE"))
                .setText(peIsInfinite ? "0"
                        : String.valueOf(Math.round(resultDto.getCost() / (resultDto.getCost() / 100D * resultDto.getDividend())))
                );

        JLabel peLabel = (JLabel) getColumnNamed("PE");
        double deLabel = Double.parseDouble(peLabel.getText().replace(",", "."));
        if (peIsInfinite) {
            peLabel.setForeground(Color.RED);
        } else if (deLabel < 5) {
            peLabel.setForeground(new Color(0.0f, 1.0f, 0.0f));
        } else if (deLabel < 10) {
            peLabel.setForeground(new Color(0.0f, 1.0f, 0.5f));
        } else if (deLabel < 15) {
            peLabel.setForeground(new Color(0.0f, 0.5f, 1.0f));
        } else if (deLabel < 30) {
            peLabel.setForeground(new Color(0.5f, 0.0f, 1.0f));
        } else {
            peLabel.setForeground(new Color(1.0f, 0.0f, 0.5f));
        }

        //
        Component name = getColumnNamed("NAME");
        double cost = resultDto.getCost();
        int count = resultDto.getCount();

        if (cost > 30_000) {
            name.setBackground(Color.RED);
            name.setForeground(Color.BLACK);
        } else if (cost > 15_000) {
            name.setBackground(Color.RED.darker().darker());
            name.setForeground(Color.GRAY);
        } else if (cost > 10_000) {
            name.setBackground(Color.RED.darker());
            name.setForeground(Color.WHITE);
        } else if (count == 0) {
            name.setBackground(Color.MAGENTA.darker().darker().darker());
            name.setForeground(Color.WHITE);
        }

        switch ((int) ((JSpinner) getColumnNamed("INDEX")).getValue()) {
            case 6 -> {
                name.setBackground(Color.BLACK);
                name.setForeground(Color.DARK_GRAY);
            }
            case 5 -> {
                name.setBackground(Color.BLACK);
                name.setForeground(Color.RED.darker());
            }
            case 4 -> {
                name.setBackground(Color.RED.darker().darker());
                name.setForeground(Color.GRAY);
            }
            case 3 -> {
                name.setBackground(Color.RED.darker());
                name.setForeground(Color.WHITE);
            }
            case 2 -> {
                name.setBackground(Color.GREEN);
                name.setForeground(Color.BLACK);
            }
            case 1 -> {
                name.setBackground(Color.MAGENTA.darker().darker().darker());
                name.setForeground(Color.YELLOW);
            }
            case 0 -> {
                name.setBackground(Color.DARK_GRAY.darker());
                name.setForeground(Color.WHITE);
            }
            case -2 -> name.setForeground(Color.ORANGE);
        }
        getColumnNamed("TICKER").setBackground(name.getBackground());
        getColumnNamed("TICKER").setForeground(name.getForeground());

//        getColumnNamed("INDEX").setBackground((int) ((JSpinner) getColumnNamed("INDEX")).getValue() == 2 ? Color.GREEN : Color.WHITE);
//        getColumnNamed("INDEX").setForeground((int) ((JSpinner) getColumnNamed("INDEX")).getValue() == 2 ? Color.BLACK : Color.WHITE);

        //
        getColumnNamed("COUNT").setForeground(Color.ORANGE);
        if (((JTextField) getColumnNamed("COUNT")).getText().equals("0")) {
            ((JTextField) getColumnNamed("COUNT")).setText("");
        }

        //
        getColumnNamed("COMMENT").setFont(Constant.fontTableComment);
        getColumnNamed("COMMENT").setForeground(Color.ORANGE);
        ((JTextField) getColumnNamed("COMMENT")).setToolTipText(((JTextField) getColumnNamed("COMMENT")).getText());

        //
        getColumnNamed("LOT_COST").setForeground(Color.CYAN);

        //
        getColumnNamed("COST").setForeground(Color.YELLOW);
        getColumnNamed("COST").setFont(Constant.fontTableSum);
        ((JLabel) getColumnNamed("COST")).setHorizontalAlignment(SwingConstants.RIGHT);

        //
        JLabel ct = ((JLabel) getColumnNamed("COST_TYPE"));
        ct.setForeground(Color.WHITE);
        if (ct.getText().equalsIgnoreCase(RUB.value())) {
            ct.setForeground(Color.YELLOW.darker());
        } else if (ct.getText().equalsIgnoreCase(USD.value())) {
            ct.setForeground(Color.GREEN.darker());
        } else if (ct.getText().equalsIgnoreCase(EUR.value())) {
            ct.setForeground(Color.CYAN.darker());
        }
//        ct.setBorder(new EmptyBorder(0, 3, 0, 0));
        ct.setHorizontalAlignment(SwingConstants.LEFT);

        //
        getColumnNamed("LOT_SIZE").setForeground(new Color(0.65f, 0.2f, 1.0f));
        getColumnNamed("LOT_SIZE").setFont(Constant.fontTableSum);
    }
}
