package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.PowerManager;
import android.view.KeyEvent;

import com.q25.inputhelper.input.InputFix;

public final class SpaceWakeInputFix implements InputFix {
    private static final long WAKE_LOCK_TIMEOUT_MS = 3_000L;
    private final ScreenWakeController controller;

    public SpaceWakeInputFix() {
        this(new ScreenWakeController());
    }

    SpaceWakeInputFix(ScreenWakeController controller) {
        this.controller = controller;
    }

    @Override
    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        if (service == null || event == null) {
            return false;
        }

        return controller.onKeyEvent(
                new AndroidScreenState(service),
                new AndroidWakeAction(service),
                event.getAction(),
                event.getKeyCode(),
                event.getRepeatCount()
        );
    }

    interface ScreenState {
        boolean isInteractive();
    }

    interface WakeAction {
        boolean wakeScreen();
    }

    static final class ScreenWakeController {
        private boolean waitingForSpaceUp;

        boolean onKeyEvent(
                ScreenState screenState,
                WakeAction wakeAction,
                int action,
                int keyCode,
                int repeatCount
        ) {
            if (keyCode != KeyEvent.KEYCODE_SPACE) {
                return false;
            }

            if (action == KeyEvent.ACTION_UP && waitingForSpaceUp) {
                waitingForSpaceUp = false;
                return true;
            }

            if (action != KeyEvent.ACTION_DOWN) {
                return false;
            }

            if (screenState.isInteractive()) {
                return false;
            }

            if (repeatCount > 0) {
                waitingForSpaceUp = true;
                return true;
            }

            boolean wokeScreen = wakeAction.wakeScreen();
            waitingForSpaceUp = wokeScreen;
            return wokeScreen;
        }
    }

    private static final class AndroidScreenState implements ScreenState {
        private final Context context;

        private AndroidScreenState(Context context) {
            this.context = context;
        }

        @Override
        public boolean isInteractive() {
            PowerManager powerManager = powerManager(context);
            return powerManager != null && powerManager.isInteractive();
        }
    }

    private static final class AndroidWakeAction implements WakeAction {
        private final Context context;

        private AndroidWakeAction(Context context) {
            this.context = context;
        }

        @Override
        public boolean wakeScreen() {
            PowerManager powerManager = powerManager(context);
            if (powerManager == null) {
                return false;
            }

            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE,
                    "com.q25.inputhelper:spaceWake"
            );
            wakeLock.acquire(WAKE_LOCK_TIMEOUT_MS);
            return true;
        }
    }

    private static PowerManager powerManager(Context context) {
        return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }
}
