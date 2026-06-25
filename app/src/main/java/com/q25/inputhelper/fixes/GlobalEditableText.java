package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;

final class GlobalEditableText {
    private GlobalEditableText() {
    }

    static boolean hasActiveEditable(AccessibilityService service) {
        AccessibilityNodeInfo editable = findActiveEditable(service);
        if (editable != null) {
            editable.recycle();
            return true;
        }
        return isInputMethodAcceptingText(service);
    }

    static AccessibilityNodeInfo findActiveEditable(AccessibilityService service) {
        if (service == null) {
            return null;
        }

        AccessibilityNodeInfo root = service.getRootInActiveWindow();
        if (root == null) {
            return null;
        }

        try {
            AccessibilityNodeInfo focused = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
            if (focused != null) {
                if (focused.isEditable()) {
                    return focused;
                }
                focused.recycle();
            }
            return findFocusedEditableInTree(root);
        } finally {
            root.recycle();
        }
    }

    private static boolean isInputMethodAcceptingText(AccessibilityService service) {
        if (service == null) {
            return false;
        }
        InputMethodManager inputMethodManager =
                (InputMethodManager) service.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager != null && inputMethodManager.isAcceptingText();
    }

    private static AccessibilityNodeInfo findFocusedEditableInTree(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        if (node.isFocused() && node.isEditable()) {
            return AccessibilityNodeInfo.obtain(node);
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child == null) {
                continue;
            }

            try {
                AccessibilityNodeInfo match = findFocusedEditableInTree(child);
                if (match != null) {
                    return match;
                }
            } finally {
                child.recycle();
            }
        }
        return null;
    }
}
