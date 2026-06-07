package com.q25.inputhelper.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.view.KeyEvent;

import org.junit.Test;

public final class Q25KeyTranslatorTest {
    @Test
    public void q25PinKeysMapToDigits() {
        assertEquals(Q25KeyTranslator.Input.DIGIT_1, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_W));
        assertEquals(Q25KeyTranslator.Input.DIGIT_5, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_D));
        assertEquals(Q25KeyTranslator.Input.DIGIT_9, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_C));
        assertEquals(Q25KeyTranslator.Input.DIGIT_0, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_0));
    }

    @Test
    public void controlKeysMapToPinActions() {
        assertEquals(Q25KeyTranslator.Input.ENTER, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_ENTER));
        assertEquals(Q25KeyTranslator.Input.ENTER, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_DPAD_CENTER));
        assertEquals(Q25KeyTranslator.Input.DELETE, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_DEL));
    }

    @Test
    public void unrelatedKeysAreIgnored() {
        assertNull(Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_A));
        assertNull(Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_ALT_RIGHT));
    }

    @Test
    public void pinInputsMapToSystemUiButtonIds() {
        assertEquals("com.android.systemui:id/key1",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.DIGIT_1));
        assertEquals("com.android.systemui:id/key_enter",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.ENTER));
        assertEquals("com.android.systemui:id/delete_button",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.DELETE));
    }
}
