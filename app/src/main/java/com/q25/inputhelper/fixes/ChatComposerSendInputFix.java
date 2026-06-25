package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

import com.q25.inputhelper.input.ComposerEnterKeyHandler;
import com.q25.inputhelper.input.InputFix;

public final class ChatComposerSendInputFix implements InputFix {
    private final ComposerEnterKeyHandler handler = new ComposerEnterKeyHandler(
            ComposerEnterKeyHandler.defaultSupportedPackages(),
            ComposerEnterKeyHandler.defaultSendButtonMatchers()
    );

    @Override
    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        return handler.onKeyEvent(service, event);
    }
}
