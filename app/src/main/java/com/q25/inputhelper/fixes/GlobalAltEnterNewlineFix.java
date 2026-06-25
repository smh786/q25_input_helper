package com.q25.inputhelper.fixes;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.q25.inputhelper.input.InputFix;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class GlobalAltEnterNewlineFix implements InputFix {
    private static final List<String> KNOWN_MULTILINE_COMPOSER_PACKAGES = Arrays.asList(
            "com.openai.chatgpt",
            "ai.openai.chatgpt",
            "com.google.android.apps.messaging",
            "com.whatsapp",
            "org.telegram",
            "org.telegram.messenger",
            "org.thoughtcrime.securesms",
            "im.vector.app",
            "com.mattermost.rn",
            "chat.mattermost",
            "com.matter",
            "ai.perplexity",
            "chat.perplexity.android",
            "com.perplexity",
            "ai.perplexity.app"
    );
    private static final List<String> PLACEHOLDER_TEXTS = Arrays.asList(
            "message",
            "text message",
            "type a message",
            "write a message",
            "send a message",
            "reply",
            "reply to message",
            "ask anything",
            "ask a follow up",
            "message chatgpt",
            "reply to chatgpt",
            "message perplexity"
    );

    @Override
    public boolean onKeyEvent(AccessibilityService service, KeyEvent event) {
        if (!isHandledAltEnterEvent(event)) {
            return false;
        }

        AccessibilityNodeInfo editable = GlobalEditableText.findActiveEditable(service);
        if (editable == null) {
            return GlobalEditableText.hasActiveEditable(service);
        }

        try {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0
                    && supportsMultiline(editable)) {
                insertNewline(editable);
            }
            return true;
        } finally {
            editable.recycle();
        }
    }

    static boolean isHandledAltEnterEvent(KeyEvent event) {
        if (event == null) {
            return false;
        }
        return isHandledAltEnterKey(
                event.getAction(),
                event.getKeyCode(),
                event.getMetaState()
        );
    }

    static boolean isHandledAltEnterKey(
            int action,
            int keyCode,
            int metaState
    ) {
        return (action == KeyEvent.ACTION_DOWN || action == KeyEvent.ACTION_UP)
                && keyCode == KeyEvent.KEYCODE_ENTER
                && isLeftAltPressed(metaState)
                && !isShiftPressed(metaState);
    }

    static boolean isLeftAltPressed(int metaState) {
        return (metaState & KeyEvent.META_ALT_LEFT_ON) != 0;
    }

    static boolean isShiftPressed(int metaState) {
        return (metaState & (KeyEvent.META_SHIFT_ON
                | KeyEvent.META_SHIFT_LEFT_ON
                | KeyEvent.META_SHIFT_RIGHT_ON)) != 0;
    }

    static boolean supportsMultiline(AccessibilityNodeInfo node) {
        if (node == null) {
            return false;
        }
        return node.isMultiLine()
                || (node.getInputType() & InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0
                || isKnownMultilineComposerPackage(node.getPackageName());
    }

    static boolean isKnownMultilineComposerPackage(CharSequence packageName) {
        if (packageName == null) {
            return false;
        }

        String actualPackage = packageName.toString();
        for (String knownPackage : KNOWN_MULTILINE_COMPOSER_PACKAGES) {
            if (actualPackage.equals(knownPackage) || actualPackage.startsWith(knownPackage + ".")) {
                return true;
            }
        }
        return false;
    }

    static CharSequence insertNewlineText(CharSequence text, int selectionStart, int selectionEnd) {
        String current = text == null ? "" : text.toString();
        int start = clampSelection(selectionStart, current.length());
        int end = clampSelection(selectionEnd, current.length());
        int min = Math.min(start, end);
        int max = Math.max(start, end);
        return current.substring(0, min) + "\n" + current.substring(max);
    }

    static CharSequence editableText(
            CharSequence text,
            CharSequence hintText,
            boolean showingHintText,
            int selectionStart,
            int selectionEnd
    ) {
        if (showingHintText || isHintText(text, hintText) || isLikelyPlaceholderText(
                text,
                selectionStart,
                selectionEnd
        )) {
            return "";
        }
        return text;
    }

    private static int clampSelection(int selection, int length) {
        if (selection < 0) {
            return length;
        }
        return Math.min(selection, length);
    }

    private static void insertNewline(AccessibilityNodeInfo editable) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                insertNewlineText(
                        editableText(
                                editable.getText(),
                                hintText(editable),
                                isShowingHintText(editable),
                                editable.getTextSelectionStart(),
                                editable.getTextSelectionEnd()
                        ),
                        editable.getTextSelectionStart(),
                        editable.getTextSelectionEnd()
                )
        );
        editable.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    private static boolean isShowingHintText(AccessibilityNodeInfo node) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && node.isShowingHintText();
    }

    private static CharSequence hintText(AccessibilityNodeInfo node) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? node.getHintText() : null;
    }

    private static boolean isHintText(CharSequence text, CharSequence hintText) {
        return text != null
                && hintText != null
                && text.toString().contentEquals(hintText);
    }

    private static boolean isLikelyPlaceholderText(
            CharSequence text,
            int selectionStart,
            int selectionEnd
    ) {
        if (text == null || selectionStart > 0 || selectionEnd > 0) {
            return false;
        }

        String normalizedText = normalize(text);
        for (String placeholderText : PLACEHOLDER_TEXTS) {
            if (normalizedText.equals(normalize(placeholderText))) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(CharSequence value) {
        return value.toString()
                .toLowerCase(Locale.US)
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }
}
