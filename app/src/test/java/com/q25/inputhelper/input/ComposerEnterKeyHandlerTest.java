package com.q25.inputhelper.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

import java.util.Arrays;

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
    public void shouldArmSendOnKeyDownOnlyForInitialEnterWithDraftAndSendButton() {
        assertTrue(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(0, true, true));

        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(1, true, true));
        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(0, false, true));
        assertFalse(ComposerEnterKeyHandler.shouldArmSendOnKeyDown(0, true, false));
    }

    @Test
    public void shouldClickSendButtonOnKeyUpOnlyForPendingPlainEnterWithDraft() {
        assertTrue(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(true, true));

        assertFalse(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(false, true));
        assertFalse(ComposerEnterKeyHandler.shouldClickSendButtonOnKeyUp(true, false));
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
    public void isSupportedPackageMatchesExactAndSubpackages() {
        assertTrue(ComposerEnterKeyHandler.isSupportedPackage(
                Arrays.asList("com.whatsapp"),
                "com.whatsapp"));
        assertTrue(ComposerEnterKeyHandler.isSupportedPackage(
                Arrays.asList("org.telegram.messenger"),
                "org.telegram.messenger.web"));

        assertFalse(ComposerEnterKeyHandler.isSupportedPackage(
                Arrays.asList("com.whatsapp"),
                "com.example"));
        assertFalse(ComposerEnterKeyHandler.isSupportedPackage(
                Arrays.asList("com.whatsapp"),
                null));
    }

    @Test
    public void isLikelySendButtonMatchesKnownViewIdAndSendLabels() {
        assertTrue(ComposerEnterKeyHandler.isLikelySendButton(
                ComposerEnterKeyHandler.defaultSendButtonMatchers(),
                "Compose:Draft:Send",
                null,
                null));
        assertTrue(ComposerEnterKeyHandler.isLikelySendButton(
                ComposerEnterKeyHandler.defaultSendButtonMatchers(),
                "com.example:id/send_button",
                null,
                null));
        assertTrue(ComposerEnterKeyHandler.isLikelySendButton(
                ComposerEnterKeyHandler.defaultSendButtonMatchers(),
                null,
                "Send message",
                null));
        assertTrue(ComposerEnterKeyHandler.isLikelySendButton(
                ComposerEnterKeyHandler.defaultSendButtonMatchers(),
                null,
                null,
                "Submit"));

        assertFalse(ComposerEnterKeyHandler.isLikelySendButton(
                ComposerEnterKeyHandler.defaultSendButtonMatchers(),
                "com.example:id/gallery",
                "Attach image",
                null));
    }

    @Test
    public void composerDraftTextTreatsVisibleHintAsEmptyDraft() {
        assertTrue(ComposerEnterKeyHandler.composerDraftText("Text message", true).toString()
                .equals(""));
        assertTrue(ComposerEnterKeyHandler.composerDraftText("hello", false).toString()
                .equals("hello"));
    }
}
