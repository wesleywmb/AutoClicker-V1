package com.autoclicker.ui;

import com.autoclicker.config.AutoClickerConfig;
import com.autoclicker.config.AutoClickerConfig.ClickType;
import com.autoclicker.config.AutoClickerConfig.MouseButton;
import com.autoclicker.engine.AutoClickerEngine;
import com.autoclicker.listener.HotkeyListener;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AutoClickerUI extends JFrame {
    
    private final AutoClickerConfig config;
    private final AutoClickerEngine engine;
    private final HotkeyListener hotkeyListener;
    
    private JSpinner spinnerInterval;
    private JComboBox<MouseButton> comboMouseButton;
    private JComboBox<ClickType> comboClickType;
    private JTextField textFieldHotkey;
    private JSpinner spinnerRepeat;
    private JCheckBox checkInfinite;
    private JLabel labelStatus;
    private JLabel labelClickCount;
    private JButton btnToggle;
    
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_RED = new Color(244, 67, 54);
    private static final Color ACCENT_BLUE = new Color(33, 150, 243);
    
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 15);
    
    public AutoClickerUI() {
        config = new AutoClickerConfig();
        engine = new AutoClickerEngine(config);
        hotkeyListener = new HotkeyListener(this);
        
        engine.setOnStopCallback(() -> SwingUtilities.invokeLater(() -> updateUIState(false)));
        
        applyModernLookAndFeel();
        initializeUI();
        hotkeyListener.start();
        startStatusUpdateTimer();
    }
    
    private void applyModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("CheckBox.arc", 5);
        } catch (Exception e) {
            System.err.println("Falha ao aplicar FlatLaf: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }
    
    private void initializeUI() {
        setTitle("AutoClicker Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        root.add(createHeader());
        root.add(Box.createVerticalStrut(20));
        
        root.add(createIntervalSection());
        root.add(Box.createVerticalStrut(12));
        
        root.add(createMouseConfigSection());
        root.add(Box.createVerticalStrut(12));
        
        root.add(createRepeatSection());
        root.add(Box.createVerticalStrut(12));
        
        root.add(createHotkeySection());
        root.add(Box.createVerticalStrut(12));
        
        root.add(createStatusSection());
        root.add(Box.createVerticalStrut(16));
        
        root.add(createControlPanel());
        
        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(450, 500));
        setLocationRelativeTo(null);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("AutoClicker Pro");
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Automação de Cliques");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        
        return panel;
    }
    
    private JPanel createIntervalSection() {
        JPanel card = createCard("Intervalo de Clique");
        
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JLabel label = new JLabel("Clicar a cada:");
        label.setFont(FONT_LABEL);
        
        SpinnerNumberModel model = new SpinnerNumberModel(0.2, 0.001, 60.0, 0.01);
        spinnerInterval = new JSpinner(model);
        spinnerInterval.setPreferredSize(new Dimension(100, 32));
        
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerInterval, "0.###");
        spinnerInterval.setEditor(editor);
        
        spinnerInterval.addChangeListener(e -> {
            double value = (Double) spinnerInterval.getValue();
            config.setClickIntervalSeconds(value);
        });
        
        JLabel suffix = new JLabel("segundos");
        suffix.setFont(FONT_LABEL);
        
        JLabel cpsLabel = new JLabel();
        cpsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        updateCPSLabel(cpsLabel);
        
        spinnerInterval.addChangeListener(e -> updateCPSLabel(cpsLabel));
        
        content.add(label);
        content.add(spinnerInterval);
        content.add(suffix);
        content.add(cpsLabel);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private void updateCPSLabel(JLabel label) {
        double interval = (Double) spinnerInterval.getValue();
        int cps = (int) Math.round(1.0 / interval);
        label.setText(String.format("(≈ %d CPS)", cps));
    }
    
    private JPanel createMouseConfigSection() {
        JPanel card = createCard("Configuração do Mouse");
        
        JPanel content = new JPanel(new GridLayout(2, 2, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel labelButton = new JLabel("Botão:");
        labelButton.setFont(FONT_LABEL);
        
        comboMouseButton = new JComboBox<>(MouseButton.values());
        comboMouseButton.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MouseButton) {
                    setText(((MouseButton) value).getDisplayName());
                }
                return this;
            }
        });
        comboMouseButton.addActionListener(e -> 
            config.setMouseButton((MouseButton) comboMouseButton.getSelectedItem())
        );
        
        JLabel labelType = new JLabel("Tipo:");
        labelType.setFont(FONT_LABEL);
        
        comboClickType = new JComboBox<>(ClickType.values());
        comboClickType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClickType) {
                    setText(((ClickType) value).getDisplayName());
                }
                return this;
            }
        });
        comboClickType.addActionListener(e -> 
            config.setClickType((ClickType) comboClickType.getSelectedItem())
        );
        
        content.add(labelButton);
        content.add(comboMouseButton);
        content.add(labelType);
        content.add(comboClickType);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createRepeatSection() {
        JPanel card = createCard("Repetição");
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        checkInfinite = new JCheckBox("Modo infinito (rodar até parar manualmente)");
        checkInfinite.setFont(FONT_LABEL);
        checkInfinite.setSelected(true);
        checkInfinite.setFocusPainted(false);
        
        JPanel limitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JLabel labelLimit = new JLabel("Parar após:");
        labelLimit.setFont(FONT_LABEL);
        
        spinnerRepeat = new JSpinner(new SpinnerNumberModel(100, 1, 999999, 10));
        spinnerRepeat.setPreferredSize(new Dimension(120, 32));
        spinnerRepeat.setEnabled(false);
        spinnerRepeat.addChangeListener(e -> 
            config.setRepeatCount((Integer) spinnerRepeat.getValue())
        );
        
        JLabel suffix = new JLabel("cliques");
        suffix.setFont(FONT_LABEL);
        
        limitPanel.add(labelLimit);
        limitPanel.add(spinnerRepeat);
        limitPanel.add(suffix);
        
        checkInfinite.addActionListener(e -> {
            boolean infinite = checkInfinite.isSelected();
            config.setInfiniteRepeat(infinite);
            spinnerRepeat.setEnabled(!infinite);
            labelLimit.setEnabled(!infinite);
            suffix.setEnabled(!infinite);
        });
        
        content.add(checkInfinite);
        content.add(limitPanel);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createHotkeySection() {
        JPanel card = createCard("Hotkey de Ativação");
        
        JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JLabel label = new JLabel("Tecla:");
        label.setFont(FONT_LABEL);
        
        textFieldHotkey = new JTextField(config.getHotkeyActivation(), 8);
        textFieldHotkey.setEditable(false);
        textFieldHotkey.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldHotkey.setPreferredSize(new Dimension(100, 36));
        textFieldHotkey.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textFieldHotkey.setBackground(new Color(255, 193, 7, 30));
        textFieldHotkey.setForeground(new Color(255, 193, 7));
        
        JButton btnChange = new JButton("Alterar");
        btnChange.setPreferredSize(new Dimension(100, 36));
        btnChange.addActionListener(e -> changeHotkey());
        
        content.add(label);
        content.add(textFieldHotkey);
        content.add(btnChange);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createStatusSection() {
        JPanel card = createCard("Status");
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        labelStatus = new JLabel("Parado");
        labelStatus.setFont(new Font("Segoe UI", Font.BOLD, 15));
        labelStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        labelClickCount = new JLabel("Cliques executados: 0");
        labelClickCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelClickCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(labelStatus);
        content.add(Box.createVerticalStrut(5));
        content.add(labelClickCount);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        btnToggle = new JButton("Iniciar");
        btnToggle.setFont(FONT_BTN);
        btnToggle.setPreferredSize(new Dimension(400, 50));
        btnToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnToggle.setBackground(ACCENT_GREEN);
        btnToggle.setForeground(Color.WHITE);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> {
            if (config.isRunning()) stopClicking();
            else startClicking();
        });
        
        JLabel hint = new JLabel(String.format("Pressione %s para iniciar/parar", 
            config.getHotkeyActivation()));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(btnToggle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(hint);
        
        return panel;
    }
    
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SECTION);
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    private void changeHotkey() {
        JDialog dialog = new JDialog(this, "Nova Hotkey", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JLabel label = new JLabel("Pressione a nova tecla de atalho...");
        label.setFont(FONT_LABEL);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
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
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        dialog.setVisible(true);
    }
    
    public void startClicking() {
        engine.resetClickCount();
        engine.start();
        updateUIState(true);
    }
    
    public void stopClicking() {
        engine.stop();
        updateUIState(false);
    }
    
    private void updateUIState(boolean running) {
        spinnerInterval.setEnabled(!running);
        comboMouseButton.setEnabled(!running);
        comboClickType.setEnabled(!running);
        spinnerRepeat.setEnabled(!running && !checkInfinite.isSelected());
        checkInfinite.setEnabled(!running);
        
        if (running) {
            btnToggle.setText("Parar");
            btnToggle.setBackground(ACCENT_RED);
            labelStatus.setText("Executando");
            labelStatus.setForeground(ACCENT_GREEN);
        } else {
            btnToggle.setText("Iniciar");
            btnToggle.setBackground(ACCENT_GREEN);
            labelStatus.setText("Parado");
            labelStatus.setForeground(Color.GRAY);
        }
    }
    
    private void startStatusUpdateTimer() {
        Timer timer = new Timer(100, e -> {
            if (config.isRunning()) {
                int count = engine.getClickCount();
                if (config.isInfiniteRepeat()) {
                    labelClickCount.setText(String.format("Cliques executados: %d", count));
                } else {
                    int total = config.getRepeatCount();
                    labelClickCount.setText(String.format("Cliques: %d / %d (%.1f%%)", 
                        count, total, (count * 100.0 / total)));
                }
            } else {
                labelClickCount.setText(String.format("Cliques executados: %d", engine.getClickCount()));
            }
        });
        timer.start();
    }
    
    public AutoClickerConfig getConfig() {
        return config;
    }
    
    public AutoClickerEngine getEngine() {
        return engine;
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "true");
        System.setProperty("sun.java2d.dpiaware", "true");
        
        SwingUtilities.invokeLater(() -> {
            AutoClickerUI ui = new AutoClickerUI();
            ui.setVisible(true);
        });
    }
}
