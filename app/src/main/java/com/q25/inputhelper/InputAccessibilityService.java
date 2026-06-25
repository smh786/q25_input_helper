package com.q25.inputhelper;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.q25.inputhelper.fixes.CalculatorInputFix;
import com.q25.inputhelper.fixes.GlobalAltEnterNewlineFix;
import com.q25.inputhelper.fixes.ChatComposerSendInputFix;
import com.q25.inputhelper.fixes.SystemUiPinInputFix;
import com.q25.inputhelper.input.InputFixRegistry;
import com.q25.inputhelper.settings.HelperScreen;
import com.q25.inputhelper.settings.HelperScreenSettings;

public final class InputAccessibilityService extends AccessibilityService {
    private InputFixRegistry registry;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        registry = new InputFixRegistry();
        registry.add(new GlobalAltEnterNewlineFix(), isHelperEnabled(HelperScreen.CHAT_COMPOSER_SHORTCUTS));
        registry.add(new SystemUiPinInputFix(), isHelperEnabled(HelperScreen.SYSTEM_UI_PIN));
        registry.add(new CalculatorInputFix(), isHelperEnabled(HelperScreen.CALCULATOR));
        registry.add(new ChatComposerSendInputFix(), isHelperEnabled(HelperScreen.CHAT_COMPOSER_SHORTCUTS));
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

    private InputFixRegistry.EnabledState isHelperEnabled(final HelperScreen helperScreen) {
        return new InputFixRegistry.EnabledState() {
            @Override
            public boolean isEnabled() {
                return HelperScreenSettings.isEnabled(InputAccessibilityService.this, helperScreen);
            }
        };
    }
}
