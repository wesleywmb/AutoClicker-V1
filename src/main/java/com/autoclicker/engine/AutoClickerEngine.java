package com.autoclicker.engine;

import com.autoclicker.config.AutoClickerConfig;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class AutoClickerEngine {
    
    private final Robot robot;
    private final AutoClickerConfig config;
    private Thread clickThread;
    private final AtomicBoolean shouldStop = new AtomicBoolean(false);
    private final AtomicInteger clickCount = new AtomicInteger(0);
    private Runnable onStopCallback;
    
    public AutoClickerEngine(AutoClickerConfig config) {
        this.config = config;
        try {
            this.robot = new Robot();
            this.robot.setAutoDelay(0);
            this.robot.setAutoWaitForIdle(false);
        } catch (AWTException e) {
            throw new IllegalStateException("Erro ao inicializar Robot: " + e.getMessage(), e);
        }
    }
    
    public void start() {
        if (clickThread != null && clickThread.isAlive()) {
            return;
        }
        
        shouldStop.set(false);
        clickCount.set(0);
        config.setRunning(true);
        
        clickThread = new Thread(this::clickLoop, "AutoClicker-Thread");
        clickThread.setDaemon(true);
        clickThread.start();
    }
    
    public void stop() {
        shouldStop.set(true);
        config.setRunning(false);
        
        if (clickThread != null && clickThread.isAlive()) {
            clickThread.interrupt();
            try {
                clickThread.join(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void toggle() {
        if (config.isRunning()) {
            stop();
        } else {
            start();
        }
    }
    
    private void clickLoop() {
        try {
            while (!shouldStop.get() && !Thread.currentThread().isInterrupted()) {
                if (!config.isInfiniteRepeat() && clickCount.get() >= config.getRepeatCount()) {
                    break;
                }
                
                performClick();
                clickCount.incrementAndGet();
                
                long delay = config.getClickIntervalMillis();
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            config.setRunning(false);
            if (onStopCallback != null) {
                onStopCallback.run();
            }
        }
    }
    
    private void performClick() {
        int buttonMask = config.getMouseButton().getMask();
        int clickCount = config.getClickType().getClickCount();
        
        for (int i = 0; i < clickCount; i++) {
            robot.mousePress(buttonMask);
            robot.mouseRelease(buttonMask);
            
            if (clickCount > 1 && i < clickCount - 1) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    public int getClickCount() {
        return clickCount.get();
    }
    
    public void resetClickCount() {
        clickCount.set(0);
    }
    
    public boolean isRunning() {
        return config.isRunning() && clickThread != null && clickThread.isAlive();
    }
    
    public void setOnStopCallback(Runnable callback) {
        this.onStopCallback = callback;
    }
}
