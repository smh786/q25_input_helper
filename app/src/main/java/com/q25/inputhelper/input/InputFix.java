package com.q25.inputhelper.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public interface InputFix {
    boolean onKeyEvent(AccessibilityService service, KeyEvent event);
}
