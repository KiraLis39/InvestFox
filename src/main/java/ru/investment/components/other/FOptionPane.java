package ru.investment.components.other;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FOptionPane extends JDialog implements ActionListener {
    private static JLabel toLabel;
    private JButton OK_BUTTON;
    private JButton NO_BUTTON, YES_BUTTON;
    private int answer = -1, timeout = 20;


    public FOptionPane(String title, String message) {
        this(title, message, TYPE.DEFAULT, null);
    }

    public FOptionPane(String title, String message, TYPE type, BufferedImage ico) {
        type = type == null ? TYPE.DEFAULT : type;

        try {
            ico = ImageIO.read(new File("./resources/icons/favorite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setModal(true);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

        setTitle(title);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setPreferredSize(new Dimension(300, 150));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().setBorder(new EmptyBorder(3, 3, 3, 3));

        BufferedImage finalIco = ico;
        TYPE finalType = type;
        JPanel basePane = new JPanel(new BorderLayout(3, 3)) {
            {
                setBackground(Color.DARK_GRAY);
                setBorder(new EmptyBorder(3, 3, 3, 3));

                JPanel icoPane = new JPanel() {
                    @Override
                    public void paintComponent(Graphics g) {
                        if (finalIco != null) {
                            g.drawImage(finalIco,

                                    0, 0,
                                    64, 64,

                                    0, 0,
                                    finalIco.getWidth(), finalIco.getHeight(),

                                    this);
                        }
                    }

                    {
                        setPreferredSize(new Dimension(64, 0));
                    }
                };

                toLabel = new JLabel() {
                    {
                        setForeground(Color.GRAY);
                    }
                };

                JPanel mesPane = new JPanel(new BorderLayout(3, 3)) {
                    {
                        setOpaque(false);
                        setBorder(new EmptyBorder(16, 6, 0, 0));

                        JTextArea mesArea = new JTextArea() {
                            {
                                setEditable(false);
                                setForeground(Color.WHITE);
                                setText(message);
                                setWrapStyleWord(true);
                                setLineWrap(true);
                                setBorder(null);
                                setBackground(Color.DARK_GRAY);
                            }
                        };

                        JScrollPane mesScroll = new JScrollPane(mesArea) {
                            {
                                setOpaque(false);
                                getViewport().setOpaque(false);
                                setBorder(null);
                                getViewport().setBorder(null);
                            }
                        };

                        add(mesScroll);
                    }
                };

                JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3)) {
                    {
                        setOpaque(false);

                        switch (finalType) {
                            case DEFAULT -> {
                                OK_BUTTON = new JButton("OK") {
                                    {
                                        setActionCommand("ok");
                                        addActionListener(FOptionPane.this);
                                    }
                                };
                                add(OK_BUTTON);
                            }
                            case YES_NO_TYPE -> {
                                YES_BUTTON = new JButton("Да") {
                                    {
                                        setActionCommand("yes");
                                        addActionListener(FOptionPane.this);
                                    }
                                };
                                NO_BUTTON = new JButton("Нет") {
                                    {
                                        setActionCommand("no");
                                        addActionListener(FOptionPane.this);
                                    }
                                };
                                add(YES_BUTTON);
                                add(NO_BUTTON);
                            }
                            default -> log.error("Не обрабатывается тип '{}'", finalType);
                        }
                    }
                };

                add(icoPane, BorderLayout.WEST);
                add(toLabel, BorderLayout.NORTH);
                add(mesPane, BorderLayout.CENTER);
                add(btnPane, BorderLayout.SOUTH);
            }
        };

        add(basePane);

        Thread toThread = new Thread(() -> {
            while (timeout > 0) {
                timeout--;
                toLabel.setText("Осталось: " + timeout + " сек.");
                try {
                    TimeUnit.MILLISECONDS.sleep(1_000);
                } catch (InterruptedException e) {
                    log.warn("Interrupted here: {}", e.getMessage());
                }
            }
            answer = 1;
            dispose();
        });
        toThread.start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public int get() {
        return answer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "yes" -> answer = 0;
            case "no" -> answer = -1;
            case "ok", default -> {
            }
        }

        dispose();
    }

    public enum TYPE {DEFAULT, YES_NO_TYPE}
}
