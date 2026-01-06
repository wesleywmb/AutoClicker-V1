package com.autoclicker.engine;

import com.autoclicker.config.AutoClickerConfig;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class AutoClickerEngine {
    private final Robot robot;
    private final AutoClickerConfig config;
    private Thread clickThread;
    private volatile boolean shouldStop;
    private int clickCount;

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

        shouldStop = false;
        clickCount = 0;
        config.setRunning(true);

        clickThread = new Thread(() -> {
            while (!shouldStop) {
                if (!config.isInfiniteRepeat() && clickCount >= config.getRepeatCount()) {
                    stop();
                    break;
                }

                performClick();
                clickCount++;

                try {
                    Thread.sleep(config.getDelayMillis());
                } catch (InterruptedException e) {
                    break;
                }
            }
            config.setRunning(false);
        });

        clickThread.setDaemon(true);
        clickThread.start();
    }

    public void stop() {
        shouldStop = true;
        config.setRunning(false);
        if (clickThread != null) {
            clickThread.interrupt();
        }
    }

    private void performClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void toggle() {
        if (config.isRunning()) {
            stop();
        } else {
            start();
        }
    }

    public int getClickCount() {
        return clickCount;
    }

    public void resetClickCount() {
        clickCount = 0;
    }
}
