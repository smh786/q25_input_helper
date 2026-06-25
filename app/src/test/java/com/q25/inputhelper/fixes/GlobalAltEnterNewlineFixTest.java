package com.q25.inputhelper.fixes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

public final class GlobalAltEnterNewlineFixTest {
    @Test
    public void isHandledAltEnterKeyHandlesAltEnterDownAndUp() {
        assertTrue(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.META_ALT_LEFT_ON));
        assertTrue(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_ENTER,
                KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON));
    }

    @Test
    public void isHandledAltEnterKeyIgnoresPlainEnterAndOtherKeys() {
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.META_ALT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.META_ALT_RIGHT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_ENTER,
                KeyEvent.META_ALT_ON | KeyEvent.META_ALT_RIGHT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.META_SHIFT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_ENTER,
                KeyEvent.META_ALT_LEFT_ON | KeyEvent.META_SHIFT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE, KeyEvent.META_ALT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterKey(
                KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_ENTER, KeyEvent.META_ALT_LEFT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isHandledAltEnterEvent(null));
    }

    @Test
    public void isLeftAltPressedOnlyHandlesLeftAlt() {
        assertTrue(GlobalAltEnterNewlineFix.isLeftAltPressed(KeyEvent.META_ALT_LEFT_ON));
        assertTrue(GlobalAltEnterNewlineFix.isLeftAltPressed(
                KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON));

        assertFalse(GlobalAltEnterNewlineFix.isLeftAltPressed(0));
        assertFalse(GlobalAltEnterNewlineFix.isLeftAltPressed(KeyEvent.META_ALT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isLeftAltPressed(KeyEvent.META_ALT_RIGHT_ON));
        assertFalse(GlobalAltEnterNewlineFix.isLeftAltPressed(
                KeyEvent.META_ALT_ON | KeyEvent.META_ALT_RIGHT_ON));
    }

    @Test
    public void isShiftPressedHandlesLeftAndRightShift() {
        assertTrue(GlobalAltEnterNewlineFix.isShiftPressed(KeyEvent.META_SHIFT_ON));
        assertTrue(GlobalAltEnterNewlineFix.isShiftPressed(KeyEvent.META_SHIFT_LEFT_ON));
        assertTrue(GlobalAltEnterNewlineFix.isShiftPressed(KeyEvent.META_SHIFT_RIGHT_ON));

        assertFalse(GlobalAltEnterNewlineFix.isShiftPressed(0));
    }

    @Test
    public void insertNewlineTextUsesSelectionOrAppendsWhenMissing() {
        assertTrue(GlobalAltEnterNewlineFix.insertNewlineText("hello", 2, 2).toString()
                .equals("he\nllo"));
        assertTrue(GlobalAltEnterNewlineFix.insertNewlineText("hello", 1, 4).toString()
                .equals("h\no"));
        assertTrue(GlobalAltEnterNewlineFix.insertNewlineText("hello", -1, -1).toString()
                .equals("hello\n"));
    }

    @Test
    public void editableTextTreatsVisibleHintAsEmptyText() {
        assertTrue(GlobalAltEnterNewlineFix.editableText("Text message", null, true, -1, -1).toString()
                .equals(""));
        assertTrue(GlobalAltEnterNewlineFix.editableText("hello", null, false, 5, 5).toString()
                .equals("hello"));
    }

    @Test
    public void editableTextTreatsHintTextAsEmptyText() {
        assertTrue(GlobalAltEnterNewlineFix.editableText("Message", "Message", false, 0, 0)
                .toString()
                .equals(""));
    }

    @Test
    public void editableTextTreatsKnownPlaceholderWithNoCursorAsEmptyText() {
        assertTrue(GlobalAltEnterNewlineFix.editableText("Type a message", null, false, -1, -1)
                .toString()
                .equals(""));
        assertTrue(GlobalAltEnterNewlineFix.editableText("Ask anything", null, false, 0, 0)
                .toString()
                .equals(""));
    }

    @Test
    public void editableTextPreservesPlaceholderLikeUserTextWhenCursorIsInsideText() {
        assertTrue(GlobalAltEnterNewlineFix.editableText("Message", null, false, 7, 7)
                .toString()
                .equals("Message"));
    }

    @Test
    public void isKnownMultilineComposerPackageMatchesChatApps() {
        assertTrue(GlobalAltEnterNewlineFix.isKnownMultilineComposerPackage("com.openai.chatgpt"));
        assertTrue(GlobalAltEnterNewlineFix.isKnownMultilineComposerPackage("com.whatsapp.w4b"));

        assertFalse(GlobalAltEnterNewlineFix.isKnownMultilineComposerPackage("com.android.settings"));
        assertFalse(GlobalAltEnterNewlineFix.isKnownMultilineComposerPackage(null));
    }
}
