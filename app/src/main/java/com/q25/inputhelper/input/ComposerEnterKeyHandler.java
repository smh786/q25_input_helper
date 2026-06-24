package com.q25.inputhelper.input;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.q25.inputhelper.util.AccessibilityNodes;

public final class ComposerEnterKeyHandler {
    private final String packageName;
    private final String sendButtonViewId;
    private boolean pendingPlainEnterSend;

    public ComposerEnterKeyHandler(String packageName, String sendButtonViewId) {
        this.packageName = packageName;
        this.sendButtonViewId = sendButtonViewId;
    }

    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        if (!isHandledEnterEvent(event)) {
            return false;
        }

        AccessibilityNodeInfo root = service.getRootInActiveWindow();
        if (root == null) {
            return false;
        }

        try {
            AccessibilityNodeInfo composer = findFocusedComposer(root);
            if (composer == null) {
                pendingPlainEnterSend = false;
                return false;
            }

            try {
                boolean altPressed = isAltPressed(event);
                boolean hasDraft = hasDraftText(composer);
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (altPressed) {
                        if (event.getRepeatCount() == 0) {
                            insertNewline(composer);
                        }
                        pendingPlainEnterSend = false;
                        return true;
                    }

                    pendingPlainEnterSend = shouldArmSendOnKeyDown(altPressed, event.getRepeatCount(), hasDraft);
                    return true;
                }

                if (!shouldClickSendButtonOnKeyUp(altPressed, pendingPlainEnterSend, hasDraft)) {
                    pendingPlainEnterSend = false;
                    return true;
                }

                pendingPlainEnterSend = false;
                AccessibilityNodeInfo sendButton = findSendButton(root);
                if (sendButton == null) {
                    return true;
                }

                try {
                    if (sendButton.isClickable()) {
                        sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    return true;
                } finally {
                    sendButton.recycle();
                }
            } finally {
                composer.recycle();
            }
        } finally {
            root.recycle();
        }
    }

    static boolean isHandledEnterEvent(KeyEvent event) {
        if (event == null) {
            return false;
        }
        return isHandledEnterKey(event.getAction(), event.getKeyCode());
    }

    static boolean isHandledEnterKey(int action, int keyCode) {
        return keyCode == KeyEvent.KEYCODE_ENTER
                && (action == KeyEvent.ACTION_DOWN || action == KeyEvent.ACTION_UP);
    }

    static boolean shouldArmSendOnKeyDown(boolean altPressed, int repeatCount, boolean hasDraft) {
        return !altPressed && repeatCount == 0 && hasDraft;
    }

    static boolean shouldClickSendButtonOnKeyUp(boolean altPressed, boolean pendingPlainEnterSend, boolean hasDraft) {
        return !altPressed && pendingPlainEnterSend && hasDraft;
    }

    static boolean isAltMetaState(int metaState) {
        return (metaState & (KeyEvent.META_ALT_ON
                | KeyEvent.META_ALT_LEFT_ON
                | KeyEvent.META_ALT_RIGHT_ON)) != 0;
    }

    private static boolean isAltPressed(KeyEvent event) {
        return event.isAltPressed() || isAltMetaState(event.getMetaState());
    }

    static boolean hasDraftText(CharSequence text) {
        return text != null && text.toString().trim().length() > 0;
    }

    static CharSequence insertNewlineText(CharSequence text, int selectionStart, int selectionEnd) {
        String current = text == null ? "" : text.toString();
        int start = clampSelection(selectionStart, current.length());
        int end = clampSelection(selectionEnd, current.length());
        int min = Math.min(start, end);
        int max = Math.max(start, end);
        return current.substring(0, min) + "\n" + current.substring(max);
    }

    static CharSequence composerDraftText(CharSequence text, boolean showingHintText) {
        return showingHintText ? "" : text;
    }

    static boolean isSendButtonViewId(String expectedViewId, CharSequence viewId) {
        return viewId != null && expectedViewId.contentEquals(viewId);
    }

    private static int clampSelection(int selection, int length) {
        if (selection < 0) {
            return length;
        }
        return Math.min(selection, length);
    }

    private static void insertNewline(AccessibilityNodeInfo composer) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                insertNewlineText(
                        composerDraftText(composer.getText(), isShowingHintText(composer)),
                        composer.getTextSelectionStart(),
                        composer.getTextSelectionEnd()
                )
        );
        composer.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    private static boolean hasDraftText(AccessibilityNodeInfo composer) {
        return composer != null && hasDraftText(composerDraftText(composer.getText(), isShowingHintText(composer)));
    }

    private static boolean isShowingHintText(AccessibilityNodeInfo node) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && node.isShowingHintText();
    }

    private AccessibilityNodeInfo findFocusedComposer(AccessibilityNodeInfo root) {
        if (!AccessibilityNodes.hasPackage(root, packageName)) {
            return null;
        }

        AccessibilityNodeInfo focused = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (focused == null) {
            return null;
        }

        if (focused.isEditable() && AccessibilityNodes.hasPackage(focused, packageName)) {
            return focused;
        } else {
            focused.recycle();
            return null;
        }
    }

    private AccessibilityNodeInfo findSendButton(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }

        if (isSendButtonViewId(sendButtonViewId, root.getViewIdResourceName()) && root.isClickable()) {
            return AccessibilityNodeInfo.obtain(root);
        }

        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = root.getChild(i);
            if (child == null) {
                continue;
            }

            try {
                AccessibilityNodeInfo match = findSendButton(child);
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
