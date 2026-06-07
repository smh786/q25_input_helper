package com.q25.inputhelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.q25.inputhelper.fixes.SystemUiPinInputFix;
import com.q25.inputhelper.input.InputFixRegistry;

public final class InputAccessibilityService extends AccessibilityService {
    private InputFixRegistry registry;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                | AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        info.notificationTimeout = 100;
        setServiceInfo(info);

        registry = new InputFixRegistry();
        registry.add(new SystemUiPinInputFix());
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (registry == null || event == null) return false;
        return registry.onKeyEvent(this, event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Key handling is delegated from onKeyEvent. Screen detection happens inside each fix.
    }

    @Override
    public void onInterrupt() {
        // No persistent work to cancel.
    }
}
