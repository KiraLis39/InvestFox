package components;

import dto.ResultShareDTO;
import gui.InvestFrame;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import registry.Registry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Comparator;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShareTableRow extends JPanel implements Comparator<ShareTableRow> {
    private ResultShareDTO resultDto;

    public ShareTableRow(ResultShareDTO resultDto) {
        this.resultDto = resultDto;

        setPreferredSize(new Dimension(0, 35));
        setLayout(new GridLayout(1, 15, 0, 0));
        setBackground(Color.darkGray);

        loadColumns();
    }

    public void loadColumns() {
        double sharePay = resultDto.getCOST() / 100D * resultDto.getDIVIDEND();
        double allSharePay = sharePay * resultDto.getCOUNT() / 0.87D; // -13%
        double allCost = resultDto.getCOST() * resultDto.getCOUNT();
        double PE = resultDto.getCOST() / sharePay;

        addSpinnerColumn("INDEX", resultDto.getINDEX());
        addTextColumn("SECTOR", resultDto.getSECTOR());
        addEditableColumn("NAME", resultDto.getSHOWED_NAME(), "<html>" + resultDto.getNAME().replace(" :: ", "<br>"));
        addEditableColumn("TICKER", resultDto.getTICKER(), null, Color.GRAY, false);
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

        JLabel pe = (JLabel) getColumnNamed("PE");
        if (Double.valueOf(PE).isInfinite()) {
            pe.setText("0");
            pe.setForeground(Color.RED);
        } else {
            pe.setText(String.format("%.2f", PE));
            pe.setForeground(Color.WHITE);
        }
    }

    private void addSpinnerColumn(String name, short index) {
        this.add(new JSpinner(new SpinnerNumberModel(index, -2, 6, 1)) {
            {
                setName(name);
                setBorder(null);
                setFont(Registry.btnsFont2);
                getEditor().getComponent(0).setForeground(Color.WHITE);
                getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.GRAY);
                ((JSpinner.DefaultEditor) getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
                addChangeListener(e ->
                        {
                            getEditor().getComponent(0).setBackground(((int) getValue() * 1f) % 2f == 0 ? Color.DARK_GRAY : Color.GRAY);
                            resultDto.setINDEX(Short.valueOf(getValue().toString()));
                            Component name = getColumnNamed("NAME");
                            Component ticker = getColumnNamed("TICKER");
                            name.setBackground(resultDto.getINDEX() == 2 ? Color.GREEN :
                                    (resultDto.getINDEX() == 1 ? Color.MAGENTA.darker() :
                                            (resultDto.getINDEX() == 3 ? Color.RED :
                                                    (resultDto.getINDEX() == 4 ? Color.RED.darker() : Color.DARK_GRAY)
                                            )
                                    )
                            );
                            ticker.setBackground(name.getBackground());
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
        this.add(new JTextField(text) {
            {
                setName(name);
                setBorder(null);
                setHorizontalAlignment(JTextField.CENTER);
                setFont(Registry.btnsFont2);
                setBackground(Color.DARK_GRAY);
                setForeground(color == null ? Color.YELLOW : color);

                if (getName().equals("COMMENT")) {
                    setFont(Registry.btnsFont5);
                    setToolTipText(getText());
                }

                if (getName().equals("NAME")) {
                    if (resultDto.getCOUNT() > 0) {
                        setBackground(Color.DARK_GRAY);
                    } else {
                        if (resultDto.getCOST() > 15_000) {
                            setBackground(Color.RED.darker().darker());
                        } else if (resultDto.getCOST() > 10_000) {
                            setBackground(Color.RED.darker());
                        } else {
                            setBackground(Color.MAGENTA.darker().darker());
                        }
                    }

                    Component cjs = getColumnNamed("INDEX");
                    if ((int) ((JSpinner) cjs).getValue() == 2) {
                        setBackground(Color.GREEN);
                        setForeground(Color.BLACK);
                    }
                }

                if (tooltip != null) {
                    setToolTipText(tooltip);
                }
                if (!editable) {
                    setEditable(false);
                }

                if (getName().equals("COUNT")) {
                    InvestFrame.getTablePane().updateResults();
                }

                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        selectAll();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getName().equals("COUNT")) {
                            resultDto.setCOUNT(getText().isBlank() ? Integer.parseInt("0") : Integer.parseInt(getText()));
                            JLabel allCost = (JLabel) getColumnNamed("ALL_COST");
                            allCost.setText(String.format("%.2f", resultDto.getCOST() * resultDto.getCOUNT()));

                            JLabel allPay = (JLabel) getColumnNamed("ALL_PAY");
                            allPay.setText(String.format("%.2f", (resultDto.getCOST() / 100D * resultDto.getDIVIDEND()) * resultDto.getCOUNT() / 0.87D)); // -13%

                            if (getText().equals("0")) {
                                setText("");
                            }
                        }

                        if (getName().equals("COMMENT")) {
                            setToolTipText(getText());
                            resultDto.setCOMMENT(getText());
                        }

                        if (getName().equals("NAME")) {
                            resultDto.setSHOWED_NAME(getText());
                        }

                        if (getName().equals("INDEX")) {
                            resultDto.setINDEX(Short.valueOf(getText()));
                        }
                    }
                });
            }
        });
    }

    private Component getColumnNamed(String name) {
        for (Component comp : getComponents()) {
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

    private void addTextColumn(String name, String text, Color color) {
        addTextColumn(name, text, null, color);
    }

    private void addTextColumn(String name, String text, String tooltip, Color foreground) {
        this.add(new JLabel(text) {{
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
        return index1 > index2 ? 1 : -1;
    }
}
