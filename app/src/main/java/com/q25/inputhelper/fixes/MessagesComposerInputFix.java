package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

import com.q25.inputhelper.input.InputFix;
import com.q25.inputhelper.input.ComposerEnterKeyHandler;

public final class MessagesComposerInputFix implements InputFix {
    static final String MESSAGES_PACKAGE = "com.google.android.apps.messaging";
    static final String SEND_BUTTON_VIEW_ID = "Compose:Draft:Send";
    private final ComposerEnterKeyHandler handler =
            new ComposerEnterKeyHandler(MESSAGES_PACKAGE, SEND_BUTTON_VIEW_ID);

    @Override
    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        return handler.onKeyEvent(service, event);
    }
}
