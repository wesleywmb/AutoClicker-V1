package com.autoclicker.listener;

import com.autoclicker.ui.AutoClickerUI;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.SwingUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HotkeyListener implements NativeKeyListener {
    private static final Logger LOG = Logger.getLogger(HotkeyListener.class.getName());

    private final AutoClickerUI ui;
    private volatile boolean pressed;

    public HotkeyListener(AutoClickerUI ui) {
        this.ui = ui;
    }

    /**
     * Backward-compatible wrapper.
     * Prefer {@link #register()}.
     */
    public void start() {
        register();
    }

    /**
     * Backward-compatible wrapper.
     * Prefer {@link #unregister()}.
     */
    public void stop() {
        unregister();
    }

    public void register() {
        silenceJNativeHookLogs();

        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
            }
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException e) {
            throw new IllegalStateException("Falha ao registrar hotkey global (JNativeHook): " + e.getMessage(), e);
        }
    }

    public void unregister() {
        try {
            GlobalScreen.removeNativeKeyListener(this);
            if (GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException e) {
            LOG.log(Level.FINE, "Falha ao remover hook global", e);
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (pressed) {
            return;
        }

        if (isActivationHotkey(e)) {
            pressed = true;
            toggleAutoClick();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (isActivationHotkey(e)) {
            pressed = false;
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    private boolean isActivationHotkey(NativeKeyEvent e) {
        String pressedKey = NativeKeyEvent.getKeyText(e.getKeyCode());
        String configured = ui.getConfig().getHotkeyActivation();
        return pressedKey != null && configured != null && pressedKey.equalsIgnoreCase(configured);
    }

    private void toggleAutoClick() {
        SwingUtilities.invokeLater(() -> {
            if (ui.getConfig().isRunning()) {
                ui.stopClicking();
            } else {
                ui.startClicking();
            }
        });
    }

    private static void silenceJNativeHookLogs() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }
}
