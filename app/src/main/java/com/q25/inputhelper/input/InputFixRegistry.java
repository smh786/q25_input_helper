package com.q25.inputhelper.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public final class InputFixRegistry {
    private static final EnabledState ALWAYS_ENABLED = new EnabledState() {
        @Override
        public boolean isEnabled() {
            return true;
        }
    };

    private final List<RegisteredFix> fixes = new ArrayList<>();

    public void add(InputFix fix) {
        add(fix, ALWAYS_ENABLED);
    }

    public void add(InputFix fix, EnabledState enabledState) {
        if (fix != null) fixes.add(new RegisteredFix(
                fix,
                enabledState == null ? ALWAYS_ENABLED : enabledState
        ));
    }

    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        for (RegisteredFix fix : fixes) {
            if (fix.enabledState.isEnabled() && fix.inputFix.onKeyEvent(service, event)) return true;
        }

        return false;
    }

    public interface EnabledState {
        boolean isEnabled();
    }

    private static final class RegisteredFix {
        private final InputFix inputFix;
        private final EnabledState enabledState;

        private RegisteredFix(InputFix inputFix, EnabledState enabledState) {
            this.inputFix = inputFix;
            this.enabledState = enabledState;
        }
    }
}
