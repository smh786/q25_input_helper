package com.q25.inputhelper;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.q25.inputhelper.fixes.SystemUiPinInputFix;
import com.q25.inputhelper.input.InputFixRegistry;

public final class InputAccessibilityService extends AccessibilityService {
    private static final String TAG = "Q25InputService";
    private InputFixRegistry registry;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        registry = new InputFixRegistry();
        registry.add(new SystemUiPinInputFix());
        Log.i(TAG, "service connected");
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (registry == null || event == null) return false;
        return registry.onKeyEvent(this, event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Intentionally left empty. Accessibility events are handled by the input fix logic.
    }

    @Override
    public void onInterrupt() {
        // No persistent work to cancel.
    }
}
