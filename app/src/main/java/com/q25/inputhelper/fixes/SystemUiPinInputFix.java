package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.q25.inputhelper.input.InputFix;
import com.q25.inputhelper.input.Q25KeyTranslator;
import com.q25.inputhelper.util.AccessibilityNodes;

public final class SystemUiPinInputFix implements InputFix {
    static final String PIN_VIEW_ID = "com.android.systemui:id/keyguard_pin_view";

    @Override
    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) return false;

        Q25KeyTranslator.Input input = Q25KeyTranslator.toPinInput(event.getKeyCode());
        if (input == null) return false;

        AccessibilityNodeInfo root = service.getRootInActiveWindow();
        if (root == null) return false;

        try {
            AccessibilityNodeInfo pinView = AccessibilityNodes.findSingleNode(root, PIN_VIEW_ID);
            if (pinView == null) return false;

            try {
                String buttonId = Q25KeyTranslator.systemUiPinButtonId(input);
                AccessibilityNodeInfo button = AccessibilityNodes.findSingleNode(root, buttonId);
                if (button == null) return false;

                try {
                    if (!AccessibilityNodes.isDescendantOf(button, pinView)) return false;
                    if (!button.isClickable()) return false;
                    if (event.getRepeatCount() > 0) return true;
                    return button.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } finally {
                    button.recycle();
                }
            } finally {
                pinView.recycle();
            }
        } finally {
            root.recycle();
        }
    }
}
