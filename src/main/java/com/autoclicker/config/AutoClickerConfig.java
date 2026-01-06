package com.autoclicker.config;

public class AutoClickerConfig {
    private int clicksPerSecond;
    private String hotkeyActivation;
    private boolean isRunning;
    private int repeatCount;
    private boolean infiniteRepeat;

    public AutoClickerConfig() {
        this.clicksPerSecond = 5;
        this.hotkeyActivation = "F6";
        this.isRunning = false;
        this.repeatCount = 100;
        this.infiniteRepeat = true;
    }

    public int getClicksPerSecond() {
        return clicksPerSecond;
    }

    public void setClicksPerSecond(int clicksPerSecond) {
        if (clicksPerSecond < 1) {
            clicksPerSecond = 1;
        }
        if (clicksPerSecond > 100) {
            clicksPerSecond = 100;
        }
        this.clicksPerSecond = clicksPerSecond;
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
        this.repeatCount = repeatCount;
    }

    public boolean isInfiniteRepeat() {
        return infiniteRepeat;
    }

    public void setInfiniteRepeat(boolean infiniteRepeat) {
        this.infiniteRepeat = infiniteRepeat;
    }

    public int getDelayMillis() {
        return 1000 / clicksPerSecond;
    }
}
