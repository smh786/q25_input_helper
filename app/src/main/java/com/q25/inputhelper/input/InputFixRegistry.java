package com.q25.inputhelper.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public final class InputFixRegistry {
    private final List<InputFix> fixes = new ArrayList<>();

    public void add(InputFix fix) {
        if (fix != null) fixes.add(fix);
    }

    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        for (InputFix fix : fixes) {
            if (fix.onKeyEvent(service, event)) return true;
        }

        return false;
    }
}
