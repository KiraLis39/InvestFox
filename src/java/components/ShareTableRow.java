package components;

import dto.ResultShareDTO;
import gui.InvestFrame;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import registry.CostType;
import registry.Registry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;

import static registry.CostType.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShareTableRow extends JPanel implements Comparator<ShareTableRow> {
    private ResultShareDTO resultDto;
    private JPanel midPane, leftPane, rightPane;

    public ShareTableRow(ResultShareDTO resultDto) {
        this.resultDto = resultDto;

        if (resultDto == null) {
            throw new RuntimeException("ShareTableRow: resultDto is NULL!");
        }

        setPreferredSize(new Dimension(0, 35));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        leftPane = new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
            }
        };

        midPane = new JPanel(new GridLayout(1, 14, 1, 0)) {
            {
                setOpaque(false);
            }
        };

        rightPane = new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
            }
        };

        add(leftPane, BorderLayout.WEST);
        add(midPane, BorderLayout.CENTER);
        add(rightPane, BorderLayout.EAST);

        loadColumns();
    }

    public void loadColumns() {
        double sharePay = resultDto.getCOST() / 100D * (resultDto.getDIVIDEND() == null ? 0 : resultDto.getDIVIDEND());
        double allSharePay = sharePay * resultDto.getCOUNT() / 0.87D; // -13%
        double allCost = resultDto.getCOST() * resultDto.getCOUNT();
        double PE = resultDto.getCOST() / sharePay;

        addSpinnerColumn("INDEX", resultDto.getINDEX());
        addTextColumn("SECTOR", resultDto.getSECTOR());
        addEditableColumn("NAME", resultDto.getSHOWED_NAME(), "<html>" + resultDto.getNAME().replace(" :: ", "<br>"));
        addEditableColumn("TICKER", resultDto.getTICKER(), null, Color.WHITE, false);
        addTextColumn("COST", String.format("%.2f", resultDto.getCOST()));
        addTextColumn("COST_TYPE", resultDto.getCOST_TYPE());
        addTextColumn("LOT_SIZE", resultDto.getLOT_SIZE() + "");
        addTextColumn("LOT_COST", String.format("%.2f", resultDto.getLOT_COST()));
        addTextColumn("DIVIDEND", String.format("%.2f", resultDto.getDIVIDEND()), Color.GREEN);
        addTextColumn("SHARE_PAY", String.format("%.2f", sharePay), Color.YELLOW);
        addEditableColumn("COUNT", resultDto.getCOUNT() > 0 ? resultDto.getCOUNT() + "" : "");
        addTextColumn("ALL_COST", String.format("%.2f", allCost), Color.RED);
        addTextColumn("ALL_PAY", String.format("%.2f", allSharePay), Color.GREEN);
        addEditableColumn("COMMENT", resultDto.getCOMMENT(), resultDto.getCOMMENT());

        if (Double.valueOf(PE).isInfinite()) {
            addTextColumn("PE", "0", Color.RED);
        } else {
            addTextColumn("PE", String.format("%.2f", PE), Color.WHITE);
        }

        validateRowStyle();
    }

    public void updateColumns(ResultShareDTO updatedDto) {
        updatedDto.setINDEX(resultDto.getINDEX());
        updatedDto.setSHOWED_NAME(resultDto.getSHOWED_NAME());
        updatedDto.setCOUNT(resultDto.getCOUNT());
        this.resultDto = updatedDto;

        double sharePay = updatedDto.getCOST() / 100D * updatedDto.getDIVIDEND();
        double PE = updatedDto.getCOST() / sharePay;

        JLabel сost = (JLabel) getColumnNamed("COST");
        сost.setText(String.format("%.2f", updatedDto.getCOST()));

        JLabel lotSize = (JLabel) getColumnNamed("LOT_SIZE");
        lotSize.setText(updatedDto.getLOT_SIZE() + "");

        JLabel lotCost = (JLabel) getColumnNamed("LOT_COST");
        lotCost.setText(String.format("%.2f", updatedDto.getLOT_COST()));

        JLabel div = (JLabel) getColumnNamed("DIVIDEND");
        div.setText(String.format("%.2f", updatedDto.getDIVIDEND()));

        JLabel sPay = (JLabel) getColumnNamed("SHARE_PAY");
        sPay.setText(String.format("%.2f", sharePay));

        JTextField countField = (JTextField) getColumnNamed("COUNT");
        int count = Integer.parseInt(countField.getText().isBlank() ? "0" : countField.getText());
        JLabel allCost = (JLabel) getColumnNamed("ALL_COST");
        allCost.setText(String.format("%.2f", (updatedDto.getCOST() * count)));

        double allSharePay = sharePay * count / 0.87D; // -13%
        JLabel allPay = (JLabel) getColumnNamed("ALL_PAY");
        allPay.setText(String.format("%.2f", allSharePay));

        validateRowStyle();
    }

    private void addSpinnerColumn(String name, short index) {
        leftPane.add(new JSpinner(new SpinnerNumberModel(index, -2, 6, 1)) {
            {
                setName(name);
                setBorder(null);
                setFont(Registry.btnsFont2);
//                getEditor().getComponent(0).setForeground(Color.WHITE);
                getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.BLACK);
                ((JSpinner.DefaultEditor) getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
                addChangeListener(e ->
                    {
                        getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.GRAY);
                        resultDto.setINDEX(Short.parseShort(getValue().toString()));
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
                setBorder(null);
                setHorizontalAlignment(JTextField.CENTER);
                setFont(Registry.btnsFont2);
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
                            resultDto.setCOUNT(getText().isBlank() ? Integer.parseInt("0") : Integer.parseInt(getText()));
                            JLabel allCost = (JLabel) getColumnNamed("ALL_COST");
                            allCost.setText(String.format("%.2f", resultDto.getCOST() * resultDto.getCOUNT()));

                            JLabel allPay = (JLabel) getColumnNamed("ALL_PAY");
                            allPay.setText(String.format("%.2f", (resultDto.getCOST() / 100D * resultDto.getDIVIDEND()) * resultDto.getCOUNT() / 0.87D)); // -13%

                            if (resultDto.getCOUNT() > 0 && (int) ((JSpinner) getColumnNamed("INDEX")).getValue() > 0) {
                                ((JSpinner) getColumnNamed("INDEX")).setValue(0);
                            }
                        }
                    });

                    InvestFrame.getTablePane().updateResults();
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
                            resultDto.setCOMMENT(getText());
                        }

                        if (getName().equals("NAME")) {
                            resultDto.setSHOWED_NAME(getText());
                        }

                        if (getName().equals("INDEX")) {
                            resultDto.setINDEX(Short.parseShort(getText()));
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

    private Component getColumnNamed(String name) {
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                for (Component comp2 : ((JPanel) comp).getComponents()) {
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
        JComponent c = midPane;
        if (name.equals("PE")) {
            c = rightPane;
            c.setPreferredSize(new Dimension(50, 0));
        }
        c.add(new JLabel(text) {{
            setName(name);
            setForeground(foreground == null ? Color.WHITE : foreground);
            setHorizontalAlignment(0);
            if (tooltip != null) {
                setToolTipText(tooltip);
            }
        }});
    }

    @Override
    public int compare(ShareTableRow o1, ShareTableRow o2) {
        short index1 = (short) ((JSpinner) o1.getColumnNamed("INDEX")).getValue();
        short index2 = (short) ((JSpinner) o2.getColumnNamed("INDEX")).getValue();
        return index1 > index2 ? 1 : index1 == index2 ? 0 : -1;
    }

    public void validateRowStyle() {
        boolean peIsInfinite = Double.valueOf(resultDto.getCOST() / (resultDto.getCOST() / 100D * resultDto.getDIVIDEND())).isInfinite();
        ((JLabel) getColumnNamed("PE"))
                .setText(peIsInfinite ? "0"
                                : String.format("%.2f", (resultDto.getCOST() / (resultDto.getCOST() / 100D * resultDto.getDIVIDEND())))
                );
        getColumnNamed("PE").setForeground(peIsInfinite ? Color.RED : Color.WHITE);

        //
        Component name = getColumnNamed("NAME");
        double cost = resultDto.getCOST();
        int count = resultDto.getCOUNT();

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
//            case -1 -> {}
//            case -2 -> {}
            default -> {}
        }
        getColumnNamed("TICKER").setBackground(name.getBackground());
        getColumnNamed("TICKER").setForeground(name.getForeground());

        //
        getColumnNamed("INDEX").setBackground((int) ((JSpinner) getColumnNamed("INDEX")).getValue() == 2 ? Color.GREEN : Color.WHITE);
        getColumnNamed("INDEX").setForeground((int) ((JSpinner) getColumnNamed("INDEX")).getValue() == 2 ? Color.BLACK : Color.WHITE);

        //
        getColumnNamed("COUNT").setForeground(Color.ORANGE);
        if (((JTextField) getColumnNamed("COUNT")).getText().equals("0")) {
            ((JTextField) getColumnNamed("COUNT")).setText("");
        }

        //
        getColumnNamed("COMMENT").setFont(Registry.btnsFont5);
        getColumnNamed("COMMENT").setForeground(Color.ORANGE);
        ((JTextField) getColumnNamed("COMMENT")).setToolTipText(((JTextField) getColumnNamed("COMMENT")).getText());

        //
        getColumnNamed("LOT_COST").setForeground(Color.CYAN);

        //
        getColumnNamed("COST").setForeground(Color.YELLOW);
        getColumnNamed("COST").setFont(Registry.btnsFont6);
        ((JLabel) getColumnNamed("COST")).setHorizontalAlignment(JLabel.RIGHT);

        //
        JLabel ct = ((JLabel)getColumnNamed("COST_TYPE"));
        ct.setForeground(Color.WHITE);
        if (ct.getText().equalsIgnoreCase(RUB.value())) {
            ct.setForeground(Color.YELLOW.darker());
        } else if (ct.getText().equalsIgnoreCase(USD.value())) {
            ct.setForeground(Color.GREEN.darker());
        } else if (ct.getText().equalsIgnoreCase(EUR.value())) {
            ct.setForeground(Color.CYAN.darker());
        }
        ct.setBorder(new EmptyBorder(0, 3, 0, 0));
        ct.setHorizontalAlignment(JLabel.LEFT);

        //
        getColumnNamed("LOT_SIZE").setForeground(new Color(0.65f, 0.2f, 1.0f));
        getColumnNamed("LOT_SIZE").setFont(Registry.btnsFont6);
    }
}
