package com.autoclicker.ui;

import com.autoclicker.config.AutoClickerConfig;
import com.autoclicker.engine.AutoClickerEngine;
import com.autoclicker.listener.HotkeyListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AutoClickerUI extends JFrame {
    private final AutoClickerConfig config;
    private final AutoClickerEngine engine;
    private final HotkeyListener hotkeyListener;

    private JSpinner spinnerCPS;
    private JTextField textFieldHotkey;
    private JSpinner spinnerRepeat;
    private JCheckBox checkInfinite;
    private JLabel labelStatus;
    private JButton btnToggle;

    private static final Color BG = new Color(18, 18, 20);
    private static final Color SURFACE = new Color(28, 28, 32);
    private static final Color BORDER = new Color(44, 44, 50);
    private static final Color TEXT = new Color(235, 235, 240);
    private static final Color MUTED = new Color(165, 165, 175);
    private static final Color INPUT_BG = new Color(22, 22, 26);

    private static final Color ACCENT_GREEN = new Color(46, 160, 67);
    private static final Color ACCENT_RED = new Color(220, 53, 69);
    private static final Color ACCENT_AMBER = new Color(255, 193, 7);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 14);

    public AutoClickerUI() {
        config = new AutoClickerConfig();
        engine = new AutoClickerEngine(config);
        hotkeyListener = new HotkeyListener(this);

        applyGlobalStyle();
        initializeUI();
        hotkeyListener.start();
    }

    private void applyGlobalStyle() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("Panel.background", BG);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", FONT_TEXT);

        UIManager.put("Button.font", FONT_BTN);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", ACCENT_GREEN);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("CheckBox.foreground", TEXT);
        UIManager.put("CheckBox.font", FONT_TEXT);

        UIManager.put("TextField.background", INPUT_BG);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.caretForeground", TEXT);

        UIManager.put("Spinner.background", INPUT_BG);
        UIManager.put("Spinner.foreground", TEXT);
    }

    private void initializeUI() {
        setTitle("AutoClicker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setSize(420, 560);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));
        setContentPane(root);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 12, 0);

        JLabel header = new JLabel("AutoClicker");
        header.setFont(FONT_TITLE);
        header.setForeground(TEXT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        root.add(header, gbc);

        gbc.gridy++;
        root.add(createSpeedCard(), gbc);

        gbc.gridy++;
        root.add(createRepeatCard(), gbc);

        gbc.gridy++;
        root.add(createHotkeyCard(), gbc);

        gbc.gridy++;
        root.add(createStatusCard(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 0, 0);
        root.add(createControlRow(), gbc);
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(SURFACE);
        card.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(14, 16, 14, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel label = new JLabel(title);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT);
        card.add(label, gbc);

        return card;
    }

    private JPanel createSpeedCard() {
        JPanel card = createCard("Velocidade");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        spinnerCPS = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        spinnerCPS.setPreferredSize(new Dimension(90, 34));
        styleSpinner(spinnerCPS);
        spinnerCPS.addChangeListener(e -> config.setClicksPerSecond((Integer) spinnerCPS.getValue()));

        JLabel suffix = new JLabel("cliques/seg");
        suffix.setForeground(MUTED);
        suffix.setFont(FONT_TEXT);

        row.add(spinnerCPS);
        row.add(suffix);

        card.add(row, gbc);
        return card;
    }

    private JPanel createRepeatCard() {
        JPanel card = createCard("Repetição");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        checkInfinite = new JCheckBox("Modo infinito");
        checkInfinite.setOpaque(false);
        checkInfinite.setSelected(true);
        checkInfinite.setFocusPainted(false);
        checkInfinite.setForeground(TEXT);
        checkInfinite.setFont(FONT_TEXT);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        row.setOpaque(false);

        spinnerRepeat = new JSpinner(new SpinnerNumberModel(100, 1, 999999, 10));
        spinnerRepeat.setPreferredSize(new Dimension(110, 32));
        styleSpinner(spinnerRepeat);
        spinnerRepeat.setEnabled(false);
        spinnerRepeat.addChangeListener(e -> config.setRepeatCount((Integer) spinnerRepeat.getValue()));

        JLabel suffix = new JLabel("cliques");
        suffix.setForeground(MUTED);
        suffix.setFont(FONT_TEXT);

        row.add(spinnerRepeat);
        row.add(suffix);

        checkInfinite.addActionListener(e -> {
            boolean infinite = checkInfinite.isSelected();
            config.setInfiniteRepeat(infinite);
            spinnerRepeat.setEnabled(!infinite);
        });

        container.add(checkInfinite);
        container.add(row);

        card.add(container, gbc);
        return card;
    }

    private JPanel createHotkeyCard() {
        JPanel card = createCard("Hotkey");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        textFieldHotkey = new JTextField(config.getHotkeyActivation(), 6);
        textFieldHotkey.setEditable(false);
        textFieldHotkey.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldHotkey.setPreferredSize(new Dimension(90, 34));
        textFieldHotkey.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textFieldHotkey.setBackground(INPUT_BG);
        textFieldHotkey.setForeground(ACCENT_AMBER);
        textFieldHotkey.setCaretColor(TEXT);
        textFieldHotkey.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(6, 8, 6, 8)));

        JButton btnChange = new JButton("Alterar");
        styleSecondaryButton(btnChange);
        btnChange.addActionListener(e -> changeHotkey());

        row.add(textFieldHotkey);
        row.add(btnChange);

        card.add(row, gbc);
        return card;
    }

    private JPanel createStatusCard() {
        JPanel card = createCard("Status");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labelStatus = new JLabel("● Parado");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        labelStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelStatus.setForeground(MUTED);

        card.add(labelStatus, gbc);
        return card;
    }

    private JPanel createControlRow() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        btnToggle = new JButton("▶  Iniciar");
        btnToggle.setPreferredSize(new Dimension(0, 44));
        btnToggle.setFont(FONT_BTN);
        btnToggle.setBackground(ACCENT_GREEN);
        btnToggle.setForeground(Color.WHITE);
        btnToggle.setFocusPainted(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setOpaque(true);
        btnToggle.setContentAreaFilled(true);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> {
            if (config.isRunning()) stopClicking();
            else startClicking();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(btnToggle, gbc);

        return panel;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(2, 2, 2, 2)));
        spinner.setBackground(INPUT_BG);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(INPUT_BG);
            tf.setForeground(TEXT);
            tf.setCaretColor(TEXT);
            tf.setBorder(new EmptyBorder(6, 8, 6, 8));
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 12, 8, 12)));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void changeHotkey() {
        JDialog dialog = new JDialog(this, "Pressione uma tecla", true);
        dialog.setSize(300, 140);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(SURFACE);

        JLabel label = new JLabel("Pressione a nova tecla...");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        dialog.setLayout(new BorderLayout());
        dialog.add(label, BorderLayout.CENTER);

        dialog.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                config.setHotkeyActivation(keyText);
                textFieldHotkey.setText(keyText);
                dialog.dispose();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        dialog.setVisible(true);
    }

    public void startClicking() {
        engine.start();
        updateUIState(true);
    }

    public void stopClicking() {
        engine.stop();
        updateUIState(false);
    }

    private void updateUIState(boolean running) {
        spinnerCPS.setEnabled(!running);
        spinnerRepeat.setEnabled(!running && !checkInfinite.isSelected());
        checkInfinite.setEnabled(!running);

        if (running) {
            btnToggle.setText("⏸  Parar");
            btnToggle.setBackground(ACCENT_RED);
            labelStatus.setText("● Executando");
            labelStatus.setForeground(ACCENT_GREEN);
        } else {
            btnToggle.setText("▶  Iniciar");
            btnToggle.setBackground(ACCENT_GREEN);
            labelStatus.setText("● Parado");
            labelStatus.setForeground(MUTED);
        }
    }

    public AutoClickerConfig getConfig() {
        return config;
    }

    public AutoClickerEngine getEngine() {
        return engine;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AutoClickerUI ui = new AutoClickerUI();
            ui.setVisible(true);
        });
    }
}
