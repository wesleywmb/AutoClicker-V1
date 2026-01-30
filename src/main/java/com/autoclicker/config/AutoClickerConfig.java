package com.autoclicker.config;

import java.awt.event.InputEvent;

public class AutoClickerConfig {
    
    private double clickIntervalSeconds;
    private MouseButton mouseButton;
    private ClickType clickType;
    private int repeatCount;
    private boolean infiniteRepeat;
    private String hotkeyActivation;
    private volatile boolean isRunning;
    
    private static final double MIN_INTERVAL = 0.001;
    private static final double MAX_INTERVAL = 60.0;
    
    /**
     * Enumeração para botões do mouse
     */
    public enum MouseButton {
        LEFT("Esquerdo", InputEvent.BUTTON1_DOWN_MASK),
        RIGHT("Direito", InputEvent.BUTTON3_DOWN_MASK),
        MIDDLE("Meio", InputEvent.BUTTON2_DOWN_MASK);
        
        private final String displayName;
        private final int mask;
        
        MouseButton(String displayName, int mask) {
            this.displayName = displayName;
            this.mask = mask;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getMask() {
            return mask;
        }
    }
    
    /**
     * Enumeração para tipos de clique
     */
    public enum ClickType {
        SINGLE("Simples", 1),
        DOUBLE("Duplo", 2);
        
        private final String displayName;
        private final int clickCount;
        
        ClickType(String displayName, int clickCount) {
            this.displayName = displayName;
            this.clickCount = clickCount;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getClickCount() {
            return clickCount;
        }
    }
    
    public AutoClickerConfig() {
        this.clickIntervalSeconds = 0.2;
        this.mouseButton = MouseButton.LEFT;
        this.clickType = ClickType.SINGLE;
        this.hotkeyActivation = "F6";
        this.isRunning = false;
        this.repeatCount = 100;
        this.infiniteRepeat = true;
    }
    
    // ==================== Getters e Setters com Validação ====================
    
    public double getClickIntervalSeconds() {
        return clickIntervalSeconds;
    }
    
    public void setClickIntervalSeconds(double intervalSeconds) {
        if (intervalSeconds < MIN_INTERVAL) {
            intervalSeconds = MIN_INTERVAL;
        }
        if (intervalSeconds > MAX_INTERVAL) {
            intervalSeconds = MAX_INTERVAL;
        }
        this.clickIntervalSeconds = intervalSeconds;
    }
    
    public int getClickIntervalMillis() {
        return (int) (clickIntervalSeconds * 1000);
    }
    
    public void setClickIntervalMillis(int millis) {
        setClickIntervalSeconds(millis / 1000.0);
    }
    
    public MouseButton getMouseButton() {
        return mouseButton;
    }
    
    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton = mouseButton;
    }
    
    public ClickType getClickType() {
        return clickType;
    }
    
    public void setClickType(ClickType clickType) {
        this.clickType = clickType;
    }
    
    public String getHotkeyActivation() {
        return hotkeyActivation;
    }
    
    public void setHotkeyActivation(String hotkeyActivation) {
        this.hotkeyActivation = hotkeyActivation;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void setRunning(boolean running) {
        isRunning = running;
    }
    
    public int getRepeatCount() {
        return repeatCount;
    }
    
    public void setRepeatCount(int repeatCount) {
        this.repeatCount = Math.max(1, repeatCount);
    }
    
    public boolean isInfiniteRepeat() {
        return infiniteRepeat;
    }
    
    public void setInfiniteRepeat(boolean infiniteRepeat) {
        this.infiniteRepeat = infiniteRepeat;
    }
    
    public void setClicksPerSecond(int cps) {
        if (cps < 1) cps = 1;
        if (cps > 1000) cps = 1000;
        setClickIntervalSeconds(1.0 / cps);
    }
    
    public int getClicksPerSecond() {
        return (int) Math.round(1.0 / clickIntervalSeconds);
    }
}
