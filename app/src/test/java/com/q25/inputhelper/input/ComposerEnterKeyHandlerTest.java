package com.q25.inputhelper.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

public final class ComposerEnterKeyHandlerTest {
    @Test
    public void isHandledEnterKeyHandlesEnterDownAndUp() {
        assertTrue(ComposerEnterKeyHandler.isHandledEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        assertTrue(ComposerEnterKeyHandler.isHandledEnterKey(
                KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
    }

    @Test
    public void isHandledEnterKeyIgnoresNonEnterKeysAndOtherActions() {
        assertFalse(ComposerEnterKeyHandler.isHandledEnterKey(
                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE));
        assertFalse(ComposerEnterKeyHandler.isHandledEnterKey(
                KeyEvent.ACTION_MULTIPLE, KeyEvent.KEYCODE_ENTER));
        assertFalse(ComposerEnterKeyHandler.isHandledEnterEvent(null));
    }

    @Test
    public void shouldArmSendOnKeyDownOnlyForInitialPlainEnterWithDraft() {
        assertTrue(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(false, 0, true));

        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(true, 0, true));
        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(false, 1, true));
        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(false, 0, false));
    }

    @Test
    public void shouldClickSendButtonOnKeyUpOnlyForPendingPlainEnterWithDraft() {
        assertTrue(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(false, true, true));

        assertFalse(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(true, true, true));
        assertFalse(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(false, false, true));
        assertFalse(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(false, true, false));
    }

    @Test
    public void isAltMetaStateHandlesLeftAndRightAlt() {
        assertTrue(ComposerEnterKeyHandler.isAltMetaState(KeyEvent.META_ALT_ON));
        assertTrue(ComposerEnterKeyHandler.isAltMetaState(KeyEvent.META_ALT_LEFT_ON));
        assertTrue(ComposerEnterKeyHandler.isAltMetaState(KeyEvent.META_ALT_RIGHT_ON));

        assertFalse(ComposerEnterKeyHandler.isAltMetaState(0));
    }

    @Test
    public void hasDraftTextRequiresNonBlankText() {
        assertTrue(ComposerEnterKeyHandler.hasDraftText("hello"));

        assertFalse(ComposerEnterKeyHandler.hasDraftText(null));
        assertFalse(ComposerEnterKeyHandler.hasDraftText(""));
        assertFalse(ComposerEnterKeyHandler.hasDraftText("   "));
    }

    @Test
    public void isSendButtonViewIdHandlesNullViewIds() {
        assertTrue(ComposerEnterKeyHandler.isSendButtonViewId(
                "Compose:Draft:Send",
                "Compose:Draft:Send"));

        assertFalse(ComposerEnterKeyHandler.isSendButtonViewId("Compose:Draft:Send", null));
        assertFalse(ComposerEnterKeyHandler.isSendButtonViewId(
                "Compose:Draft:Send",
                "ComposeRowIcon:Gallery"));
    }

    @Test
    public void insertNewlineTextUsesSelectionOrAppendsWhenMissing() {
        assertTrue(ComposerEnterKeyHandler.insertNewlineText("hello", 2, 2).toString()
                .equals("he\nllo"));
        assertTrue(ComposerEnterKeyHandler.insertNewlineText("hello", 1, 4).toString()
                .equals("h\no"));
        assertTrue(ComposerEnterKeyHandler.insertNewlineText("hello", -1, -1).toString()
                .equals("hello\n"));
    }

    @Test
    public void composerDraftTextTreatsVisibleHintAsEmptyDraft() {
        assertTrue(ComposerEnterKeyHandler.composerDraftText("Text message", true).toString()
                .equals(""));
        assertTrue(ComposerEnterKeyHandler.composerDraftText("hello", false).toString()
                .equals("hello"));
    }
}
