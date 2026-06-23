package com.q25.inputhelper.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

import org.junit.Test;

public final class InputFixRegistryTest {
    @Test
    public void onKeyEventSkipsDisabledFixes() {
        InputFixRegistry registry = new InputFixRegistry();
        FakeEnabledState enabledState = new FakeEnabledState(false);
        CountingFix disabledFix = new CountingFix(true);
        CountingFix enabledFix = new CountingFix(true);

        registry.add(disabledFix, enabledState);
        registry.add(enabledFix);

        assertTrue(registry.onKeyEvent(null, keyEvent()));
        assertEquals(0, disabledFix.callCount);
        assertEquals(1, enabledFix.callCount);
    }

    @Test
    public void onKeyEventUsesCurrentEnabledStateForEachEvent() {
        InputFixRegistry registry = new InputFixRegistry();
        FakeEnabledState enabledState = new FakeEnabledState(false);
        CountingFix fix = new CountingFix(true);

        registry.add(fix, enabledState);

        assertFalse(registry.onKeyEvent(null, keyEvent()));
        enabledState.enabled = true;
        assertTrue(registry.onKeyEvent(null, keyEvent()));
        assertEquals(1, fix.callCount);
    }

    private static KeyEvent keyEvent() {
        return new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_W);
    }

    private static final class FakeEnabledState implements InputFixRegistry.EnabledState {
        private boolean enabled;

        private FakeEnabledState(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }

    private static final class CountingFix implements InputFix {
        private final boolean handled;
        private int callCount;

        private CountingFix(boolean handled) {
            this.handled = handled;
        }

        @Override
        public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
            callCount++;
            return handled;
        }
    }
}
